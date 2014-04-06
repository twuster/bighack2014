package com.example.BigHack2014;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.Button;

public class MyActivity extends Activity implements View.OnClickListener{

    Context context;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findViewById(R.id.johnbutton).setOnClickListener(this);
        context = this;
        Button tonyButton = (Button)findViewById(R.id.tonybutton);
        tonyButton.setOnClickListener(this);
        Button vincentButton = (Button)findViewById(R.id.vincentbutton);
        vincentButton.setOnClickListener(this);
        Button nishantButton = (Button)findViewById(R.id.nishantbutton);
        nishantButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.johnbutton:
                Intent intent = new Intent(this, DrawMapActivity.class);
                this.finish();
                startActivity(intent);
                break;
            case R.id.vincentbutton:
                Intent myIntent = new Intent(this,MapListView.class);
                this.startActivity(myIntent);
                break;
            case R.id.nishantbutton:

                break;
            case R.id.tonybutton:
                Log.d("tonybutton", "PRESSING BUTTON");
                Intent i = new Intent(context, CongratulationActivity.class);
                context.startActivity(i);
                break;
            default:
                break;


        }
    }

    public void openListView(View view) {
        Intent myIntent = new Intent(this,MapListView.class);
        this.startActivity(myIntent);
    }
}
