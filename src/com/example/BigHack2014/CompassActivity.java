package com.example.BigHack2014;

import android.app.Activity;
import android.content.Context;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Created by Nishant on 4/5/14.
 */
public class CompassActivity extends Activity implements GooglePlayServicesClient.ConnectionCallbacks,
    GooglePlayServicesClient.OnConnectionFailedListener, LocationListener, SensorEventListener {

    private static SensorManager sensorManager;
    private static LocationClient locationClient;
    private Location currentLocation;
    private LocationRequest locationRequest;
    private float[] gravity;
    private float[] geomagnetic;
    private boolean onPath = false;
    private LinkedList<LatLng> points;
    private ListIterator<LatLng> path;
    private Location currentDest;
    private LinkedList<LatLng> pointsVisited = new LinkedList<LatLng>();
    private float currentDegree = (float) 0.0;

    @Override
    public void onStart(){
        super.onStart();
        locationClient.connect();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_GAME);
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.compass);

        points = new LinkedList<LatLng>();
        points.add(new LatLng(0.0, 0.0));
        path = points.listIterator();

        Location dest = new Location("");
        LatLng d = path.next();
        dest.setLatitude(d.latitude);
        dest.setLongitude(d.longitude);
        currentDest = dest;

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        locationClient = new LocationClient(this, this, this);
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(250);
        locationRequest.setFastestInterval(250);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    private float bearingToPoint(float lat, float lon) {
        Location nextPoint = new Location("");
        nextPoint.setLatitude(lat);
        nextPoint.setLongitude(lon);
        float bearing = currentLocation.bearingTo(nextPoint);
        if (gravity != null && geomagnetic != null) {
            Log.d("Bearing", "Finding orientation!");
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, gravity, geomagnetic);
            float orientation[] = new float[3];
            if (success) {
                SensorManager.getOrientation(R, orientation);
            }
            float azimuth = orientation[0];
            Log.d("Azimuth", Float.toString(azimuth));
            float heading = (float) (azimuth * (180.0 / Math.PI));
            GeomagneticField geomagneticField = new GeomagneticField(
                                                       (float) currentLocation.getLatitude(),
                                                       (float) currentLocation.getLongitude(),
                                                       (float) currentLocation.getAltitude(),
                                                       System.currentTimeMillis());
            float declination = geomagneticField.getDeclination();
            float trueHeading = heading + declination;
            return (float)(trueHeading * -1.0) + bearing; //degrees to location
        } else {
            Log.d("Gravity or Geo", "null");
        }
        return (float) 0.0;
    }

    private void updateView(float bearing){
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                bearing,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );

        ra.setDuration(210);
        ra.setFillAfter(true);
        ImageView image = (ImageView) findViewById(R.id.imageViewCompass);
        image.startAnimation(ra);
        currentDegree = bearing;

    }

    @Override
    public void onConnected(Bundle dataBundle) {
        onLocationChanged(locationClient.getLastLocation());
        locationClient.requestLocationUpdates(locationRequest, this);
    }

    @Override
    public void onDisconnected(){
        return;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult){
         Log.e("Connection Error", connectionResult.toString());
    }

    @Override
    public void onLocationChanged(Location location){
        Log.d("Location", "location changed");
        this.currentLocation = location;
        float distance = location.distanceTo(currentDest);
        if (distance < 5){
            if (!onPath){
                //TODO: give "Start run" prompt in view
                onPath = true;
            }

            LatLng nextDest = new LatLng(0.0, 0.0);
            if (path.hasNext()){
                nextDest = path.next();
            } else {
                //TODO: go to congratulatory page
                //TODO: make polyline + finish activity
                this.finish();
            }
            Location nextLocation = new Location("");
            nextLocation.setLatitude(nextDest.latitude);
            nextLocation.setLongitude(nextDest.longitude);
            currentDest = nextLocation;
        }
        float bearing = bearingToPoint((float)currentDest.getLatitude(), (float)currentDest.getLongitude());
        if (onPath){
            pointsVisited.add(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
        }
        //TODO: Update view with new bearing
        Log.d("Bearing", Float.toString(bearing));
        if (bearing < 0) {
            bearing = bearing + 360;
        }
        Log.d("New bearing", Float.toString(bearing));
        updateView(bearing);

    }

    @Override
    public void onStop(){
        if (locationClient.isConnected()){
            locationClient.removeLocationUpdates(this);
            locationClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()){
            case Sensor.TYPE_ACCELEROMETER:
                gravity = event.values.clone();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                geomagnetic = event.values.clone();
                break;
        }
    }

}
