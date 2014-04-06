package com.example.BigHack2014;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapListView extends MyActivity {

    public Context context;
    private RunsDataSource datasource;
    List<Run> runList;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();
        context = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        Button johnbutton = (Button)findViewById(R.id.johnbutton);
        johnbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DrawMapActivity.class);
                context.startActivity(i);
            }
        });

        final ListView listview = (ListView) findViewById(R.id.list_view);
//        replace below with call to database
        final ArrayList<String> list = new ArrayList<String>();

        datasource = new RunsDataSource(this);
        datasource.open();
        runList = datasource.getAllRuns();
        for (int i=0; i < runList.size(); i ++){
            list.add(runList.get(i).getName());
        }

        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
//                final String item2 = (String) parent.getItemAtPosition(position);
//                final String item3 = (String) parent.getItemAtPosition(position);
                Intent i = new Intent(context, DetailView.class);
                Run run = runList.get(position);
                i.putExtra("message", item);
                i.putExtra("bitmap", run.getBitmap());
                i.putExtra("date", run.getDate());
                context.startActivity(i);
            }

        });
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }


}
