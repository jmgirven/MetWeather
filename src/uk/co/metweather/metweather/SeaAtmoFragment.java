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

public class SeaAtmoFragment extends Fragment {
	// ARGH! Ugly!
	// Copy paste code from SeaFragment and AtmoFragment
	// Should be fragments inside fragments
	// Implemented in API level 17 and support libraries, but doesn't work for me
	
	public int site_position;	

	public ImageView imageViewTide;
	public ImageView imageViewWave;
	public ImageView imageViewPeri;
	
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
    	
    	View view = inflater.inflate(R.layout.fragment_sea_atmospheric, container, false);
    	
		site_position = getArguments().getInt(ConditionsActivity.SITE_POSITION);
		
		
    	// --- Sea ---
		imageViewTide = (ImageView) view.findViewById(R.id.sea_image_tide);
		if (site_position == 2) {
			imageViewWave  = (ImageView) view.findViewById(R.id.sea_image_wave);
			imageViewPeri  = (ImageView) view.findViewById(R.id.sea_image_peri);
		}

		
		imageViewTide.setTag(R.id.DownloadImageTask_url,
    			ListSites.ImageURLs[site_position]+"th.gif");
		if (site_position == 2) {
			imageViewWave.setTag(R.id.DownloadImageTask_url,
	    			ListSites.ImageURLs[site_position]+"wh.gif");
			imageViewPeri.setTag(R.id.DownloadImageTask_url,
	    			ListSites.ImageURLs[site_position]+"pd.gif");
		}
    	
    	
    	// --- Atmo ---
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
    		imageViewTide.setImageBitmap( (Bitmap) savedInstanceState.getParcelable("imageViewTide") );
    		if (site_position == 2) {
    			imageViewWave.setImageBitmap(
    					(Bitmap) savedInstanceState.getParcelable("imageViewWave") );
    			imageViewPeri.setImageBitmap(
    					(Bitmap) savedInstanceState.getParcelable("imageViewPeri") );
    		}
    		
    		imageViewTemp.setImageBitmap( (Bitmap) savedInstanceState.getParcelable("imageViewTemp") );
    		imageViewPres.setImageBitmap( (Bitmap) savedInstanceState.getParcelable("imageViewPres") );
    		if (site_position == 0) { imageViewVis.setImageBitmap( (Bitmap) savedInstanceState.getParcelable("imageViewVis") ); }
    	}




    	imageViewTide.setClickable(true);
    	if (site_position == 2) {
    		imageViewWave.setClickable(true);
    		imageViewPeri.setClickable(true);
    	}
    	
    	imageViewTide.setTag(R.id.SitePosition, site_position);
    	imageViewTide.setTag(R.id.FragmentPosition, 2);
    	if (site_position == 2) {
    		imageViewWave.setTag(R.id.SitePosition, site_position);
    		imageViewPeri.setTag(R.id.SitePosition, site_position);
    		imageViewWave.setTag(R.id.FragmentPosition, 2);
    		imageViewPeri.setTag(R.id.FragmentPosition, 2);
    	}
    	
    	imageViewTide.setOnClickListener(new MyOnClickListener(imageViewTide));
    	if (site_position == 2) {
    		imageViewWave.setOnClickListener(new MyOnClickListener(imageViewWave));
    		imageViewPeri.setOnClickListener(new MyOnClickListener(imageViewPeri));
    	}
    	
    	imageViewTemp.setClickable(true);
    	imageViewPres.setClickable(true);
    	if (site_position == 0) { imageViewVis.setClickable(true); }

    	imageViewTemp.setTag(R.id.SitePosition, site_position);
    	imageViewPres.setTag(R.id.SitePosition, site_position);
    	imageViewTemp.setTag(R.id.FragmentPosition, 1);
    	imageViewPres.setTag(R.id.FragmentPosition, 1);
    	if (site_position == 0) {
    		imageViewVis.setTag(R.id.SitePosition, site_position);
    		imageViewVis.setTag(R.id.FragmentPosition, 1);
    	}
    	
    	imageViewTemp.setOnClickListener(new MyOnClickListener(imageViewTemp));
    	imageViewPres.setOnClickListener(new MyOnClickListener(imageViewPres));
    	if (site_position == 0) { imageViewVis.setOnClickListener(new MyOnClickListener(imageViewVis)); }
		
    	
		return view;
    }
    
    
    public void updateSiteView() {
    	new DownloadImageTask().execute(imageViewTide);
    	if (site_position == 2) {
	    	new DownloadImageTask().execute(imageViewWave);
	    	new DownloadImageTask().execute(imageViewPeri);
    	}

    	new DownloadImageTask().execute(imageViewTemp);
    	new DownloadImageTask().execute(imageViewPres);
    	if (site_position == 0) { new DownloadImageTask().execute(imageViewVis); }
    }
    
    
    @Override
    public void onSaveInstanceState(Bundle outState){
    	// Switching away from fragment, save images to be reused in savedInstanceState
    	BitmapDrawable image;
    	
    	image = (BitmapDrawable) imageViewTide.getDrawable();
    	if (image != null) {
    		outState.putParcelable("imageViewTide", image.getBitmap());
    	}
    	
    	if (site_position == 2) { 
	    	image = (BitmapDrawable) imageViewWave.getDrawable();
	    	if (image != null) {
	    		outState.putParcelable("imageViewWave", image.getBitmap());
	    	}

	    	image = (BitmapDrawable) imageViewPeri.getDrawable();
	    	if (image != null) {
	    		outState.putParcelable("imageViewPeri", image.getBitmap());
	    	}
    	}
    	
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
