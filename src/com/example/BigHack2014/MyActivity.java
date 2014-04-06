package com.example.BigHack2014;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MyActivity extends Activity implements View.OnClickListener {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.johnbutton:

                break;
            case R.id.vincentbutton:

                break;
            case R.id.nishantbutton:

                break;
            case R.id.tonybutton:
                Log.d("tonybutton", "PRESSING BUTTON");
                Intent i = new Intent(this, CongratulationActivity.class);
                this.startActivity(i);
                break;
            default:
                break;


        }
    }
}
