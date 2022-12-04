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
        String[][] data = databaseHelper.retrieveData(DatabaseHelper.TBLITEM, 0);
        ListView list = (ListView) findViewById(R.id.listView);
        if(data[0][0].equals("NO DATA!")){
           finish();
        } else {
            String[] id = new String[data.length];
            for (int i = 0; i < data.length; i++) {
                id[i] = data[i][0];
            }
            CustomViewAdapter adapter = new CustomViewAdapter(ViewActivity.this, R.layout.list_item, DatabaseHelper.TBLITEM, data, id);
            list.setAdapter(adapter);
        }
    }
}