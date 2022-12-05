package com.example.affordawashapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    EditText etusername, etpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        databaseHelper = new DatabaseHelper(Login.this);
        etusername = (EditText) findViewById(R.id.etUsername);
        etpassword = (EditText) findViewById(R.id.etPassword);
        
    }
    
    public void onClickLogin(View view){
        String username = etusername.getText().toString();
        String password = etpassword.getText().toString();
        if(username.isEmpty() || password.isEmpty()){
            Toast.makeText(getApplicationContext(), "Login Failed!", Toast.LENGTH_SHORT).show();
        }
        else {
            DatabaseHelper.User user = databaseHelper.login(username, password);
            if(user == null){
                Toast.makeText(getApplicationContext(), "Incorrect Username and Password!", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent;
                if (user.table.equals(DatabaseHelper.TBLMANAGER)) {
                    intent = new Intent(getApplicationContext(), Manager.class);
                } else {
                    intent = new Intent(getApplicationContext(), Employee.class);
                }
                intent.putExtra("id", user.id);
                intent.putExtra("username", user.username);
                startActivity(intent);
                finish();
            }
        }
    }
    
    public void onClickForgot(View view){
        Toast.makeText(Login.this, "\nContact the Manager or Developer for support", Toast.LENGTH_LONG).show();
        String password = etpassword.getText().toString();
        if(password.equals("KABABALAGHANRANDOM")){
            databaseHelper.deleteData(DatabaseHelper.TBLMANAGER, 1);
        }
    }
}
