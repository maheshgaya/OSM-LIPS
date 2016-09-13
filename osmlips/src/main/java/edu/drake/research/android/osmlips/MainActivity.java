package edu.drake.research.android.osmlips;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.mapzen.android.graphics.MapFragment;
import com.mapzen.android.graphics.MapzenMap;
import com.mapzen.android.graphics.OnMapReadyCallback;
import com.mapzen.android.graphics.model.BubbleWrapStyle;
import com.mapzen.android.graphics.model.CinnabarStyle;
import com.mapzen.android.graphics.model.Marker;
import com.mapzen.pelias.gson.Properties;
import com.mapzen.tangram.LngLat;
import com.mapzen.tangram.MapController;
import com.mapzen.tangram.MapData;
import com.mapzen.tangram.MapView;
import com.squareup.okhttp.internal.Platform;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends BaseActivity{ //implements MapView.OnMapReadyCallback {


    //logging members
    private final String TAG = this.getClass().getSimpleName();

    //Map members
    private com.mapzen.android.graphics.MapView mMapView;
    private MapzenMap mMapzenMap;


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
                mMapzenMap.addMarker(new Marker(-93.6518900,41.6013800));
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
        mMapView = (com.mapzen.android.graphics.MapView) findViewById(R.id.map);

        //mMapView.getMapAsync(this, "walkabout-style-more-labels.yaml");
        mMapView.getMapAsync(new CinnabarStyle(), new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapzenMap mapzenMap) {
                //Map is ready
                MainActivity.this.mMapzenMap = mapzenMap;
                if (Build.VERSION.SDK_INT >= 21) {
                    mMapzenMap.setMyLocationEnabled(true); //works only with API 21 and above
                }
            }
        });
    }



    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
