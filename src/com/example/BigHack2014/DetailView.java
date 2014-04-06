package com.example.BigHack2014;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by vincenttian on 4/5/14.
 */
public class DetailView extends Activity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
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
