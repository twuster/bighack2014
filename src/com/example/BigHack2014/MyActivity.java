package com.example.BigHack2014;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MyActivity extends Activity{

    Context context;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        context = this;

        Button tonyButton = (Button)findViewById(R.id.tonybutton);
        tonyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("tonybutton", "PRESSING BUTTON");
                Intent i = new Intent(context, CongratulationActivity.class);
                context.startActivity(i);
            }
        });
    }

//    @Override
//    public void onClick(View v) {
//        switch(v.getId()){
//            case R.id.johnbutton:
//
//                break;
//            case R.id.vincentbutton:
//                Intent myIntent = new Intent(this,MapListView.class);
//                this.startActivity(myIntent);
//                break;
//            case R.id.nishantbutton:
//
//                break;
//            case R.id.tonybutton:
//
//                break;
//            default:
//                break;
//
//
//        }
//    }

    public void openListView(View view) {
        Intent myIntent = new Intent(this,MapListView.class);
        this.startActivity(myIntent);
    }
}
