package uk.co.metweather.metweather;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

public class FullScreenImageActivity extends Activity {
	// Display an image full screen
	// Fixed to landscape mode
	
	ZoomImageView imageView;
	final static String FullScreenImageTag = "bitmap";
	Bundle extras;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_screen_image_activity);
        imageView = (ZoomImageView) findViewById(R.id.full_screen_image);
        
        
        // Site position passed in extras
        extras = getIntent().getExtras();
        // Bitmap passed in extras
        Bitmap bitmap = (Bitmap) extras.getParcelable(FullScreenImageTag);
        
        
        // Scale bitmap such that width is display width
        // and bitmap aspect ratio is conserved
        int[] displayDimensions = getDisplayDimensions(getWindowManager());
        imageView.setImageBitmap( Bitmap.createScaledBitmap(bitmap,
        		displayDimensions[0],
        		bitmap.getHeight() * displayDimensions[0] / bitmap.getWidth(),
        		false) );
    }
    

	// Get display size uses different methods depending on api
    @SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
    static public int[] getDisplayDimensions(WindowManager windowManager) {
    	// [width, height]
    	int[] displayDimensions = new int[2];
    	
        // Get display size
        Display display = windowManager.getDefaultDisplay();
        
        // getWidth getHeight depreciated in API level 13
        if (android.os.Build.VERSION.SDK_INT >= 13) {
	        Point size = new Point();
	        display.getSize(size);
	        displayDimensions[0] = size.x;
	        displayDimensions[1] = size.y;
        } else {
        	displayDimensions[0] = display.getWidth();
        	displayDimensions[1] = display.getHeight();
        }
    	
        return displayDimensions;
    }
    
    
    // Menu
    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        MenuItem about = menu.add(0, R.id.about_app, 0, R.string.title_about);
        about.setIcon(android.R.drawable.ic_dialog_info);
    	return super.onCreateOptionsMenu(menu);
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent myIntent;
		
		switch (item.getItemId()) {
			// About button, seen all over the place
			case R.id.about_app:
		    	myIntent = new Intent(FullScreenImageActivity.this, AboutActivity.class);
		    	myIntent.putExtra(ConditionsActivity.SITE_POSITION, 
		    			extras.getInt(ConditionsActivity.SITE_POSITION));
	            myIntent.putExtra(ConditionsActivity.FRAGMENT_POSITION, 
	            		extras.getInt(ConditionsActivity.FRAGMENT_POSITION));
		    	startActivity(myIntent);
				return true;
				
			// Modified home button
			// Returns to ConditionsActivity, passing arguments
			case android.R.id.home:
	            myIntent = new Intent(this, ConditionsActivity.class);
	            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            myIntent.putExtra(ConditionsActivity.SITE_POSITION,
		    			extras.getInt(ConditionsActivity.SITE_POSITION));
	            myIntent.putExtra(ConditionsActivity.FRAGMENT_POSITION,
		    			extras.getInt(ConditionsActivity.FRAGMENT_POSITION));
	            startActivity(myIntent);
	            return true;
				
			default:
				return super.onOptionsItemSelected(item);
		}
	}

}
