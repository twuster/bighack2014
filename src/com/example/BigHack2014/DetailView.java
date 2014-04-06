package com.example.BigHack2014;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.kindredprints.android.sdk.KLOCPhoto;
import com.kindredprints.android.sdk.KindredOrderFlow;
import com.kindredprints.android.sdk.KindredOrderFlowActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailView extends Activity{

    public Context context;
    private final static String KINDRED_APP_KEY = "test_eXv0gT0jqHLo0sB8Ku9v7UFo";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.detail_view);




        Intent intent = getIntent();
        final Bundle extras = intent.getExtras();
        String name = (String) extras.get("message");
        String bitmap = (String) extras.get("bitmap");
        Date date = (Date) extras.get("date");
        TextView nameText = (TextView) findViewById(R.id.nameText);
        nameText.setText(name);
        Bitmap b = loadBitmap(bitmap);
        ImageView mapView = (ImageView)findViewById(R.id.mapView);
        mapView.setImageBitmap(b);
        TextView dateText = (TextView) findViewById(R.id.dateText);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String parsedDate = dateFormat.format(date);
        dateText.setText(parsedDate);


        Button printButton = (Button)findViewById(R.id.printButton);
        printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KindredOrderFlow orderFlow  = new KindredOrderFlow(context, KINDRED_APP_KEY);
                String path = Environment.getExternalStorageDirectory().toString() + "/" +  extras.get("bitmap");
                orderFlow.addImageToCart(new KLOCPhoto(null, path));
                Intent i = new Intent(getApplicationContext(), KindredOrderFlowActivity.class);
                startActivityForResult(i, 0);
            }
        });
    }

    public Bitmap loadBitmap(String file){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        String path = Environment.getExternalStorageDirectory().toString() + "/" + file;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        return bitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == KindredOrderFlowActivity.KP_RESULT_CANCELLED) {
            Log.i("MyActivity", "User cancelled Kindred purchase");
        } else if (resultCode == KindredOrderFlowActivity.KP_RESULT_PURCHASED) {
            Log.i("MyActivity", "User completed Kindred purchase!");
        }
    }

}
