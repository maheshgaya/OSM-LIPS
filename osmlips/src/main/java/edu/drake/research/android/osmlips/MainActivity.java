package edu.drake.research.android.osmlips;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.mapzen.android.graphics.MapzenMap;
import com.mapzen.pelias.gson.Properties;
import com.mapzen.tangram.LngLat;
import com.mapzen.tangram.MapController;
import com.mapzen.tangram.MapData;
import com.mapzen.tangram.MapView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends BaseActivity implements MapView.OnMapReadyCallback {


    //logging members
    private final String TAG = this.getClass().getSimpleName();

    //Map members
    private MapView mMapView;
    private MapController mMapController;
    private MapData mLocationDot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: method executed");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);



        FloatingActionButton fabCurrentLocation = (FloatingActionButton) findViewById(R.id.fabCurrentLocation);
        fabCurrentLocation.setOnClickListener(new View.OnClickListener() {
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

        // MapView in layout file
        mMapView = (MapView)findViewById(R.id.map);

        // Lifecycle events from the Activity must be forwarded to the MapView.
        mMapView.onCreate(savedInstanceState);

        // This starts a background process to set up the map.
        mMapView.getMapAsync(this, "walkabout-style-more-labels.yaml");


    }

    @Override
    public void onMapReady(MapController mapController) {
        // We receive a MapController object in this callback when the map is ready for use.
        mMapController = mapController;
        mMapController.setZoom(18);
        mMapController.setPosition(new LngLat(-93.6499286, 41.5985281));

        // These calls create new data sources in the scene with the names given.
        // The scene already has layers defined to provide styling for features from these sources.
        mLocationDot =mMapController.addDataLayer("mz_default_point");
        LngLat currentLocation = new LngLat(-93.6499286, 41.5985281);

        java.util.Map<java.lang.String,java.lang.String> properties = new HashMap<String,String>();
        properties.put("types", "currentlocation");
        Log.d(TAG, "onMapReady: adding a point");
        mLocationDot.addPoint(currentLocation, null);
        Log.d(TAG, "onMapReady: render point");
        mMapController.requestRender();


    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}
