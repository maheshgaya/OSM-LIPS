package edu.drake.research.android.openlips;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

/**
 * edu.drake.research.android.openlips
 * OpenLIPS
 * Created by Mahesh Gaya on 8/14/16.
 */
public class WifiScanner extends BaseActivity
        implements View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener{
    //Logging members
    protected final String TAG = this.getClass().getSimpleName();

    //Wifi Manager members
    protected WifiManager mWifiManager;
    protected List<ScanResult> mWifiScanList;
    protected String[] mWifiList;
    protected int mWifiState;
    protected WifiInfo mWifiInfo;
    protected WifiScanReceiver mWifiReceiver;

    //Permission members
    protected static final int PERMISSION_REQUEST_CODE = 2;
    protected static final String[] PERMISSION_ARRAY = new String[]{
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE
    };

    //Layout members
    private ListView mWifiListView;
    private TextView mLastUpdatedText;
    private String mWifiStateLabel;
    private String mWifiInfoLabel;
    private String mLastUpdatedLabel;
    private View mView;
    private Context mContext;
    private Activity mActivity;
    private CoordinatorLayout mCoordinatorLayout;

    protected String mLastUpdatedTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: method executed");
        //setTheme(R.style.WifiScannerActivityTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_scanner);

        //Navigation Drawer
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Wifi Scanner
        mWifiListView = (ListView)findViewById(R.id.listview_wifi_scan_results);
        mLastUpdatedText = (TextView)findViewById(R.id.txt_last_updated_time_wifi);

        Log.d(TAG, "onCreate: Get mCoordinatorLayout");
        mCoordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinatorLayoutWifi);
        

        mWifiInfoLabel = getResources().getString(R.string.wifi_info_label);
        mWifiStateLabel = getResources().getString(R.string.wifi_state_label);
        mLastUpdatedLabel = getResources().getString(R.string.last_updated_time_label);


        mContext = getApplicationContext();
        mActivity = this;

        mWifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        mWifiReceiver = new WifiScanReceiver();
        mWifiManager.startScan();


    }

    //Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: method executed");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.wifi_scanner, menu);
        return true;
    }

    //Toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: method executed");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Log.d(TAG, "onOptionsItemSelected: action_settings selected");
            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
        } else if (id == R.id.action_select_wifi){
            Log.d(TAG, "onOptionsItemSelected: action_select_wifi selected");
            return true;
        } else if (id == R.id.action_refresh_wifi){
            Log.d(TAG, "onOptionsItemSelected: action_refresh");
            Snackbar.make(mCoordinatorLayout,"Getting Wifi updates...", Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE).show();
            mWifiManager.startScan();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: method executed");
        registerReceiver(mWifiReceiver, new IntentFilter(mWifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        mWifiManager.startScan();
        super.onResume();

    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: method executed");
        unregisterReceiver(mWifiReceiver);
        super.onPause();
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

        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.ACCESS_WIFI_STATE)
                && ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.CHANGE_WIFI_STATE)) {
            if (mView != null) {
                Snackbar.make(mView, "This Wifi Scanner needs some permissions. Please set them on in your settings.", Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE).show();
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
                        Snackbar.make(mView, "Permission Granted, Now you can access wifi data.", Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE).show();

                    }
                }
                break;
        }

    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: method executed");
        mView = v;
    }

    public String wifiStateString(int wifiStateInt){

        Log.d(TAG, "wifiStateString: method executed");
        switch (wifiStateInt){
            case 0:
                return "WIFI_STATE_DISABLING";
            case 1:
                return "WIFI_STATE_DISABLED";
            case 2:
                return "WIFI_STATE_ENABLING";
            case 3:
                return "WIFI_STATE_ENABLED";
            case 4:
                return "WIFI_STATE_UNKNOWN";
        }
        return "An error occurred";
    }


    private class WifiScanReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //Toast.makeText(getApplicationContext(), "Getting new data...",Toast.LENGTH_LONG).show();
            mLastUpdatedTime = java.text.DateFormat.getTimeInstance().format(new Date());
            mLastUpdatedText.setText(String.format("%s: %s", mLastUpdatedLabel,mLastUpdatedTime));
            Log.d(TAG, "WifiScanReceiver.onReceive: method executed");
            mWifiState = mWifiManager.getWifiState();
            mWifiInfo = mWifiManager.getConnectionInfo();
            mWifiScanList = mWifiManager.getScanResults();
            mWifiList = new String[mWifiScanList.size()];

            int j;
            for (int i = 0; i < mWifiScanList.size(); i++){
                //mWifiList[i] = (mWifiScanList.get(i).toString());
                j = i + 1;
                mWifiList[i] = ("#" + j + ":\t" +
                        "SSID: " + (mWifiScanList.get(i).SSID).toString() + "\n" +
                        "BSSID: " + (mWifiScanList.get(i).BSSID).toString() + "\n" +
                        "Capabilities: " + (mWifiScanList.get(i).capabilities).toString() + "\n" +
                        "Frequency: " + (mWifiScanList.get(i).frequency) + "\n" +
                        "Level: " + (mWifiScanList.get(i).level) + "\n"
                        //+ "Timestamp: " + (wifiScanList.get(i).timestamp) + " hours\n"
                );
            }

            if (mWifiList != null) {
                mWifiListView.setAdapter(new ArrayAdapter<String>(mContext, R.layout.custom_text_view, mWifiList));
            }
        }
    }
}