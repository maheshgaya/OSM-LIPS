/*
 * Copyright 2017 Mahesh Gaya
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package edu.drake.research.android.lipswithmaps.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.drake.research.android.lipswithmaps.R;
import edu.drake.research.android.lipswithmaps.adapter.WifiAdapter;
import edu.drake.research.android.lipswithmaps.data.WifiItem;
import edu.drake.research.android.lipswithmaps.helper.Utils;

import static junit.framework.Assert.assertNotNull;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, SensorEventListener{
    //region TAG
    //Logging
    private final LatLng DRAKE_UNIVERSITY = new LatLng(41.6013800, -93.6518900);
    private static final String TAG = MapsActivity.class.getSimpleName();
    //endregion

    //region constants
    private static final String locationPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    //Permissions
    private static int LOCATION_REQUEST_CODE = 100;
    //endregion

    //region views
    //Binding views
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.recycleview_wifi) RecyclerView mWifiRecycleView;
    @BindView(R.id.bottomsheet_wifilist)FrameLayout mWifiListBottomSheet;
    @BindView(R.id.textview_current_location)TextView mCurrentLocationTextView;
    @BindView(R.id.main_coordinator_layout)CoordinatorLayout mCoordinatorLayout;
    private BottomSheetBehavior mBottomSheetBehavior;
    //endregion

    //region sensors
    //Sensors - Accelerometer, magnetic
    private SensorManager mSensorManager;
    /* Measures the acceleration force in m/s2 that is applied to a device on all
    three physical axes (x, y, and z), including the force of gravity.*/
    private Sensor mAccelerometer;
    /* Measures the ambient geomagnetic field for all three physical axes (x, y, z) in μT. */
    private Sensor mMagnetometer;
    /* Measures the orientation of a device by providing the three elements of the device's rotation vector. */
    private Sensor mOrientationMeter;
    //endregion

    //region Map
    //Map variables
    private GoogleMap mMap;
    //endregion

    //region wifi
    //Wifi list
    private List<WifiItem> mWifiList;
    private WifiAdapter mWifiAdapter;
    //Wifi variables
    private List<ScanResult> mWifiScanResults;
    private WifiManager mWifiManager;
    final BroadcastReceiver mWifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mWifiList.clear();
            mWifiScanResults = mWifiManager.getScanResults();

            for (ScanResult result: mWifiScanResults){
                mWifiList.add(new WifiItem(result.SSID, result.BSSID, result.level));
            }

            mWifiAdapter.notifyDataSetChanged();

        }

    };
    //endregion

    //region location manager
    //Location variables
    LocationManager mLocationManager; //get the location of the user with accuracy
    Location mLocation; //will contain the longitude and latitude obtained
    android.location.LocationListener mLocationListener = new android.location.LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mLocation = location;
            mCurrentLocationTextView.setText("Time: " + Utils.getFriendlyDateTime(location.getTime()) +
                    "\nLongitude: " + location.getLongitude() +
                    "\t\tLatitude: " + location.getLatitude() +
                    "\nAccuracy: " + location.getAccuracy() +
                    "\t\tAltitude: " + location.getAltitude());
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };
    //endregion

    //region Lifecycle methods
    @Override
    @SuppressWarnings({"MissingPermission"})
    protected void onPause() {
        try {
            unregisterReceiver(mWifiScanReceiver);
        } catch (java.lang.IllegalArgumentException e){
            Log.e(TAG, "onStop: ", e);
            e.printStackTrace();
        }
        if (mLocationManager != null){
            //do not put permission here it will end up in an infinite loop
            mLocationManager.removeUpdates(mLocationListener);
        }

        if (mSensorManager != null){
            mSensorManager.unregisterListener(this);
        }
        super.onPause();
    }

    /**
     * Sets up the toolbar
     */
    private void setupToolbar(){
        //shows Action bar
        setSupportActionBar(mToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.pref_general.
        int id = item.getItemId();
        switch (id){
            case R.id.action_details: {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            }
            case R.id.action_upload:{
                Toast.makeText(getApplicationContext(), "Upload Content", Toast.LENGTH_SHORT).show();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Initializes the views and ask for permission
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        setupToolbar();
        initSensors();

        //Initialize the containers and wifi adapters
        mWifiList = new ArrayList<>();
        mWifiAdapter = new WifiAdapter(mWifiList);

        //Initialize the list view for the wifis
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mWifiRecycleView.setLayoutManager(mLayoutManager);
        mWifiRecycleView.setItemAnimator(new DefaultItemAnimator());
        mWifiRecycleView.setAdapter(mWifiAdapter);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Initialize the wifi Broadcaster
        wifiScan();
        askPermission();
        mBottomSheetBehavior = BottomSheetBehavior.from(mWifiListBottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

    }


    /**
     * Registers the listeners for the different sensors
     */
    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mOrientationMeter, SensorManager.SENSOR_DELAY_NORMAL);

    }
    //endregion

    //region Location methods
    /**
     * Gets the current location
     */
    private void getCurrentLongLat() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Check permission
        // Register the listener with the Location Manager to receive location updates
        if (ContextCompat.checkSelfPermission(this, locationPermission)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
        } else {
            askPermission();
        }

    }
    //endregion

    //region Permission methods
    /**
     * Based on Permission result, initialize the sensors or show rationale
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    permissions[0].equals(locationPermission) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializePermittedSensors();
            } else {
                //Permission was denied. Display an error message.
                showRationale();

            }
        }
    }

    /**
     * Shows rationale message
     * @return
     */
    private boolean showRationale(){
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                locationPermission)) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            Snackbar.make(mCoordinatorLayout, R.string.permission_location_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(MapsActivity.this,
                                    new String[]{locationPermission},
                                    LOCATION_REQUEST_CODE);
                        }
                    })
                    .show();

        } else {

            // No explanation needed, we can request the permission.
            try {
                ActivityCompat.requestPermissions(this,
                        new String[]{locationPermission},
                        LOCATION_REQUEST_CODE);
            } catch (Exception e){
                Log.e(TAG, "showRationale: ", e);
            }
        }
        return false;
    }

    /**
     * Ask for permission
     */
    private void askPermission(){
        if (ContextCompat.checkSelfPermission(this, locationPermission)
                == PackageManager.PERMISSION_GRANTED) {
            initializePermittedSensors();
        } else {
            showRationale();
        }
    }
    //endregion

    //region WI-FI methods
    /**
     * get wifi scan
     */
    private void wifiScan(){
        mWifiManager= (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        registerReceiver(mWifiScanReceiver,
                new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        mWifiManager.startScan();
    }
    //endregion

    //region map methods
    /**
     * When map is ready, initialize the Map UI
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        mMap.addMarker(new MarkerOptions().position(DRAKE_UNIVERSITY).title("Drake University! Yay!"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(drakeUniversity));
        setCameraPosition(DRAKE_UNIVERSITY);
        enableCurrentLocationUI();
    }

    /**
     * show current map UI
     */
    private void enableCurrentLocationUI(){
        if (ContextCompat.checkSelfPermission(this, locationPermission)
                == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }

        }
    }
    /**
     * Set the camera zoom
     * @param latitude
     * @param longitude
     */
    public void setCameraPosition(double latitude, double longitude){
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))      // Sets the center of the map
                .zoom(15)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    /**
     *
     * @param position
     */
    public void setCameraPosition(LatLng position){
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(position)      // Sets the center of the map
                .zoom(15)              // Sets the zoom
                .build();              // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
    //endregion

    //region Sensor methods
    /**
     * Initializes wifi and location sensors after permission is asked
     */
    private void initializePermittedSensors(){
        getCurrentLongLat();
        wifiScan();
    }

    /**
     * initialize the meter sensors and other
     */
    private void initSensors(){
        mSensorManager = (SensorManager)getApplicationContext().getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mOrientationMeter = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d(TAG, "onSensorChanged: " + event.values + " -- " + event.sensor);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //endregion
}
