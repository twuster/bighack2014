package com.example.BigHack2014;

import android.app.Activity;
import android.content.Intent;
import android.hardware.*;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.ListIterator;

public class CompassActivity extends Activity implements SensorEventListener, GooglePlayServicesClient.ConnectionCallbacks,
    GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {

    // define the display assembly compass picture
    private ImageView image;

    // record the compass picture angle turned
    private float currentDegree = 0f;

    // device sensor manager
    private SensorManager mSensorManager;

    private Location currentLocation;
    private LocationRequest locationRequest;
    private static LocationClient locationClient;
    private boolean onPath = false;
    private ArrayList<LatLng> points;
    private ListIterator<LatLng> path;
    private Location currentDest;
    private ArrayList<LatLng> pointsVisited = new ArrayList<LatLng>();
    private int counter = 0;

    TextView tvHeading;
    TextView alert;
    Button button;
    Chronometer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compass);

        //
        image = (ImageView) findViewById(R.id.imageViewCompass);

        // TextView that will tell the user what degree is he heading
        tvHeading = (TextView) findViewById(R.id.tvHeading);

        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        Bundle extras = getIntent().getExtras();
        if (extras!=null){
            points = extras.getParcelableArrayList("points");
        }

        locationClient = new LocationClient(this, this, this);
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(250);
        locationRequest.setFastestInterval(250);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        path = points.listIterator();
        LatLng ll = path.next();
        currentDest = new Location("");
        currentDest.setLatitude(ll.latitude);
        currentDest.setLongitude(ll.longitude);

        alert = (TextView) findViewById(R.id.alertText);
        alert.setText("Go to start point");

        button = (Button) findViewById(R.id.cancel_complete_button);

        timer = (Chronometer) findViewById(R.id.timer);



    }

    @Override
    public void onStart(){
        super.onStart();
        locationClient.connect();
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);
    }

    private void incrementCounter(){
        counter = (counter + 1) % 10;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // get the angle around the z-axis rotated
        float north = Math.round(event.values[0]);


        float bearing = 0f;
        if (locationClient.isConnected() && currentDest != null) {
            float distance = currentLocation.distanceTo(currentDest);
            if (distance < 5){
                if (!onPath){
                    alert.setText("Begin run!");
                    onPath = true;
                    timer.setBase(SystemClock.elapsedRealtime());
                }

                LatLng nextDest = new LatLng(0.0, 0.0);
                if (path.hasNext()){
                    nextDest = path.next();
                } else {
                    alert.setText("Run complete!");
                    button.setText("Complete!");
                    button.setBackgroundColor(getResources().getColor(R.color.button_green));
                    mSensorManager.unregisterListener(this);
                    return;

                }
                Location nextLocation = new Location("");
                nextLocation.setLatitude(nextDest.latitude);
                nextLocation.setLongitude(nextDest.longitude);
                currentDest = nextLocation;
            }
            bearing = currentLocation.bearingTo(currentDest);
        }

        GeomagneticField geomagneticField = new GeomagneticField(
                                                   (float) currentLocation.getLatitude(),
                                                   (float) currentLocation.getLongitude(),
                                                   (float) currentLocation.getAltitude(),
                                                   System.currentTimeMillis());
        float declination = geomagneticField.getDeclination();

        tvHeading.setText("Heading: " + Float.toString(north-declination+bearing) + " degrees");

        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -north+bearing+declination,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // how long the animation will take place
        ra.setDuration(210);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        // Start the animation
        image.startAnimation(ra);
        currentDegree = -north+bearing+declination;

    }

    public void onClickComplete(View view) {
        Intent i = new Intent(this, CongratulationActivity.class);
        if (pointsVisited.size() > 0) {
            Log.d("Congratulate", Integer.toString(pointsVisited.size()));
            i.putParcelableArrayListExtra("points", pointsVisited);
        } else {
            Log.d("Passing drawn points", Integer.toString(points.size()));
            i.putParcelableArrayListExtra("points", points);
        }
        i.putExtra("time", SystemClock.elapsedRealtime() - timer.getBase());
        this.finish();
        startActivity(i);
        this.finish();
        return;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }

    @Override
    public void onLocationChanged(Location location){
        currentLocation = location;
        if (counter == 0 && onPath) {
            pointsVisited.add(new LatLng(location.getLatitude(), location.getLongitude()));
        }
        incrementCounter();
    }

    @Override
    public void onConnected(Bundle dataBundle) {
        onLocationChanged(locationClient.getLastLocation());
        locationClient.requestLocationUpdates(locationRequest, this);
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
    public void onDisconnected(){    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult){
         Log.e("Connection Error", connectionResult.toString());
    }



}