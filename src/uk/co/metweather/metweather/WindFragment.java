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

public class WindFragment extends Fragment {

	public int site_position;
	
	ImageView imageViewWS;
	ImageView imageViewWD;
	
	int[] displayDimensions;


	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
	

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {
        
        // Inflate the layout for this fragment
    	View view = inflater.inflate(R.layout.fragment_wind, container, false);
    	
        // During create, check which site we are looking at
		// and which section
		site_position = getArguments().getInt(ConditionsActivity.SITE_POSITION);
		
		// get the latest imageView
		imageViewWS = (ImageView) view.findViewById(R.id.wind_image_ws);
		imageViewWD = (ImageView) view.findViewById(R.id.wind_image_wd);
		
		// Save image URL to tag
    	imageViewWS.setTag(R.id.DownloadImageTask_url,
    			ListSites.ImageURLs[site_position]+"ws.gif");
    	imageViewWD.setTag(R.id.DownloadImageTask_url,
    			ListSites.ImageURLs[site_position]+"wd.gif");
		
    	// If image not saved in cache, download, else restore
    	if (savedInstanceState == null) {
    		updateSiteView();
    	} else {
    		imageViewWS.setImageBitmap( (Bitmap) savedInstanceState.getParcelable("imageViewWS") );
    		imageViewWD.setImageBitmap( (Bitmap) savedInstanceState.getParcelable("imageViewWD") );
    	}

    	// Make images clickable
    	// Show full screen landscape version
    	imageViewWS.setClickable(true);
    	imageViewWD.setClickable(true);

    	// Pass arguments so that we know where to return too
    	imageViewWS.setTag(R.id.SitePosition, site_position);
    	imageViewWD.setTag(R.id.SitePosition, site_position);
    	imageViewWS.setTag(R.id.FragmentPosition, 1);
    	imageViewWD.setTag(R.id.FragmentPosition, 1);
    	
    	imageViewWS.setOnClickListener(new MyOnClickListener(imageViewWS));
    	imageViewWD.setOnClickListener(new MyOnClickListener(imageViewWD));
    	
        return view;
    }
    
    
    public void updateSiteView() {
    	// Used above and by refresh button
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
    	
    	super.onSaveInstanceState(outState);
    }

	
    // Menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	super.onCreateOptionsMenu(menu, inflater);
        MenuItem refresh = menu.add(0, R.id.refresh, 0, R.string.refresh_image);
        refresh.setIcon(R.drawable.ic_menu_refresh);
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.refresh:
				updateSiteView();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
