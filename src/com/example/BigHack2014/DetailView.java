package com.example.BigHack2014;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

//import com.kindredprints.android.sdk.KindredOrderFlow;
//import com.kindredprints.android.sdk.KLOCPhoto; // use this
//import com.kindredprints.android.sdk.KindredOrderFlowActivity;

public class DetailView extends Activity{

    public Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.detail_view);

        Button printButton = (Button)findViewById(R.id.printButton);
//        printButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                KindredOrderFlow orderFlow  = new KindredOrderFlow(context, "eXv0gT0jqHLo0sB8Ku9v7UFo");

//                Save image to disc and give path
//                FileOutputStream out = null;
//                Intent intent = getIntent();
//                try {
//                    out = new FileOutputStream((String) intent.getExtras().get("message"));
//                    intent.getExtras().get("map").compress(Bitmap.CompressFormat.PNG, 90, out);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    try{
//                        out.close();
//                    } catch(Throwable ignore) {}
//                }

//                orderFlow.addImageToCart(new KLOCPhoto(null, (String) intent.getExtras().get("message")));
//                Intent i = new Intent(getApplicationContext(), KindredOrderFlowActivity.class);
//                startActivityForResult(i, 0);
//            }
//        });



        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
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
    }

    public Bitmap loadBitmap(String file){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        String path = Environment.getExternalStorageDirectory().toString() + "/" + file;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        return bitmap;
    }

}
