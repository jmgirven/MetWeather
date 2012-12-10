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

public class AtmosphericFragment extends Fragment {

	public int site_position;
	
	public ImageView imageViewTemp;
	public ImageView imageViewPres;
	public ImageView imageViewVis;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
	

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {
    	
    	View view = inflater.inflate(R.layout.fragment_atmospheric, container, false);

		site_position = getArguments().getInt(ConditionsActivity.SITE_POSITION);

		imageViewTemp = (ImageView) view.findViewById(R.id.atmo_image_temp);
		imageViewPres = (ImageView) view.findViewById(R.id.atmo_image_pres);
		if (site_position == 0) { imageViewVis  = (ImageView) view.findViewById(R.id.atmo_image_vis); }
		
    	imageViewTemp.setTag(R.id.DownloadImageTask_url,
    			ListSites.ImageURLs[site_position]+"tp.gif");
    	imageViewPres.setTag(R.id.DownloadImageTask_url,
    			ListSites.ImageURLs[site_position]+"bp.gif");
    	if (site_position == 0) { imageViewVis .setTag(R.id.DownloadImageTask_url,
    			ListSites.ImageURLs[site_position]+"vs.gif"); }

    	if (savedInstanceState == null) {
    		updateSiteView();
    	} else {
    		imageViewTemp.setImageBitmap( (Bitmap) savedInstanceState.getParcelable("imageViewTemp") );
    		imageViewPres.setImageBitmap( (Bitmap) savedInstanceState.getParcelable("imageViewPres") );
    		if (site_position == 0) { imageViewVis.setImageBitmap( (Bitmap) savedInstanceState.getParcelable("imageViewVis") ); }
    	}

    	
    	imageViewTemp.setClickable(true);
    	imageViewPres.setClickable(true);
    	if (site_position == 0) { imageViewVis.setClickable(true); }

    	imageViewTemp.setTag(R.id.SitePosition, site_position);
    	imageViewPres.setTag(R.id.SitePosition, site_position);
    	imageViewTemp.setTag(R.id.FragmentPosition, 3);
    	imageViewPres.setTag(R.id.FragmentPosition, 3);
    	if (site_position == 0) {
    		imageViewVis.setTag(R.id.SitePosition, site_position);
    		imageViewVis.setTag(R.id.FragmentPosition, 3);
    	}
    	
    	imageViewTemp.setOnClickListener(new MyOnClickListener(imageViewTemp));
    	imageViewPres.setOnClickListener(new MyOnClickListener(imageViewPres));
    	if (site_position == 0) { imageViewVis.setOnClickListener(new MyOnClickListener(imageViewVis)); }
		
		return view;
	
    }
    
    
    public void updateSiteView() {
    	new DownloadImageTask().execute(imageViewTemp);
    	new DownloadImageTask().execute(imageViewPres);
    	if (site_position == 0) { new DownloadImageTask().execute(imageViewVis); }
    }
    
    
    @Override
    public void onSaveInstanceState(Bundle outState){
    	// Switching away from fragment, save images to be reused in savedInstanceState
    	BitmapDrawable image;
    	
    	image = (BitmapDrawable) imageViewTemp.getDrawable();
    	if (image != null) {
    		outState.putParcelable("imageViewTemp", image.getBitmap());
    	}
    	
    	image = (BitmapDrawable) imageViewPres.getDrawable();
    	if (image != null) {
    		outState.putParcelable("imageViewPres", image.getBitmap());
    	}
    	
    	if (site_position == 0) { 
	    	image = (BitmapDrawable) imageViewVis.getDrawable();
	    	if (image != null) {
	    		outState.putParcelable("imageViewVis", image.getBitmap());
	    	}
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
		// Handle item selection
		switch (item.getItemId()) {
			case R.id.refresh:
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

}
