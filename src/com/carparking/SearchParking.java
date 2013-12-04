package com.carparking;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class SearchParking extends FragmentActivity implements 
	GooglePlayServicesClient.ConnectionCallbacks,
	GooglePlayServicesClient.OnConnectionFailedListener{

	private static final String TAG = "SearchParking";
	Button currLocation,enterAddress;
	private LocationClient mLocationClient;
	private TextView mConnectionStatus;
	ConnectionDetector cd ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_carparking);
		currLocation = (Button) findViewById(R.id.button_current_add);
		enterAddress = (Button) findViewById(R.id.button_enter_add);
		mConnectionStatus = (TextView) findViewById(R.id.connStatus);
		cd = new ConnectionDetector(getApplicationContext());

		mLocationClient = new LocationClient(this, this, this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//Log.d(DEBUG_TAG, "onCreateOptionsMenu()");
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch(item.getItemId()) {

		case R.id.action_about: 
			startActivity(new Intent(this, AboutTheApp.class));
			return true;

		default:
			return false;
		}

	}
	
    @Override
    public void onStart() {
        super.onStart();
        mLocationClient.connect();
    }
	
	public void getCurrentLocation (View v) {
        if (cd.isConnectingToInternet() && servicesConnected() ) {
            // Get the current location
            Location currentLocation = mLocationClient.getLastLocation();
            startActivity(new Intent(this, MapActivity.class).putExtra("location", currentLocation));
        } else {
			new DialogManager("Please check wifi/data network settings", 
					"No Network Connection", this).show(getFragmentManager(), "NetworkMissing");
        }
	}

	public void onEnterAddressClick (View v) {
		
		startActivity(new Intent(this, EnterAddressActivity.class));
	}
	
    private boolean servicesConnected() {

        int resultCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (ConnectionResult.SUCCESS == resultCode) {
        	Log.d(TAG, "Play service available");
            return true;
        // Google Play services was not available for some reason
        } else {
            // Display an error dialog
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0);
            if (dialog != null) {
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(dialog);
                errorFragment.show(getFragmentManager(), "LocationSample");
            }
            return false;
        }
    }
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(
                        this, 9000);

            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            showErrorDialog(connectionResult.getErrorCode());
        }	
	}


	@Override
	public void onConnected(Bundle arg0) {
		mConnectionStatus.setText("connected");
		
	}

    private void showErrorDialog(int errorCode) {

        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
            errorCode, this, 9000);

        if (errorDialog != null) {

            ErrorDialogFragment errorFragment = new ErrorDialogFragment();

            errorFragment.setDialog(errorDialog);

            errorFragment.show(getFragmentManager(), "LocationSample");
        }
    }


    public static class ErrorDialogFragment extends DialogFragment {

        private Dialog mDialog;

        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }


	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}
	
}
