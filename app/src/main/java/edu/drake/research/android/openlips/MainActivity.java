package edu.drake.research.android.openlips;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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

public class MainActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {


    //logging members
    private final String TAG = this.getClass().getSimpleName();

    //Map members
    private MapzenMap map;
    private MapFragment mapFragment;
    private boolean enableLocationOnResume = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: method executed");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);



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

        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.style_array, android.R.layout.simple_spinner_item);

        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //spinner.setAdapter(adapter);
        //spinner.setOnItemSelectedListener(this);

        mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(new RefillStyle(), new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapzenMap map) {
                Log.e(TAG, "onMapReady: method executed");
                MainActivity.this.map = map;
                configureMap(map);
                //map.setMyLocationEnabled(true);
                map.setPosition(new LngLat(-93.6518900, 41.6013800));
                map.setRotation(0f);
                map.setZoom(3f);
                map.setTilt(0f);



            }
        });

    }

    private void configureMap(MapzenMap map) {
        map.addMarker(new Marker(-93.6518900, 41.6013800));
    }

    private void changeMapStyle(MapStyle style) {
        if (map != null) {
            map.setStyle(style);
        }
    }


    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                changeMapStyle(new BubbleWrapStyle());
                break;
            case 1:
                changeMapStyle(new CinnabarStyle());
                break;
            case 2:
                changeMapStyle(new RefillStyle());
                break;
            default:
                changeMapStyle(new BubbleWrapStyle());
                break;
        }
    }


    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing.
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
