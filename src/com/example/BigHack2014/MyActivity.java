package com.example.BigHack2014;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

public class MyActivity extends Activity implements View.OnClickListener {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findViewById(R.id.johnbutton).setOnClickListener(this);
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

                break;
            case R.id.nishantbutton:

                break;
            case R.id.tonybutton:

                break;
            default:
                break;


        }
    }
}
