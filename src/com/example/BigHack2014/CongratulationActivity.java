package com.example.BigHack2014;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

import java.util.Date;

/**
 * Created by tony on 4/5/14.
 */
public class CongratulationActivity extends Activity implements GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener, View.OnClickListener{

    /** The location Client */
    LocationClient mLocationClient;
    Button saveButton;
    GoogleMap map;
    RunsDataSource datasource;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.congratulation_layout);
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setMyLocationEnabled(true);
        mLocationClient = new LocationClient(this, this, this);
        saveButton = (Button)findViewById(R.id.save_button);
        datasource = new RunsDataSource(this);
        datasource.open();
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.save_button:
                final Run run = new Run();
                map.snapshot(new GoogleMap.SnapshotReadyCallback() {
                    @Override
                    public void onSnapshotReady(Bitmap bitmap) {
                        run.setBitmap(bitmap);
                        run.setId((long) Math.random() * 9999999);
                        run.setDate(new Date());
                        datasource.createRun(run);
                    }
                });
                break;
            default:
                break;
        }

    }
}
