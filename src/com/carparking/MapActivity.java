package com.carparking;

import java.util.ArrayList;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;


import com.carparking.util.AppConstants;
import com.carparking.util.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapActivity extends FragmentActivity implements GoogleMap.OnInfoWindowClickListener {

	private GoogleMap googleMap;
	private Location loc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		 loc = getIntent().getExtras().getParcelable("location");

		try {
			initilizeMap();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
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
	
	private void initilizeMap() {
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();
			googleMap.setOnInfoWindowClickListener(this);
			
			new GetPlaces(getApplicationContext(), this, AppConstants.PLACE_TO_LOOK, loc).execute();

		}
	}
	
	public void populateMap (ArrayList<Place> result) {
		// add current location to map
		MarkerOptions markerCurrent = new MarkerOptions().position(
				new LatLng(loc.getLatitude(),loc.getLongitude() ))
				.title("Current Location");

		markerCurrent.icon(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
		googleMap.addMarker(markerCurrent);
		
		
		for (int i = 0; i < result.size(); i++) {
			MarkerOptions marker = new MarkerOptions().position(
					new LatLng(result.get(i).getLatitude(),result.get(i).getLongitude()))
					.title(result.get(i).getName())
					.snippet(result.get(i).getVicinity());

			
			marker.icon(BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_RED));
			googleMap.addMarker(marker);

		}
		
		CameraPosition cameraPosition = new CameraPosition.Builder()
		.target(new LatLng(loc.getLatitude(),
				loc.getLongitude())).zoom(12).build();

		googleMap.animateCamera(CameraUpdateFactory
				.newCameraPosition(cameraPosition));
		

	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		LatLng pos = marker.getPosition();
		
		Intent intent = new Intent(Intent.ACTION_VIEW, 
				Uri.parse("geo:"+ pos.latitude +  "," +pos.longitude + 
						"?q="+pos.latitude +  "," + pos.longitude + "(" + marker.getTitle() + ")" ));
		startActivity(intent);
		
	}

}
