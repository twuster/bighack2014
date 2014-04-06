package com.example.BigHack2014;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by tony on 4/5/14.
 */
public class CongratulationActivity extends Activity implements GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener{

    /** The location Client */
    LocationClient mLocationClient;
    Button saveButton;
    EditText nameText;
    GoogleMap map;
    RunsDataSource datasource;
    Activity context;
    ArrayList<Parcelable> points;
    LatLngBounds.Builder builder;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.congratulation_layout);
        Bundle extras = getIntent().getExtras();
        if (extras!=null){
            points = extras.getParcelableArrayList("points");
        }
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.congrat_map)).getMap();
        map.setMyLocationEnabled(true);

        builder = new LatLngBounds.Builder();
        PolylineOptions options = new PolylineOptions();
        for (int i =0; i<points.size(); i++){
            options.add((LatLng)points.get(i));
            builder.include((LatLng)points.get(i));
        }
        Polyline line = map.addPolyline(options
                .width(5)
                .color(Color.BLUE));
        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition arg0) {
                if(points.size()>0){
                    map.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 16));
                }
            }
        });


        mLocationClient = new LocationClient(this, this, this);
        nameText = (EditText)findViewById(R.id.nameText);
        datasource = new RunsDataSource(this);
        datasource.open();
        saveButton = (Button)findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Run run = new Run();
                map.snapshot(new GoogleMap.SnapshotReadyCallback() {
                    @Override
                    public void onSnapshotReady(Bitmap bitmap) {
                        writeBitmap(bitmap, nameText.getText().toString() + ".jpg");
                        run.setBitmap(nameText.getText().toString() + ".jpg");
                        run.setDate(new Date());
                        run.setName(nameText.getText().toString());
                        datasource.createRun(run);
                        Toast.makeText(context, "Run Saved", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(context, MapListView.class);
                        context.startActivity(i);
                        context.finish();
                    }
                });
            }
        });
        context = this;
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
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }

    private void writeBitmap(Bitmap b, String name){
        FileOutputStream out = null;
        String path = Environment.getExternalStorageDirectory().toString();
        File file = new File(path, name);

        try {
            out = new FileOutputStream(file);
            b.compress(Bitmap.CompressFormat.PNG, 90, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                out.close();
            } catch(Throwable ignore) {}
        }
    }
}
