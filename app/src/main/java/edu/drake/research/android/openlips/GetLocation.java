package edu.drake.research.android.openlips;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.Date;

/**
 * Created by Mahesh Gaya on 8/3/16.
 */
public class GetLocation extends BaseActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener,
        ResultCallback<LocationSettingsResult>,
        LocationListener,
        NavigationView.OnNavigationItemSelectedListener{

    // Keys for storing activity state in the Bundle.
    protected final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    protected final static String KEY_LOCATION = "location";
    protected final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";


    //members for location services
    protected GoogleApiClient mGoogleApiClient;

    //protected Location mLastLocation;
    protected LocationRequest mLocationRequest;
    private AddressResultReceiver mAddressResultReceiver;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    protected static final int UPDATE_INTERVAL_IN_MILLISECONDS = 30000;
    protected static final int FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    protected LocationSettingsRequest mLocationSettingsRequest;
    protected String mLastUpdatedTime;
    protected String mAddressOutput;

    //layout members
    private TextView mLatitudeText;
    private TextView mLongitudeText;
    private TextView mLastUpdatedTimeText;
    private TextView mAddressText;

    private String mLongitudeLabel;
    private String mLatitudeLabel;
    private String mLastUpdatedTimeLabel;
    private String mAddressLabel;

    //permission members
    protected static final int PERMISSION_REQUEST_CODE = 1;
    protected static final String[] PERMISSION_ARRAY = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    //logging members
    private final String TAG = this.getClass().getSimpleName();

    //misc members
    private Context mContext;
    private Activity mActivity;
    private View mView;
    private CoordinatorLayout mCoordinatorLayout;
    private Location mCurrentLocation;
    private boolean mRequestingLocationUpdates;


    /*
    * Set the view for location services activity
    * Makes connection for FusedLocationProviderApi
    * Makes initial call for getting location (Gets the last known location)
    * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.GetLocationActivityTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_services);

        //Get Navigation Drawer
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //connect layout with this class
        Log.d(TAG, "onCreate: initialization");
        mLongitudeText = (TextView)findViewById(R.id.txt_longitude);
        mLatitudeText = (TextView)findViewById(R.id.txt_latitude);
        mLastUpdatedTimeText = (TextView)findViewById(R.id.txt_last_updated_time);
        mAddressText = (TextView)findViewById(R.id.txt_geoaddress);
        mCoordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinatorLayoutLocation);


        //set Labels
        mLongitudeLabel = getResources().getString(R.string.longitude_label);
        mLatitudeLabel = getResources().getString(R.string.latitude_label);
        mLastUpdatedTimeLabel = getResources().getString(R.string.last_updated_time_label);
        mAddressLabel = getResources().getString(R.string.geoaddress_label);

        mRequestingLocationUpdates = false;
        // Update values using data stored in the Bundle.
        updateValuesFromBundle(savedInstanceState);

        //assigning misc members
        mContext = getApplicationContext();
        mActivity = this;
        Log.d(TAG, "onCreate: GoogleApiClient Instance");
        buildGoogleApiClient();
        createLocationRequest();
        buildLocationSettingsRequest();
    }

    //Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: method executed");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.location, menu);
        return true;
    }

    //Toolbar
    /*
     * Handles the buttons on the Action Bar
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: method executed");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //opens setting activity
            Log.d(TAG, "onOptionsItemSelected: action_settings selected");
            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
        } else if (id == R.id.action_select_wifi){
            //opens wifi selection activity (context on Wifi Activity)
            Log.d(TAG, "onOptionsItemSelected: action_select_wifi selected");
            return true;
        } else if (id == R.id.action_refresh_location){
            //gets updates on current location
            Log.d(TAG, "onOptionsItemSelected: action_refresh_location tapped");
            if (checkPermission()){
                Log.d(TAG, "onClick: case R.id.btn_get_location: checkPermission() is True");
                mRequestingLocationUpdates = true;
                startLocationUpdates();
                Snackbar.make(mCoordinatorLayout,"Permission already granted. Getting Location updates...", Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE).show();
                if (mGoogleApiClient.isConnected() && mCurrentLocation != null){
                    startIntentService();
                }
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mGoogleApiClient.isConnected()) {
                            stopLocationUpdates();
                        }
                    }
                }, UPDATE_INTERVAL_IN_MILLISECONDS);

            } else {
                Log.d(TAG, "onClick: case R.id.btn_get_location: checkPermission() is False");
                requestPermission();
            }
        
        }

        return super.onOptionsItemSelected(item);
    }



    /*
    * Gets updated values from Bundle
    * Saved values in bundle if the phone is rotated
    * */
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null){
            if (savedInstanceState.keySet().contains(KEY_REQUESTING_LOCATION_UPDATES)){
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        KEY_REQUESTING_LOCATION_UPDATES
                );
            }
            if (savedInstanceState.keySet().contains(KEY_LOCATION)){
                mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            }
            if(savedInstanceState.keySet().contains(KEY_LAST_UPDATED_TIME_STRING)){
                mLastUpdatedTime = savedInstanceState.getString(KEY_LAST_UPDATED_TIME_STRING);
            }
            displayLocationUI();
        }
    }

    /*
    * Stops location updates
    * */
    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: method executed");
        super.onPause();
        stopLocationUpdates();

    }

    /*
     * Stops location updates
     * */
    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    /*
    * If not already connected, app connects to Google Api Client to get location updates
    * */
    @Override
    protected void onStart() {
        Log.d(TAG, "onStart: method executed");

        if (!(mGoogleApiClient.isConnected())){
            mGoogleApiClient.connect();
        }
        super.onStart();
    }

    /*
    * Gets Location updates if connected and if RequestingLocationUpdates is True
    * */
    @Override
    protected void onResume(){
        super.onResume();
        if (mGoogleApiClient.isConnected() && !mRequestingLocationUpdates){
            startLocationUpdates();
        }

    }

    /*
    * Disconnects Google Api Client
    * */
    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: method executed");
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    /*
    * Fetches location updates as intent in the background
    * */
    protected void startIntentService(){
        Intent intent = new Intent(GetLocation.this, FetchAddressIntentService.class);
        mAddressResultReceiver = new AddressResultReceiver(new Handler());
        intent.putExtra(Constants.RECEIVER, mAddressResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mCurrentLocation);
        startService(intent);
    }

    /*
    * Gets the last known location
    * if permission is granted
    * otherwise it requests for permission
    * Then if answer is not null, it updates the UI
    * */
    public void getLastKnownLocation(){
        if (checkPermission()) {
            Log.d(TAG, "onConnected: checkPermission() is True");
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdatedTime = java.text.DateFormat.getTimeInstance().format(new Date());
        }else{
            Log.d(TAG, "onConnected: checkPermission() is False");
            requestPermission();
        }
        if (mCurrentLocation != null){
            Log.d(TAG, "onConnected: mCurrentLocation is not NULL");
            displayLocationUI();
        }
    }

    /*
    * Creates a GoogleApiClient instance
    * */
    protected synchronized void buildGoogleApiClient(){
        Log.d(TAG, "buildGoogleApiClient: method executed");
        //create a GoogleApiClient Instance if it is null
        if (mGoogleApiClient == null){
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    /*
    * creates a builder for Location Settings Request
    * */
    protected void buildLocationSettingsRequest(){
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest
                .Builder()
                .addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    /*
    * Check if location is permitted in System Settings
    * Useful for API 21 and above
    * */
    protected void checkLocationSettings(){
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        mLocationSettingsRequest);
        result.setResultCallback(this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected: method executed");
        //when ApiClient is connect, get last known location
        getLastKnownLocation();
        if (mCurrentLocation != null) {
            // Determine whether a Geocoder is available.
            if (!Geocoder.isPresent()) {
                Toast.makeText(this, R.string.no_geocoder_available,
                        Toast.LENGTH_LONG).show();
                return;
            }
            startIntentService();
        }

    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()){
            case LocationSettingsStatusCodes.SUCCESS:
                Log.d(TAG, "onResult: location settings satisfied");
                startLocationUpdates();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //Location settings required, show dialog box
                try{
                    status.startResolutionForResult(
                            GetLocation.this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e){
                    Log.d(TAG, "onResult: do nothing");
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                //no way to fix it because settings is unavailable
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode){
                    case Activity.RESULT_OK:
                        Log.d(TAG, "onActivityResult: User made changes in settings");
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.d(TAG, "onActivityResult: User did not make changes in settings");
                        break;

                }
                break;
        }
    }

    private void startLocationUpdates() {
        if (checkPermission()) {
            Log.d(TAG, "startLocationUpdates: checkPermission is True");
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient,
                    mLocationRequest,
                    this
            ).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    mRequestingLocationUpdates = true;

                }
            });
        }else{
            Log.d(TAG, "startLocationUpdates: checkPermission() is False");
            requestPermission();
        }
        if (mCurrentLocation != null){
            Log.d(TAG, "startLocationUpdates: mCurrentLocation is not NULL");
            displayLocationUI();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended: method executed");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: method executed");
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: method executed");
        mView = v;
        int id = v.getId();


    }

    private boolean checkPermission(){
        Log.d(TAG, "checkPermission: method executed");
        int result_access_coarse_location = ContextCompat
                .checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION);
        int result_access_fine_location = ContextCompat
                .checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION);

        if (result_access_coarse_location == PackageManager.PERMISSION_GRANTED
                && result_access_fine_location == PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "checkPermission: Permission Granted");
            return true;
        }
        else {
            Log.d(TAG, "checkPermission: Permission Denied");
            return false;
        }
    }

    private void requestPermission(){
        Log.d(TAG, "requestPermission: method executed");

        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION)
                && ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            if (mView != null) {
                Snackbar.make(mView, "This Sensor app needs some permissions. Please set them on in your settings.", Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE).show();
            }

        } else {
            ActivityCompat.requestPermissions(mActivity, PERMISSION_ARRAY, PERMISSION_REQUEST_CODE);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String Permissions[], int[] grantResults){
        Log.d(TAG, "onRequestPermissionsResult: method executed");
        switch (requestCode){
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mView != null) {
                        Snackbar.make(mView, "Permission Granted, Now you can access location data.", Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE).show();
                    }
                }
                break;
        }

    }

    @SuppressLint("DefaultLocale")
    public void displayLocationUI(){
        Log.d(TAG, "displayLocationUI: method executed");
        if (mCurrentLocation != null) {
            mLongitudeText.setText(String.format("%s: %f ", mLongitudeLabel, mCurrentLocation.getLongitude()));
            mLatitudeText.setText((String.format("%s: %f ", mLatitudeLabel, mCurrentLocation.getLatitude())));
            mLastUpdatedTimeText.setText((String.format("%s: %s ", mLastUpdatedTimeLabel, mLastUpdatedTime)));
            mAddressText.setText((String.format("%s: \n%s ", mAddressLabel, mAddressOutput)));
        }
    }
    protected void createLocationRequest(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdatedTime = java.text.DateFormat.getTimeInstance().format(new Date());
        displayLocationUI();
    }
    public void onSavedInstanceState(Bundle savedInstanceState){
        savedInstanceState.putBoolean(KEY_REQUESTING_LOCATION_UPDATES,
                mRequestingLocationUpdates);
        savedInstanceState.putParcelable(KEY_LOCATION, mCurrentLocation);
        savedInstanceState.putString(KEY_LAST_UPDATED_TIME_STRING, mLastUpdatedTime);
        super.onSaveInstanceState(savedInstanceState);

    }

    @SuppressLint("ParcelCreator")
    class AddressResultReceiver extends ResultReceiver{
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData){
            mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            displayLocationUI();
        }
    }

}
