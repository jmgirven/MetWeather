package uk.co.metweather.metweather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

public class GetWebpageInfo extends AsyncTask<TextView, Void, TextView[]> {
	// Download the HTML source of a website
	// Process the text and display in a text view
	List<String> text = new ArrayList<String>();
    HttpURLConnection conn = null;
    InputStreamReader in = null;
    BufferedReader buff = null;
	TextView textView = null;
	
	// ImageView to be downloaded after this asynctask
	ImageView imageView = null;
    
	//Log key
    // String TAG = "GetWebpageInfo";

	@Override
	protected TextView[] doInBackground(TextView... textViews) {
		// Check all URLs are the same
		String url = (String) textViews[0].getTag(R.id.GetWebpageInfoURL);
		// for (int iii=1; iii<textViews.length; iii++) {
		// 	if ( textViews[iii].getTag(R.id.GetWebpageInfoURL) != url ) {
		// 		Log.e(TAG, "URLs are not the same.");
		// 	}
		// }
		
	    // Download whole HTML file
	    text = HTMLLines((String) url);
	    
		// For each text view given
		for (int iii=0; iii<textViews.length; iii++) {
		    this.textView = textViews[iii];
		    
		    // Process the text view according to its type tag
		    // Save it in the text tag
		    if (textView.getTag(R.id.GetWebpageInfoType) == "wind") {
		    	textView.setTag( R.id.GetWebpageInfoText, ProcessWind(text,
		    			(Integer) textView.getTag(R.id.GetWebpageInfoStart),
		    			(Integer) textView.getTag(R.id.GetWebpageInfoEnd) ) );
				
		    	// ImageView to be processed after completion of this task
		    	// Based on information passed to wind TextView
			    if (imageView != null) {
			    	// Grab wind direction text
			    	imageView.setTag( R.id.DownloadImageTask_url,
			    			DirectionToURL( StripHTML(text, 76, 77) ) );
			    }
				
		    } else if (textView.getTag(R.id.GetWebpageInfoType) == "sea") {
		    	textView.setTag( R.id.GetWebpageInfoText, ProcessSea(text,
		    			(Integer) textView.getTag(R.id.GetWebpageInfoStart),
		    			(Integer) textView.getTag(R.id.GetWebpageInfoEnd) ) );
		    } else if (textView.getTag(R.id.GetWebpageInfoType) == "atmo") {
		    	textView.setTag( R.id.GetWebpageInfoText, ProcessAtmo(text,
		    			(Integer) textView.getTag(R.id.GetWebpageInfoStart),
		    			(Integer) textView.getTag(R.id.GetWebpageInfoEnd) ) );
		    }
		}
		
		return textViews;
	}

	@Override
	protected void onPostExecute(TextView[] textViews) {
		// For each text view given
		for (int iii=0; iii<textViews.length; iii++) {
			// Take the text from the text tag and set to the text of the view
			this.textView = textViews[iii];
			textView.setText( (String) textView.getTag( R.id.GetWebpageInfoText ) );

			// ImageView to be processed after completion of this task
	    	// Based on information passed to wind textview
		    if (imageView != null) {
		    	new DownloadImageTask().execute(imageView);
		    }
		}
	}
    
