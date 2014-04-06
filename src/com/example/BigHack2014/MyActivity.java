package com.example.BigHack2014;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
                Intent myIntent = new Intent(this,MapListView.class);
                this.startActivity(myIntent);
                break;
            case R.id.nishantbutton:

                break;
            case R.id.tonybutton:

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
