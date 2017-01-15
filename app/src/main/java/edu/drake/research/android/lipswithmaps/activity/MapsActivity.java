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

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
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

    //Views
    private BottomSheetBehavior mBottomSheetBehavior; //TODO: use this for the WifiList

    //Wifi list
    private List<WifiItem> mWifiList;
    private WifiAdapter mWifiAdapter;

    //Map variables
    private GoogleMap mMap;
    private static int MY_LOCATION_REQUEST_CODE = 100;

    //Wifi variables
    private List<ScanResult> mWifiScanResults;
    private WifiManager mWifiManager;
    final BroadcastReceiver mWifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mWifiScanResults = mWifiManager.getScanResults();

            for (ScanResult result: mWifiScanResults){
                mWifiList.add(new WifiItem(result.SSID, result.level));
            }

            mWifiAdapter.notifyDataSetChanged();

        }

    };

    @Override
    protected void onStop() {
        super.onStop();
        try {
            unregisterReceiver(mWifiScanReceiver);
        } catch (java.lang.IllegalArgumentException e){
            Log.e(TAG, "onStop: ", e);
            e.printStackTrace();
        }

    }

    private void setupToolbar(){
        //shows Action bar
        setSupportActionBar(mToolbar);
    }


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
    }

    private void enableCurrentLocationUI(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }
    }

    private void getCurrentLongLat(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        android.location.LocationListener locationListener = new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mCurrentLocationTextView.setText("Longitude: " + location.getLongitude() + "\t\tLatitude: " + location.getLatitude());
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

        //TODO: Check permssion
        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            } else {
                // Permission was denied. Display an error message.
            }
        }
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
        // Add a marker in Sydney, Australia, and move the camera.
        mMap.addMarker(new MarkerOptions().position(DRAKE_UNIVERSITY).title("Drake University! Yay!"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(drakeUniversity));
        setCameraPosition(DRAKE_UNIVERSITY);
        enableCurrentLocationUI();
        getCurrentLongLat();
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
