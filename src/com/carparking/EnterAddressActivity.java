package com.carparking;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class EnterAddressActivity extends Activity{

	private static final String TAG = "EnterAddressActivity";
	EditText street, city,state,zip;
	ConnectionDetector cd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.street_address);

		street = (EditText) findViewById(R.id.editText_street);
		city = (EditText) findViewById(R.id.editText_city);
		state = (EditText) findViewById(R.id.editText_state);
		zip = (EditText) findViewById(R.id.editText_zip);
		cd = new ConnectionDetector(getApplicationContext());

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

	public void onFindParkingClick (View v) {
		if (!cd.isConnectingToInternet()) {
			new DialogManager("Please check wifi/data network settings", 
					"No Network Connection", this).show(getFragmentManager(), "NetworkMissing");
		} else {
			StringBuilder add = new StringBuilder();
			add.append(street.getText().toString() + " ");
			add.append(city.getText().toString() + " ");
			add.append(state.getText().toString() + " ");
			add.append(zip.getText().toString());

			Geocoder coder = new Geocoder(this);
			List<Address> address;

			try {
				address = coder.getFromLocationName(add.toString(),5);
				if (address == null || address.size() == 0) {
					new DialogManager("Please enter data in at least one field", 
							"Details Missing", this).show(getFragmentManager(), "MissingAddress");
				}
				Address location = address.get(0);
				//Log.e(TAG, "2. " + location.getAddressLine(0) + " " + location.getLatitude() + "," +location.getLongitude());

				Location loc = new Location("Location");
				loc.setLatitude(location.getLatitude());
				loc.setLongitude(location.getLongitude());
				startActivity(new Intent(this, MapActivity.class).putExtra("location", loc));

			} catch (Exception e) {

			}
		}
	}

}
