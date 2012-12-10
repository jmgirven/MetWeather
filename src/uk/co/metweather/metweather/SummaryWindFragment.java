package uk.co.metweather.metweather;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class SummaryWindFragment extends Fragment {
	// --- ARGH! Ugly! ---
	// Copy paste code from SummaryFragment and WindFragment
	// Should be fragments inside fragments
	// Implemented in API level 17 and support libraries, but doesn't work for me
	
	public int site_position;	
	
	TextView textViewWind;
	ImageView imageViewWind;
	TextView textViewSea;
	TextView textViewSeaTitle;
	TextView textViewAtmo;
	
	public ImageView imageViewWS;
	public ImageView imageViewWD;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
	

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {
    	
    	View view = inflater.inflate(R.layout.fragment_summary_wind, container, false);
    	
		site_position = getArguments().getInt(ConditionsActivity.SITE_POSITION);
		
    	// --- Summary ---
		// Wind
		// Text
		textViewWind = (TextView) view.findViewById(R.id.summary_wind_content);
		textViewWind.setTag(R.id.GetWebpageInfoURL,   ListSites.HomeURLs[site_position]);
		textViewWind.setTag(R.id.GetWebpageInfoStart, 67);
		textViewWind.setTag(R.id.GetWebpageInfoEnd,   77);
		textViewWind.setTag(R.id.GetWebpageInfoType,  "wind");
		//Image
		imageViewWind = (ImageView) view.findViewById(R.id.summary_wind_image);
    	imageViewWind.setTag(R.id.DownloadImageTask_url,
    			ListSites.CompassURLs[site_position]);
		
		// Sea
		textViewSea = (TextView) view.findViewById(R.id.summary_sea_content);
		textViewSea.setTag(R.id.GetWebpageInfoURL,   ListSites.HomeURLs[site_position]);
		textViewSea.setTag(R.id.GetWebpageInfoStart, 96);
		textViewSea.setTag(R.id.GetWebpageInfoEnd,   101);
		textViewSea.setTag(R.id.GetWebpageInfoType,  "sea");
		
		if (site_position != 2) {
			textViewSeaTitle = (TextView) view.findViewById(R.id.summary_sea_blurb);
			textViewSeaTitle.setText("Tidal Height:\t\n");
		}
		
		// Atmosphere
		textViewAtmo = (TextView) view.findViewById(R.id.summary_atmo_content);
		textViewAtmo.setTag(R.id.GetWebpageInfoURL,   ListSites.HomeURLs[site_position]);
		textViewAtmo.setTag(R.id.GetWebpageInfoStart, 113);
		textViewAtmo.setTag(R.id.GetWebpageInfoEnd,   117);
		textViewAtmo.setTag(R.id.GetWebpageInfoType,  "atmo");
		
		// --- Wind ---
		imageViewWS = (ImageView) view.findViewById(R.id.wind_image_ws);
		imageViewWD = (ImageView) view.findViewById(R.id.wind_image_wd);
		
    	imageViewWS.setTag(R.id.DownloadImageTask_url,
    			ListSites.ImageURLs[site_position]+"ws.gif");
    	imageViewWD.setTag(R.id.DownloadImageTask_url,
    			ListSites.ImageURLs[site_position]+"wd.gif");
		
		
    	if (savedInstanceState == null) {
    		updateSiteView();
    	} else {
    		imageViewWind.setImageBitmap( (Bitmap) savedInstanceState.getParcelable("imageViewWind") );
    		textViewWind.setText( savedInstanceState.getCharSequence("textViewWind") );
    		textViewSea .setText( savedInstanceState.getCharSequence("textViewSea") );
    		textViewAtmo.setText( savedInstanceState.getCharSequence("textViewAtmo") );
    		
    		imageViewWS.setImageBitmap( (Bitmap) savedInstanceState.getParcelable("imageViewWS") );
    		imageViewWD.setImageBitmap( (Bitmap) savedInstanceState.getParcelable("imageViewWD") );
    	}

    	imageViewWS.setClickable(true);
    	imageViewWD.setClickable(true);

    	imageViewWS.setTag(R.id.SitePosition, site_position);
    	imageViewWD.setTag(R.id.SitePosition, site_position);
    	imageViewWS.setTag(R.id.FragmentPosition, 0);
    	imageViewWD.setTag(R.id.FragmentPosition, 0);
    	
    	imageViewWS.setOnClickListener(new MyOnClickListener(imageViewWS));
    	imageViewWD.setOnClickListener(new MyOnClickListener(imageViewWD));
    	
		return view;
    }
    
    
    public void updateSiteView() {
    	GetWebpageInfo getWebpageInfo = new GetWebpageInfo();
    	
    	// For refresh button, need to (re)set Tag here
    	imageViewWind.setTag(R.id.DownloadImageTask_url,
    			ListSites.CompassURLs[site_position]);
    	
    	getWebpageInfo.imageView = imageViewWind;
    	getWebpageInfo.execute(textViewWind, textViewSea, textViewAtmo);

    	new DownloadImageTask().execute(imageViewWS);
    	new DownloadImageTask().execute(imageViewWD);
    }
    
    
    @Override
    public void onSaveInstanceState(Bundle outState){
    	// Switching away from fragment, save images to be reused in savedInstanceState
    	BitmapDrawable image;
    	
    	image = (BitmapDrawable) imageViewWS.getDrawable();
    	if (image != null) {
    		outState.putParcelable("imageViewWS", image.getBitmap());
    	}
    	
    	image = (BitmapDrawable) imageViewWD.getDrawable();
    	if (image != null) {
    		outState.putParcelable("imageViewWD", image.getBitmap());
    	}
    	
    	 image = (BitmapDrawable) imageViewWind.getDrawable();
    	if (image != null) {
    		outState.putParcelable("imageViewWind", image.getBitmap());
    	}
    	
    	outState.putCharSequence("textViewWind", textViewWind.getText());
    	outState.putCharSequence("textViewSea",  textViewSea.getText());
    	outState.putCharSequence("textViewAtmo", textViewAtmo.getText());
    	
    	super.onSaveInstanceState(outState);
    }

	
    // Menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Refresh
        MenuItem refresh = menu.add(0, R.id.refresh, 0, R.string.refresh_image);
        refresh.setIcon(R.drawable.ic_menu_refresh);
    	super.onCreateOptionsMenu(menu, inflater);
    }
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
			case R.id.refresh:
				updateSiteView();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

}
