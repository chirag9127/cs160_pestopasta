package com.pestopasta.cluzcs160;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.util.HashMap;

public class MainActivity extends Activity implements LocationListener, GoogleMap.OnMapClickListener {

	Button addTag;
	//private LocationManager locationManager;
	//private LocationListener locListener;
	private String provider;
    Handler h = new Handler();

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getMyLocation();
		setUpMapIfNeeded();
		
		//Hardcoding marker
		/*MarkerOptions marker = new MarkerOptions().position(new LatLng(37.871993, -122.257862))
				.title("New Clue");
		Marker m = myMap.addMarker(marker);*/
		//count = 0;
		if(count == 0) {
		db.put(count, new AudioFile("Campanille Tour", 37.871993, -122.257862,""+count, new File(Environment.getExternalStorageDirectory(), "test3.pcm")));
		count += 1;
		db.put(count, new AudioFile("Lost Watch", 37.871934, -122.258120,""+count, new File(Environment.getExternalStorageDirectory(), "test2.pcm")));
		count += 1;
		db.put(count, new AudioFile("Geocaching Clue?", 37.873012, -122.258785,""+count, new File(Environment.getExternalStorageDirectory(), "test1.pcm")));
		count += 1;
		}
		addMarkers();
		
		
		addTag = (Button)findViewById(R.id.button1);
		
