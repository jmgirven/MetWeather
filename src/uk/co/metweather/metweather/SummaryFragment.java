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

public class SummaryFragment extends Fragment {
	
	public int site_position;
	
	TextView textViewWind;
	ImageView imageViewWind;
	TextView textViewSea;
	TextView textViewSeaTitle;
	TextView textViewAtmo;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
	

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {
    	
    	View view = inflater.inflate(R.layout.fragment_summary, container, false);
    	
		site_position = getArguments().getInt(ConditionsActivity.SITE_POSITION);
		
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
		
    	if (savedInstanceState == null) {
    		updateSiteView();
    	} else {
    		imageViewWind.setImageBitmap( (Bitmap) savedInstanceState.getParcelable("imageViewWind") );
    		textViewWind.setText( savedInstanceState.getCharSequence("textViewWind") );
    		textViewSea .setText( savedInstanceState.getCharSequence("textViewSea") );
    		textViewAtmo.setText( savedInstanceState.getCharSequence("textViewAtmo") );
    	}
    	
		return view;
    }
    
    
    public void updateSiteView() {
    	GetWebpageInfo getWebpageInfo = new GetWebpageInfo();
    	
    	// For refresh button, need to (re)set Tag here
    	imageViewWind.setTag(R.id.DownloadImageTask_url,
    			ListSites.CompassURLs[site_position]);
    	
    	getWebpageInfo.imageView = imageViewWind;
    	getWebpageInfo.execute(textViewWind, textViewSea, textViewAtmo);
    }
    
    
    @Override
    public void onSaveInstanceState(Bundle outState){
    	// Switching away from fragment, save images and text to be
    	// reused in savedInstanceState
    	
    	BitmapDrawable image = (BitmapDrawable) imageViewWind.getDrawable();
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
