package edu.drake.research.android.openlips;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.mapzen.android.graphics.MapFragment;
import com.mapzen.android.graphics.MapzenMap;
import com.mapzen.android.graphics.OnMapReadyCallback;
import com.mapzen.android.graphics.model.BubbleWrapStyle;
import com.mapzen.android.graphics.model.CinnabarStyle;
import com.mapzen.android.graphics.model.MapStyle;
import com.mapzen.android.graphics.model.Marker;
import com.mapzen.android.graphics.model.RefillStyle;
import com.mapzen.tangram.LngLat;
import com.mapzen.tangram.MapController;
import com.mapzen.tangram.MapView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements MapView.OnMapReadyCallback{


    //logging members
    private final String TAG = this.getClass().getSimpleName();

    //Map members
    private MapView view;
    private MapController map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: method executed");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with zooming on the dot with current location", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Our MapView is declared in the layout file.
        view = (MapView)findViewById(R.id.map);

        // Lifecycle events from the Activity must be forwarded to the MapView.
        view.onCreate(savedInstanceState);

        // This starts a background process to set up the map.
        view.getMapAsync(this, "cinnabar-style-more-labels.yaml");

    }

    @Override
    public void onMapReady(MapController mapController) {
        // We receive a MapController object in this callback when the map is ready for use.
        map = mapController;
        map.setZoom(15);
        map.setPosition(new LngLat(-93.6518857, 41.601378));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: method executed");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Log.d(TAG, "onOptionsItemSelected: action_settings selected");
            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
        } else if (id == R.id.action_select_wifi){
            Log.d(TAG, "onOptionsItemSelected: action_select_wifi selected");
            return true;
        } else if (id == R.id.action_search){
            Log.d(TAG, "onOptionsItemSelected: action_search selected");
        }/*
        else if (id == R.id.menuBubbleWrapStyle){
            Log.d(TAG, "onOptionsItemSelected: bubbleWrap executed");
            changeMapStyle(new BubbleWrapStyle());
        } else if (id == R.id.menuCinnabarStyle){
            Log.d(TAG, "onOptionsItemSelected: cinnabar executed");
            changeMapStyle(new CinnabarStyle());
        } else if (id == R.id.menuRefillStyle){
            Log.d(TAG, "onOptionsItemSelected: refill executed");
            changeMapStyle(new RefillStyle());
        }*/

        return super.onOptionsItemSelected(item);
    }






    @Override
    protected void onPause() {
        super.onPause();
        view.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        view.onResume();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        view.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        view.onLowMemory();
    }
}
