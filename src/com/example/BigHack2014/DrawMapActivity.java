package com.example.BigHack2014;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.LinkedList;

public class DrawMapActivity extends Activity
        implements
        View.OnClickListener,
        ConnectionCallbacks,
        OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMarkerDragListener {

    private GoogleMap mMap;
    private LocationClient mLocationClient;
    private int stat;
    private LinkedList<LatLng> points;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapdraw);
        findViewById(R.id.set_path_button).setOnClickListener(this);
        stat = 0;
        points = new LinkedList<LatLng>();
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        setUpLocationClientIfNeeded();
        mLocationClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mLocationClient != null) {
            mLocationClient.disconnect();
        }
    }

    @Override
    public void onClick(View v) {
        switch (stat) {
            case (STAT_NEW):
                if (setNew()) stat = STAT_START;
                break;
            case (STAT_START):
                ((Button) findViewById(R.id.set_path_button)).setText("Run this path");
                stat = STAT_PATH;
                break;
            case (STAT_PATH):
                // pass
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {}

    @Override
    public void onConnected(Bundle connectionHint) {}

    @Override
    public void onDisconnected() {}

    @Override
    public void onConnectionFailed(ConnectionResult result) {}

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        if (stat != STAT_PATH) return;
        LatLng latLng = marker.getPosition();
        if (points.size() > 0) {
            PolylineOptions options = new PolylineOptions()
                    .add(points.peekLast())
                    .add(latLng);
            mMap.addPolyline(options);
        }
        points.add(latLng);
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    private Location getMyLocation() {
        if (mLocationClient != null && mLocationClient.isConnected()) {
            return mLocationClient.getLastLocation();
        }
        return null;
    }

    private boolean setNew() {
        Location l = getMyLocation();
        if (l != null) {
            LatLng latLng = new LatLng(l.getLatitude(), l.getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .draggable(true)
                    .flat(true));
            ((Button) findViewById(R.id.set_path_button)).setText("Set as Start");
            return true;
        }
        Toast.makeText(this, "getMyLocation() return null", 2).show();
        return false;
    }

    private void setUpLocationClientIfNeeded() {
        if (mLocationClient == null) {
            mLocationClient = new LocationClient(
                    getApplicationContext(),
                    this,  // ConnectionCallbacks
                    this); // OnConnectionFailedListener
        }
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the MapFragment.
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
                mMap.setOnMyLocationButtonClickListener(this);
                mMap.setOnMarkerDragListener(this);
            }
        }
    }

    private final int STAT_NEW = 0, STAT_START = 1, STAT_PATH = 2;

}
