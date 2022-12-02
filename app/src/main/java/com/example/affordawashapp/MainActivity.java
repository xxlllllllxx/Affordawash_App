package com.example.affordawashapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;



public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Thread thread = new Thread(){
            public void run(){
                try{
                    sleep(5000);
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        
        thread.start();
    }
    
}