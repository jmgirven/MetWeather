package uk.co.metweather.metweather;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class ConditionsActivity extends FragmentActivity {
	// Initialise list position arguments
    final static String SITE_POSITION = "site";
    final static String FRAGMENT_POSITION = "fragment";
    Bundle inputArgs;
    
	// Create a ViewPager to hold the fragments
	// The PagerAdapter deals with changing the fragments
	
	// Slider bits
	ConditionsPagerAdapter mConditionsPagerAdapter;
	ViewPager mViewPager;

	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_holder);        
        
        // If screen is large, load two fragments
        // else, just one at a time
        if ( 
        		((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE)
        		|| ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE)
        		// && (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        		) {
            mConditionsPagerAdapter = new ConditionsPagerAdapter_TwoFragments(getSupportFragmentManager());
        } else {
	        // Adapter to return fragments
	        mConditionsPagerAdapter = new ConditionsPagerAdapter(getSupportFragmentManager());
        }

        // Set up the ViewPager with the adapter.
        mViewPager = (ViewPager) findViewById(R.id.fragment_container);
        mViewPager.setAdapter(mConditionsPagerAdapter);

        if (savedInstanceState == null) {
        	inputArgs = getIntent().getExtras();
        	mViewPager.setCurrentItem(inputArgs.getInt(FRAGMENT_POSITION));
		} else {
			inputArgs = savedInstanceState;
		}

    	setTitle(ListSites.Sites[inputArgs.getInt(SITE_POSITION)]);
    }
	
	
	@Override
	// Make site position always available
	public void onSaveInstanceState( Bundle outState ) {
		outState.putInt(SITE_POSITION, inputArgs.getInt(SITE_POSITION));
		super.onSaveInstanceState(outState);
	}
    
    

    public class ConditionsPagerAdapter extends FragmentStatePagerAdapter {
    	// Adapter to return fragments

        public ConditionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
        	switch (i) {
    			case 0: return getSummaryFragment();
    			case 1: return getWindFragment();
    			case 2: return getSeaFragment();
    			case 3: return getAtmosphericFragment();
        	}        
        	return null;
            
        }
        
        public SummaryFragment getSummaryFragment() {
        	SummaryFragment summaryFragment = new SummaryFragment();
        	summaryFragment.setArguments(inputArgs);
        	return summaryFragment;
        }
        
        public WindFragment getWindFragment() {
        	WindFragment windFragment = new WindFragment();
        	windFragment.setArguments(inputArgs);
        	return windFragment;
        }
        
        public SeaFragment getSeaFragment() {
        	SeaFragment seaFragment = new SeaFragment();
        	seaFragment.setArguments(inputArgs);
        	return seaFragment;
        }
        
        public AtmosphericFragment getAtmosphericFragment() {
        	AtmosphericFragment atmosphericFragment = new AtmosphericFragment();
        	atmosphericFragment.setArguments(inputArgs);
        	return atmosphericFragment;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: return "Summary";
                case 1: return "Wind";
                case 2: return "Sea";
                case 3: return "Atmospheric";
            }
            return null;
        }
    }
    
    

    public class ConditionsPagerAdapter_TwoFragments extends ConditionsPagerAdapter {
    	// Adapter to return fragments
    	// Returns two fragments for large mode

        public ConditionsPagerAdapter_TwoFragments(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
        	switch (i) {
    			case 0: return getSummaryWindFragment();
    			case 1: return getSeaAtmoFragment();
        	}        
        	return null;
            
        }
        
        public Fragment getSummaryWindFragment() {
        	SummaryWindFragment summaryWindFragment = new SummaryWindFragment();
        	summaryWindFragment.setArguments(inputArgs);
        	return summaryWindFragment;
        }
        
        public Fragment getSeaAtmoFragment() {
        	SeaAtmoFragment seaAtmoFragment = new SeaAtmoFragment();
        	seaAtmoFragment.setArguments(inputArgs);
        	return seaAtmoFragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: return "Summary & Wind";
                case 1: return "Sea & Atmospheric";
            }
            return null;
        }
    }
    
    
    // Menu
    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
    	// About app
        MenuItem about = menu.add(0, R.id.about_app, 0, R.string.title_about);
        about.setIcon(android.R.drawable.ic_dialog_info);
    	return super.onCreateOptionsMenu(menu);
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.about_app:
		    	Intent myIntent = new Intent(ConditionsActivity.this, AboutActivity.class);
	            myIntent.putExtra(ConditionsActivity.SITE_POSITION, 
	            		inputArgs.getInt(ConditionsActivity.SITE_POSITION));
	            myIntent.putExtra(ConditionsActivity.FRAGMENT_POSITION, 
	            		mViewPager.getCurrentItem());
		    	startActivity(myIntent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

}