package uk.co.metweather.metweather;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MyOnClickListener implements OnClickListener {
	
	ImageView imageView;
	int site_position;
	int fragment_position;
	BitmapDrawable image;
	
	public MyOnClickListener(ImageView iv) {
		imageView = iv;
		// Site and fragment position used in back button
		site_position = (Integer) iv.getTag(R.id.SitePosition);
		fragment_position = (Integer) iv.getTag(R.id.FragmentPosition);
	}
	
    @Override
    public void onClick(View v) {
        // Open new activity
    	Intent myIntent = new Intent(v.getContext(), FullScreenImageActivity.class);
    	
    	// Pass bitmap in bundle
    	Bundle args = new Bundle();
    	image = (BitmapDrawable) imageView.getDrawable();
    	args.putParcelable(FullScreenImageActivity.FullScreenImageTag,
    			image.getBitmap());

    	args.putInt(ConditionsActivity.SITE_POSITION, site_position);
    	args.putInt(ConditionsActivity.FRAGMENT_POSITION, fragment_position);
    	
    	myIntent.putExtras(args);
    	
    	v.getContext().startActivity(myIntent);
    }

}
