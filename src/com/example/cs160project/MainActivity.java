package com.example.cs160project;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

	private GoogleMap googleMap;
	// define the display assembly compass picture
	private ImageView image;

	// record the compass picture angle turned
	private float currentDegree = 0f;

	// device sensor manager
	private SensorManager mSensorManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try {
			// Loading map
			initilizeMap();
			image = (ImageView) findViewById(R.id.compassIcon);
			mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

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
				double latitude = 17.385044;
				double longitude = 78.486671;

				// create marker
				MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("Hello Maps ");

				// adding marker
				googleMap.addMarker(marker);
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		initilizeMap();
		mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_GAME);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		// get the angle around the z-axis rotated
		float degree = Math.round(event.values[0]);

		// create a rotation animation (reverse turn degree degrees)
		RotateAnimation ra = new RotateAnimation(
				currentDegree, 
				-degree,
				Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF,
				0.5f);

		// how long the animation will take place
		ra.setDuration(210);

		// set the animation after the end of the reservation status
		ra.setFillAfter(true);

		// Start the animation
		image.startAnimation(ra);
		currentDegree = -degree;
	}
	

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	@Override
	public void onPause() {
		super.onPause();

		// to stop the listener and save battery
		mSensorManager.unregisterListener(this);
	}

	public class Compass extends Activity implements SensorEventListener {

		// define the display assembly compass picture
		private ImageView image;

		// record the compass picture angle turned
		private float currentDegree = 0f;

		// device sensor manager
		private SensorManager mSensorManager;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			//setContentView(R.layout.compass);
			image = (ImageView) findViewById(R.id.compassIcon);


			// initialize your android device sensor capabilities
			mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		}

		/* @Override
	    public void onCreateView(View v){

	    }*/

		@Override
		public void onResume() {
			super.onResume();

			// for the system's orientation sensor registered listeners
			mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
					SensorManager.SENSOR_DELAY_GAME);
		}

		@Override
		public void onPause() {
			super.onPause();

			// to stop the listener and save battery
			mSensorManager.unregisterListener(this);
		}

		@Override
		public void onSensorChanged(SensorEvent event) {

			// get the angle around the z-axis rotated
			float degree = Math.round(event.values[0]);

			// create a rotation animation (reverse turn degree degrees)
			RotateAnimation ra = new RotateAnimation(
					currentDegree, 
					-degree,
					Animation.RELATIVE_TO_SELF, 0.5f, 
					Animation.RELATIVE_TO_SELF,
					0.5f);

			// how long the animation will take place
			ra.setDuration(210);

			// set the animation after the end of the reservation status
			ra.setFillAfter(true);

			// Start the animation
			image.startAnimation(ra);
			currentDegree = -degree;

		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}
	}


}
