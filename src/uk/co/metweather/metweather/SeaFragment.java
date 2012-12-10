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

public class SeaFragment extends Fragment {
	
	public int site_position;
	public ImageView imageViewTide;
	public ImageView imageViewWave;
	public ImageView imageViewPeri;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
	

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {
    	
    	View view = inflater.inflate(R.layout.fragment_sea, container, false);
    	
		site_position = getArguments().getInt(ConditionsActivity.SITE_POSITION);
		
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
		
		return view;
	
    }
    
    
    public void updateSiteView() {
    	new DownloadImageTask().execute(imageViewTide);
    	if (site_position == 2) {
	    	new DownloadImageTask().execute(imageViewWave);
	    	new DownloadImageTask().execute(imageViewPeri);
    	}
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
