package edu.drake.research.android.lipswithmaps.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.drake.research.android.lipswithmaps.R;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback{
    private GoogleMap mMap;
    private final LatLng DRAKE_UNIVERSITY = new LatLng(41.6013800, -93.6518900);

    //Binding views
    @BindView(R.id.toolbar) Toolbar mToolbar;

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
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney, Australia, and move the camera.
        mMap.addMarker(new MarkerOptions().position(DRAKE_UNIVERSITY).title("Drake University! Yay!"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(drakeUniversity));
        setCameraPosition(DRAKE_UNIVERSITY);
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