    public List<String> HTMLLines( String url ) {
    	// Given a URL return the HTML on that page    
	    try {
	        URL page = new URL(url);
	        // URLEncoder.encode(someparameter); use when passing params that may
	        	// contain symbols or spaces use URLEncoder to encode it and convert
	        	// space to %20...etc other wise you will get a 404
            
	        conn = (HttpURLConnection) page.openConnection();
	        conn.connect();
	
	        in = new InputStreamReader((InputStream) conn.getContent());
	        buff = new BufferedReader(in);
	        
	        String line = "anything";
	        
	        while (line != null) {
	            line = buff.readLine();
        		text.add( line );
	        }

        } catch (MalformedURLException e) {
            // Log.e(TAG, "Malformed exception: " + e.toString());

        } catch (IOException e) {
            // Log.e(TAG, "IOException: " + e.toString());

        } catch (Exception e) {
	        // Log.e(TAG,
	        //         "Exception while getting html from website, exception: "
	        //                 + e.toString() + ", cause: " + e.getCause()
	        //                 + ", message: " + e.getMessage());
	        
	    } finally {
	        if (null != buff) {
	            try {
	                buff.close();
	            } catch (IOException e1) {
	            }
	            buff = null;
	        }
	        if (null != in) {
	            try {
	                in.close();
	            } catch (IOException e1) {
	            }
	            in = null;
	        }
	        if (null != conn) {
	            conn.disconnect();
	            conn = null;
	        }
	    }
	    
	    if (text.size() > 0) {
	        return text;
	    } else return null;
	    
	}

	
	private String ProcessWind(List<String> text, int start, int end) {
		String output = "";
		
		List<String> result = StripHTML(text, start, end);
		
		output += result.get(1) + "\n";
		output += result.get(3) + "\n";
		output += result.get(5) + "\n";

	    return output;
	}

	
	private String ProcessSea(List<String> text, int start, int end) {
		String output = "";
		List<String> result = StripHTML(text, start, end);
		output += result.get(1) + "\n";
		
		// Deal with Chimet supplementary information
		if (result.size() > 2) {
			output += result.get(2).replace("Wave Height (mean) ", "") + "\n";
			output += result.get(3).replace("Wave Height (maximum)", "") + "\n";
			output += result.get(4).replace("Wave Periodicity", "") + "\n";
		}
		
	    return output;
	}

	
	private String ProcessAtmo(List<String> text, int start, int end) {
		String output = "";
		
		List<String> result = StripHTML(text, start, end);
		
		output += result.get(0).replace("Air", "") + "\n";
		
		if (result.get(1).startsWith("Sea")) {
			output += result.get(1).replace("Sea", "") + "\n";
		} else {
			output += "\n" + result.get(1).replace("Barometric Pressure", "") + "\n";
		}
		
		if (result.size() > 2) {
			output += result.get(2).replace("Barometric Pressure", "") + "\n";
		}
		
		if (result.size() > 3) {
			output += result.get(3).replace("Visibility", "") + "\n";
		}

	    return output;
	}
	
	
	private List<String> StripHTML(List<String> text, int start, int end) {
		// Given the whole HTML file, and the start and end points of interest
		// Return that section of the file, stripped of HTML tags and blanks
		List<String> result = new ArrayList<String>();
		
		for (int iii = start; iii <= end; iii++) {
			String tmp = android.text.Html.fromHtml(text.get(iii)).toString();
			if (tmp.length() > 0) {	result.add( tmp ); }
		}
		
		return result;
	}
	
	
	private String DirectionToURL(List<String> direction) {
		// Two possible formats of input
		// ["Direction", "NW"] or ["NW"]
		// Depending on site
		if (direction.size() == 2) {
			return Internal(direction.get(1));
		}
		else if (direction.size() == 1) {
			return Internal(direction.get(0));
		}
		return null;
	}
	
	private String Internal( String d ) {
		// Take e.g. "NW" and convert it to "...comp30.gif"
		Map<String, String> mp = new HashMap<String, String>();
		
		mp.put("N", "0");
		mp.put("NNE", "2");
		mp.put("NE", "4");
		mp.put("ENE", "6");
		mp.put("E", "8");
		mp.put("ESE", "10");
		mp.put("SE", "12");
		mp.put("SSE", "14");
		mp.put("S", "16");
		mp.put("SSW", "18");
		mp.put("SW", "20");
		mp.put("WSW", "22");
		mp.put("W", "24");
		mp.put("WNW", "26");
		mp.put("NW", "28");
		mp.put("NNW", "30");
		
		// Site information already in tag
		return (String) imageView.getTag(R.id.DownloadImageTask_url)
				+ mp.get(d) + ".gif";
	}
	
}
