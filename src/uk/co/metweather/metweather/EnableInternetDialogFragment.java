package uk.co.metweather.metweather;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class EnableInternetDialogFragment extends DialogFragment {
	
	public EnableInternetDialogFragment() {
		// Empty constructor
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.dialog_enable_internet)
			   .setPositiveButton(R.string.dialog_positive, new DialogInterface.OnClickListener() {
				   public void onClick(DialogInterface dialog, int id) {
					   startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
				   }
			   })
			   .setNegativeButton(R.string.dialog_negative, new DialogInterface.OnClickListener() {
				   public void onClick(DialogInterface dialog, int id) {
				   }
			   });
		
		// Create the AlertDialog object and return it
		return builder.create();
	}
}
