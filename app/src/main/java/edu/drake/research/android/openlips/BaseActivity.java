package edu.drake.research.android.openlips;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;

/**
 * edu.drake.research.android.openlips
 * OpenLIPS
 * Created by Mahesh Gaya on 8/15/16.
 */
public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //logging members
    private final String TAG = this.getClass().getSimpleName();

    //Action Bar
    protected MenuItem styleItem;
    protected Spinner spinner;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: method executed");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: method executed");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        // Configure the search info and add any event listeners...

        styleItem = menu.findItem(R.id.spinner);
        spinner = (Spinner) MenuItemCompat.getActionView(styleItem);

        return true;
    }

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
        } else if (id == R.id.action_search){
            Log.d(TAG, "onOptionsItemSelected: action_search selected");
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Log.d(TAG, "onNavigationItemSelected: method executed");
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_map) {
            Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
            Log.d(TAG, "onNavigationItemSelected: mainIntent created");
            startActivity(mainIntent);
            Log.d(TAG, "onNavigationItemSelected: mainIntent started");

        } else if (id == R.id.nav_location_sensor) {
            Intent locationIntent = new Intent(getApplicationContext(), GetLocation.class);
            startActivity(locationIntent);
        } else if (id == R.id.nav_wifi_scanner) {
            Intent wifiScannerIntent = new Intent(getApplicationContext(), WifiScanner.class);
            startActivity(wifiScannerIntent);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        try {
            drawer.closeDrawer(GravityCompat.START);
        } catch (Exception e){
            Log.e(TAG, "onNavigationItemSelected: ", e);
        }
        return true;
    }
}
