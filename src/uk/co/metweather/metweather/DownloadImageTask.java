package uk.co.metweather.metweather;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class DownloadImageTask extends AsyncTask<ImageView, Void, Bitmap> {
	// Download an image from the web asynchronously and display in an ImageView
	// Pass an ImageView with the URL in the Tag R.id.DownloadImageTask_url
	// Optional pass the required width and height of the image in
	// R.id.DownloadImageTask_width and R.id.DownloadImageTask_height
	
	ImageView imageView = null;
	
	// String TAG = "DownloadImageTag";

	@Override
	protected Bitmap doInBackground(ImageView... imageViews) {
	    this.imageView = imageViews[0];
	    
	    Bitmap result = download_Image((String)
	    		imageView.getTag(R.id.DownloadImageTask_url));
	    
	    // If image width and height specified
	    if (imageView.getTag(R.id.DownloadImageTask_width) == null ||
	    		imageView.getTag(R.id.DownloadImageTask_height) == null) {
	    	return result;
	    } else {
	    	return Bitmap.createScaledBitmap(result,
	    			(Integer) imageView.getTag(R.id.DownloadImageTask_width),
	    			(Integer) imageView.getTag(R.id.DownloadImageTask_height), false);
	    }
	}

	@Override
	protected void onPostExecute(Bitmap result) {
	    imageView.setImageBitmap(result);
	}


	private Bitmap download_Image(String url) {
	    Bitmap bitmap = null;
	    
	    try {
	    	
	        URL aURL = new URL(url);
	        URLConnection conn = aURL.openConnection();
	        conn.connect();
	        InputStream is = conn.getInputStream();
	        BufferedInputStream bis = new BufferedInputStream(is);
	        bitmap = BitmapFactory.decodeStream(bis);
            
	        bis.close();
	        is.close();

        } catch (MalformedURLException e) {
            // Log.e(TAG, "Malformed exception: " + e.toString());

        } catch (IOException e) {
            // Log.e(TAG, "IOException: " + e.toString());

        } catch (Exception e) {
	    } 
	    
	    return bitmap;

	}
	
}