		addTag.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, AddTagActivity.class);
				System.out.println("YAYAYAYAYYAYAYYAOOOOOO:" + myLat);
				intent.putExtra("lat", String.valueOf(myLat));
				intent.putExtra("long", String.valueOf(myLong));
				startActivity(intent);

			}
		});
		
		myMap.setOnMarkerClickListener(new OnMarkerClickListener(){

			@Override
			public boolean onMarkerClick(Marker arg0) {
				//arg0.showInfoWindow();
				
				int i = mapper.get(arg0.getSnippet());
				AudioFile af = db.get(i);
				currAf = af;
				System.out.println("CLICK CLICK CLICK CLICK " + af.title);
				Intent intent= new Intent(MainActivity.this, PlayBackActivity.class);
                startActivity(intent);
				return false;
			}
            	});

        SystemBarTintManager tintManager = new SystemBarTintManager(com.pestopasta.cluzcs160.MainActivity.this);
        tintManager.setStatusBarTintEnabled(true);
        int actionBarColor = Color.parseColor("#BBffffff");
        tintManager.setStatusBarTintColor(actionBarColor);

        Thread t = new Thread(animateActionBarHide);
        t.start();
	}

    @Override
    protected void onResume() {
        super.onResume();
        Thread t = new Thread(animateActionBarHide);
        t.start();
    }
	
	/*@Override
	protected void onResume() {
		super.onResume();
		getMyLocation();
		setUpMapIfNeeded();
		
		//Hardcoding marker
		MarkerOptions marker = new MarkerOptions().position(new LatLng(37.871993, -122.257862))
				.title("New Clue").snippet("Take the elevator!");
		myMap.addMarker(marker);
		
		
		addTag = (Button)findViewById(R.id.button1);
		
		addTag.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, AddTagActivity.class);
				System.out.println("YAYAYAYAYYAYAYYAOOOOOO:" + myLat);
				intent.putExtra("lat", String.valueOf(myLat));
				intent.putExtra("long", String.valueOf(myLong));
				startActivity(intent);

			}
		});
	}*/
	/*@Override
	 protected void onResume() {
	super.onResume();
	locationManager.requestLocationUpdates(provider, 400, 1, this);
	
	 }*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	      case R.id.compass:
	    	  Intent intent = new Intent(this,Compass.class);
	  		  startActivity(intent);
	    	  return true;
	      default:
	            return super.onOptionsItemSelected(item);
	      
	    
	    }
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		//Toast.makeText(this, (myLat + "," + myLong), Toast.LENGTH_LONG);
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
	
	public void getMyLocation() {
		MyLocation location = new MyLocation();
		MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {

			@Override
			public void gotLocation(Location loc) {
				currentLocation = loc;
				if (loc != null) {
					myLat = loc.getLatitude();
					myLong = loc.getLongitude();
					latlonglocation = new LatLng(myLat, myLong);
					cameraPosition = new CameraPosition(latlonglocation, 18, 0,
							0);
					updateMap();

				}
			};
		}; // ends LocationResult
		location.getLocation(this, locationResult);

	}// ends getMyLocation method

	private void setUpMapIfNeeded() {
		if (myMap == null) {
			myMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();
			myMap.setMyLocationEnabled(true);
            int actionBarHeight = 0;
            TypedValue tv = new TypedValue();
            if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
            {
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                SystemBarTintManager tintManager = new SystemBarTintManager(com.pestopasta.cluzcs160.MainActivity.this);
                int statusBarHeight = tintManager.getConfig().getStatusBarHeight();
                myMap.setPadding(0,statusBarHeight+actionBarHeight,0,0);
            } else {
                myMap.setPadding(0, actionBarHeight, 0, 0);
            }
            myMap.setOnMapClickListener(this);
			//myMap.setOnMarkerClickListener(this);
		}
	}

	private void updateMap() {
		CameraUpdate cameraUpdate = CameraUpdateFactory
				.newCameraPosition(cameraPosition);
		myMap.moveCamera(cameraUpdate);
	}
	
	public void addMarkers() {
		System.out.println("HHHHHHHHEEEEERRRRRRRRE" + count);
		for(int i = 0; i < count; i += 1) {
			if(db.containsKey(i)) {
				AudioFile temp = db.get(i);
				MarkerOptions marker = new MarkerOptions().position(new LatLng(temp.x, temp.y))
						.title(temp.title).snippet(temp.mySnip);
				Marker m = myMap.addMarker(marker);
				mapper.put(temp.mySnip, i);
			}
		}
	}

	
	public GoogleMap myMap;
	public double myLat;
	public double myLong;
	public LatLng latlonglocation;
	private CameraPosition cameraPosition;
	Location currentLocation;
	
	
	public static HashMap<Integer, AudioFile> db = new HashMap<Integer, AudioFile> ();
	public static HashMap<String, Integer> mapper = new HashMap<String, Integer> ();
	public static int count;
	public static AudioFile currAf;
	
	/*public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub
		System.out.println("Click");
		int i = mapper.get(marker.getSnippet());
		AudioFile af = db.get(i);
		Toast.makeText(this, af.mySnip, Toast.LENGTH_LONG);
		return false;
	}*/

    public void onMapClick(LatLng point) {
        Thread t = new Thread(animateActionBarShow);
        t.start();
    }

    Runnable hideActionbarRunnable = new Runnable() {
        @Override
        public void run() {
            ActionBar bar = getActionBar();
            if (bar != null) {

                bar.hide();
                setUpMapIfNeeded();
                /*
                // Only set the tint if the device is running KitKat or above
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    SystemBarTintManager tintManager = new SystemBarTintManager(com.pestopasta.cluzcs160.MainActivity.this);
                    tintManager.setStatusBarTintEnabled(true);
                    tintManager.setTintAlpha(0);
                    int id = getResources().getIdentifier("config_enableTranslucentDecor", "bool", "android");
                    if (id == 0) {
                        // not on KitKat
                    } else {
                        boolean enabled = getResources().getBoolean(id);
                        // enabled = are translucent bars supported on this device
                        if (enabled) {
                            Window w = getWindow();
                            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                        }
                    }
                }
                */
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    SystemBarTintManager tintManager = new SystemBarTintManager(com.pestopasta.cluzcs160.MainActivity.this);
                    int statusBarHeight = tintManager.getConfig().getStatusBarHeight();
                    myMap.setPadding(0, statusBarHeight, 0, 0);
                } else {
                    myMap.setPadding(0, 0, 0, 0);
                }
            }
        }
    };

    Runnable showActionbarRunnable = new Runnable() {
        @Override
        public void run() {
            ActionBar bar = getActionBar();
            if (bar != null) {
                bar.show();
                setUpMapIfNeeded();
                int actionBarHeight = 0;
                TypedValue tv = new TypedValue();
                if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                    actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
                }
                // Only set the tint if the device is running KitKat or above
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    SystemBarTintManager tintManager = new SystemBarTintManager(com.pestopasta.cluzcs160.MainActivity.this);
                    //tintManager.setStatusBarTintEnabled(true);
                    //int actionBarColor = Color.parseColor("#BBffffff");
                    //tintManager.setStatusBarTintColor(actionBarColor);
                    int statusBarHeight = tintManager.getConfig().getStatusBarHeight();
                    myMap.setPadding(0, statusBarHeight + actionBarHeight, 0, 0);
                } else {
                    myMap.setPadding(0, actionBarHeight, 0, 0);
                }
            }
        }
    };

    Runnable animateActionBarShow = new Runnable() {
        @Override
        public void run() {
            h.post(showActionbarRunnable);
            h.postDelayed(hideActionbarRunnable,8000);
        }
    };

    Runnable animateActionBarHide = new Runnable() {
        @Override
        public void run() {
            h.postDelayed(hideActionbarRunnable,8000);
        }
    };
}
