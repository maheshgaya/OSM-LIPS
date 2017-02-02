/*
 * Copyright 2017 Drake University
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
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.drake.research.android.lipswithmaps.R;
import edu.drake.research.android.lipswithmaps.adapter.WifiAdapter;
import edu.drake.research.android.lipswithmaps.data.WifiItem;
import edu.drake.research.android.lipswithmaps.helper.Utils;

import static junit.framework.Assert.assertNotNull;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback{
    //Logging
    private final LatLng DRAKE_UNIVERSITY = new LatLng(41.6013800, -93.6518900);
    private static final String TAG = MapsActivity.class.getSimpleName();

    //Binding views
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.recycleview_wifi) RecyclerView mWifiRecycleView;
    @BindView(R.id.framelayout_wifilist)View mWifiListFrameLayout;
    @BindView(R.id.textview_current_location)TextView mCurrentLocationTextView;
    @BindView(R.id.main_coordinator_layout)CoordinatorLayout mCoordinatorLayout;

    //Views
    private BottomSheetBehavior mBottomSheetBehavior; //TODO: use this for the WifiList

    //Wifi list
    private List<WifiItem> mWifiList;
    private WifiAdapter mWifiAdapter;

    //Map variables
    private GoogleMap mMap;

    //Permissions
    private static int LOCATION_REQUEST_CODE = 100;
    String locationPermission = Manifest.permission.ACCESS_FINE_LOCATION;

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

    //Location variables
    LocationManager mLocationManager; //get the location of the user with accuracy
    Location mLocation; //will contain the longitude and latitude obtained
    android.location.LocationListener mLocationListener = new android.location.LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //TODO: Save this data
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

    @Override
    @SuppressWarnings({"MissingPermission"})
    protected void onStop() {
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

        super.onStop();
    }

    private void setupToolbar(){
        //shows Action bar
        setSupportActionBar(mToolbar);
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
        mWifiList = new ArrayList<>();
        mWifiAdapter = new WifiAdapter(mWifiList);

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
    }

    private void enableCurrentLocationUI(){
        if (ContextCompat.checkSelfPermission(this, locationPermission)
                == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }

        }
    }


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

    private void askPermission(){
        if (ContextCompat.checkSelfPermission(this, locationPermission)
                == PackageManager.PERMISSION_GRANTED) {
            initializeSensors();
        } else {
            showRationale();
        }
    }
    private void initializeSensors(){
        getCurrentLongLat();
        wifiScan();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    permissions[0].equals(locationPermission) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeSensors();
            } else {
                //Permission was denied. Display an error message.
                showRationale();

            }
        }
    }

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

    private void wifiScan(){
        mWifiManager= (WifiManager) getSystemService(Context.WIFI_SERVICE);
        registerReceiver(mWifiScanReceiver,
                new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        mWifiManager.startScan();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions().position(DRAKE_UNIVERSITY).title("Drake University! Yay!"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(drakeUniversity));
        setCameraPosition(DRAKE_UNIVERSITY);
        enableCurrentLocationUI();
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
}
