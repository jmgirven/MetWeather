package uk.co.metweather.metweather;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class MainActivity extends FragmentActivity {
	// Create a list of the four sites
	// On click, create a ConditionsActivity
	// Pass which site was clicked

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.site_list);

        // We need to use a different list item layout for devices older than Honeycomb
        int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;
        
        ListView listView = (ListView) findViewById(R.id.site_list);

        // Create an array adapter for the list view, using the SiteList Sites array
        listView.setAdapter(new ArrayAdapter<String>(this, layout, ListSites.Sites)); 
        
        // Map image
        ImageView imageView = (ImageView) findViewById(R.id.site_list_image);
        imageView.setImageResource(R.drawable.map);
        
        listView.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view,
        	        int site_position, long id) {
                // Test for Internet connection
                if (!isOnline()) {
    	        	FragmentManager fm = getSupportFragmentManager();
    	        	EnableInternetDialogFragment enableInternetDialogFragment = new EnableInternetDialogFragment();
    	        	enableInternetDialogFragment.show(fm, "fragment_enable_internet_dialog");
                } else {
			    	Intent myIntent = new Intent(MainActivity.this, ConditionsActivity.class);
			    	Bundle args = new Bundle();
			    	args.putInt(ConditionsActivity.SITE_POSITION, site_position);
			    	myIntent.putExtras(args);
			    	startActivity(myIntent);
                }
        	}
        });
    }
    
    
    // Test for Internet connection
    public boolean isOnline() {
        ConnectivityManager cm =
            (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
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
		switch (item.getItemId()) {
			case R.id.about_app:
		    	Intent myIntent = new Intent(MainActivity.this, AboutActivity.class);
		    	// AboutActivity is capable of returning to Conditions activity
		    	// but if <0, returns to MainActivity
	            myIntent.putExtra(ConditionsActivity.SITE_POSITION, -1);
	            myIntent.putExtra(ConditionsActivity.FRAGMENT_POSITION, -1);
		    	startActivity(myIntent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
    
}