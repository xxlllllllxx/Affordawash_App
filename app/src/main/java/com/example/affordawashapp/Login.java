package com.example.affordawashapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
//Edit from github
public class Login extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    SharedPreferences preferences;
    EditText etusername, etpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        databaseHelper = new DatabaseHelper(getApplicationContext());
        
        //if Logged in already
        if(preferences.getBoolean("LoggedIn", false)){
            if(preferences.getString("table", DatabaseHelper.TBLEMPLOYEE).equals(DatabaseHelper.TBLMANAGER)){
                startActivity(new Intent(getApplicationContext(), Manager.class));
                finish();
            } else {
                startActivity(new Intent(getApplicationContext(), Employee.class));
                finish();
            }
        }
        
        etusername = (EditText) findViewById(R.id.etUsername);
        etpassword = (EditText) findViewById(R.id.etPassword);
    
        if(databaseHelper.retrieveData(DatabaseHelper.TBLMANAGER, 0)[0][0].equals("NO DATA!")) {
            if (databaseHelper.createData(DatabaseHelper.TBLMANAGER, new String[]{"Admin1234", "Admin1234"})) {
                Toast.makeText(getApplicationContext(), "\nDEFAULT SET!\nUSERNAME: Admin\nPASSWORD: Admin1234", Toast.LENGTH_LONG).show();
            }
        }
    }
    
    public void onClickLogin(View view){
        
        String username = etusername.getText().toString();
        String password = etpassword.getText().toString();
        boolean loggedIn = false;
        //Check Login fields
        if(username.isEmpty() || password.isEmpty()){
            Toast.makeText(getApplicationContext(), "Login Failed!", Toast.LENGTH_SHORT).show();
        } else {
            //Check Manager Login
            String[][] data = databaseHelper.retrieveData(DatabaseHelper.TBLMANAGER, 0);
            for (String[] holder : data) {
                if (holder[1].equals(username) && holder[2].equals(password) && !loggedIn) {
                    loggedIn = true;
                    //LOGIN AS MANAGER
                    startActivity(new Intent(getApplicationContext(), Manager.class));
                    finish();
                    return;
                }
            }
//
            if(!loggedIn) {
                //Check Employee Login
                data = databaseHelper.retrieveData(DatabaseHelper.TBLEMPLOYEE, 0);
                if (data[0][0].equals("NO DATA!")) {
                    Toast.makeText(getApplicationContext(), "Username and Password Incorrect.", Toast.LENGTH_SHORT).show();
                } else {
                    for (String[] holder : data) {
                        if (holder[1].equals(username) && holder[2].equals(password)) {
                            loggedIn = true;
                            //LOGIN AS EMPLOYEE
                            startActivity(new Intent(getApplicationContext(), Employee.class));
                            finish();
                            return;
                        }
                    }
                    Toast.makeText(getApplicationContext(), "Username and Password Incorrect.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    
    public void onClickForgot(View view){
        //if
    }
}
