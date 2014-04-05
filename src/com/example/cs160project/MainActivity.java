package com.example.cs160project;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private GoogleMap googleMap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try {
            // Loading map
            initilizeMap();
 
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	@SuppressLint("NewApi")
	private void initilizeMap() {
        if (googleMap == null) {
        	googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();
 
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }else{
            	// latitude and longitude
            	double latitude = 37.8717;
            	double longitude = 122.2728;
            	 
            	// create marker
            	Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Hello Maps "));
            	 
            	// adding marker
            	//googleMap.addMarker(marker);
            	
            	googleMap.setOnMarkerClickListener(new OnMarkerClickListener(){

					@Override
					public boolean onMarkerClick(Marker arg0) {
						arg0.showInfoWindow();
						Intent intent= new Intent(MainActivity.this, PlayBackActivity.class);
                        startActivity(intent);
						return false;
					}
            		
            	});
            }
        }
    }

	@Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }

}
