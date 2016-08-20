package edu.drake.research.android.openlips;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.mapzen.android.graphics.MapFragment;
import com.mapzen.android.graphics.MapzenMap;
import com.mapzen.android.graphics.OnMapReadyCallback;
import com.mapzen.tangram.LngLat;

public class MainActivity extends BaseActivity{


    //logging members
    private final String TAG = this.getClass().getSimpleName();

    //Map members
    private MapzenMap map;
    private boolean enableLocationOnResume = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: method executed");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapzenMap map) {
                Log.e(TAG, "onMapReady: method executed");
                //MainActivity.this.map = map;


            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        /*
        if (map.isMyLocationEnabled()) {
            map.setMyLocationEnabled(false);
            enableLocationOnResume = true;
        }
        */
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*
        if (enableLocationOnResume) {
            map.setMyLocationEnabled(true);
        }
        */
    }
}
