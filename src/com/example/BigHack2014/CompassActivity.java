package com.example.BigHack2014;

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;

/**
 * Created by Nishant on 4/5/14.
 */
public class CompassActivity extends Activity {

    private static SensorManager sensorManager;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.compass);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    private float bearingToPoint(float lat, float lon) {

    }

}
