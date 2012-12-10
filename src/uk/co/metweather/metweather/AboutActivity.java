package uk.co.metweather.metweather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class AboutActivity extends Activity {
	Bundle extras;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
        TextView textView = (TextView) findViewById(R.id.aboutTextView);
        
        extras = getIntent().getExtras();
        
        textView.setText(R.string.aboutText);
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent myIntent;
		
		switch (item.getItemId()) {
			// Modified home button
			// Returns to MainActivity or ConditionsActivity, passing arguments
			case android.R.id.home:
				if (extras.getInt(ConditionsActivity.SITE_POSITION) < 0) {
					// If <0, return to MainActivity
		            myIntent = new Intent(this, MainActivity.class);
				} else {
			        // Otherwise, we came from ConditionsActivity
		            myIntent = new Intent(this, ConditionsActivity.class);
		            myIntent.putExtra(ConditionsActivity.SITE_POSITION,
		            		extras.getInt(ConditionsActivity.SITE_POSITION));
		            myIntent.putExtra(ConditionsActivity.FRAGMENT_POSITION,
		            		extras.getInt(ConditionsActivity.FRAGMENT_POSITION));
				}
				// This is a back button
	            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(myIntent);
	            return true;
				
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
