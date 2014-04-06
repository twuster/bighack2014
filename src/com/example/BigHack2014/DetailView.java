package com.example.BigHack2014;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.kindredprints.android.sdk.KindredOrderFlow;
import com.kindredprints.android.sdk.KLOCPhoto; // use this
import com.kindredprints.android.sdk.KindredOrderFlowActivity;

import java.io.FileOutputStream;

public class DetailView extends Activity{

    public Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        context = this;

        Button printButton = (Button)findViewById(R.id.printButton);
        printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KindredOrderFlow orderFlow  = new KindredOrderFlow(context, "eXv0gT0jqHLo0sB8Ku9v7UFo");

//                Save image to disc and give path
                FileOutputStream out = null;
                Intent intent = getIntent();
                try {
                    out = new FileOutputStream((String) intent.getExtras().get("message"));
//                    intent.getExtras().get("map").compress(Bitmap.CompressFormat.PNG, 90, out);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try{
                        out.close();
                    } catch(Throwable ignore) {}
                }

                orderFlow.addImageToCart(new KLOCPhoto(null, (String) intent.getExtras().get("message")));
                Intent i = new Intent(getApplicationContext(), KindredOrderFlowActivity.class);
                startActivityForResult(i, 0);
            }
        });



        Intent intent = getIntent();
        super.onCreate(savedInstanceState);
        String name = (String) intent.getExtras().get("message");
//        String date = (String) intent.getExtras().get("date");
//        String map = (String) intent.getExtras().get("map");
        setContentView(R.layout.detail_view);
        TextView textview = (TextView) findViewById(R.id.textView);
        textview.setText(name);
//        date.setText(date);
//        mapView.setText(map);
    }

}
