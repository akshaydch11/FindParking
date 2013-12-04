package com.carparking;

import java.util.ArrayList;

import com.carparking.util.AppConstants;
import com.carparking.util.Place;
import com.carparking.util.PlacesService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

public class GetPlaces extends AsyncTask<Void, Void, ArrayList<Place>> {

	private Context context;
	private String places;
	private Location loc;
	private MapActivity activity;

	public GetPlaces(Context context,MapActivity act, String places, Location location) {
		this.context = context;
		this.activity = act;
		this.places = places;
		this.loc = location;
		
	}

	@Override
	protected void onPostExecute(ArrayList<Place> result) {
		super.onPostExecute(result);
		activity.populateMap(result);
		
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected ArrayList<Place> doInBackground(Void... arg0) {
		PlacesService service = new PlacesService(AppConstants.BROWSER_API_KEY);
		ArrayList<Place> findPlaces = service.findPlaces(loc.getLatitude(), 
				loc.getLongitude(), places); 
		return findPlaces;
	}

}
