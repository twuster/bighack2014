package com.example.BigHack2014;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

import java.io.File;
import java.io.FileOutputStream;
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
    Context context;
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
                        Toast.makeText(context, "Map Saved", Toast.LENGTH_SHORT).show();
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
