package com.example.affordawashapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHelper = new DatabaseHelper(MainActivity.this);
        if(databaseHelper.retrieveData(DatabaseHelper.TBLMANAGER, 0)[0][0].equals("NO DATA!")) {
            if (databaseHelper.createData(DatabaseHelper.TBLMANAGER, new String[]{"Admin1234", "Admin1234"})) {
                TextView welcome = (TextView) findViewById(R.id.tvWelcome);
                TextView userlogin = (TextView) findViewById(R.id.tvUserlogin);
                welcome.setText("WELCOME");
                userlogin.setText("\nDEFAULT SET!\nUSERNAME: Admin1234\nPASSWORD: Admin1234");
                
            }
        }
        Thread thread = new Thread(){
            public void run(){
                try{
                    sleep(3000);
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