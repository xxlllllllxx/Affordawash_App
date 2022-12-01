package com.example.affordawashapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class Manager extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        databaseHelper = new DatabaseHelper(Manager.this);
        textView = (TextView) findViewById(R.id.tvMan);
        Intent intent = getIntent();
        textView.setText(databaseHelper.retrieveData(DatabaseHelper.TBLMANAGER, intent.getIntExtra("id", 0))[0][1]);
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.manager_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add:
                break;
            case R.id.changePassword:
                break;
            case R.id.logout:
                break;
            default:
                return false;
        }
        return false;
    }
}