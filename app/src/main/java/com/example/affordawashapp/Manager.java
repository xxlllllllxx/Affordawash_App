package com.example.affordawashapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class Manager extends AppCompatActivity {
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        preferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("LoggedIn", true);
        editor.putString("table", DatabaseHelper.TBLMANAGER);
        editor.apply();
    }
    
    public void textClick(View view){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("LoggedIn", false);
        editor.remove("table");
        editor.apply();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
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