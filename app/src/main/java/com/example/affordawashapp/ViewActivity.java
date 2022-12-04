package com.example.affordawashapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ViewActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        databaseHelper = new DatabaseHelper(ViewActivity.this);
        String[][] data = databaseHelper.retrieveData(getIntent().getStringExtra("table"), 0);
        ListView list = (ListView) findViewById(R.id.listView);
        if(data[0][0].equals("NO DATA!")){
           finish();
        } else {
            String[] id = new String[data.length];
            for (int i = 0; i < data.length; i++) {
                id[i] = data[i][0];
            }
            try {
                CustomViewAdapter adapter = new CustomViewAdapter(ViewActivity.this, getIntent().getIntExtra("resource", -1), getIntent().getStringExtra("table"), data, id);
                list.setAdapter(adapter);
            } catch (Exception e){
                finish();
            }
        }
    }
}