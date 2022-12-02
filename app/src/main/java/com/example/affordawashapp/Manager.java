package com.example.affordawashapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class Manager extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    TextView textView;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        databaseHelper = new DatabaseHelper(Manager.this);
        textView = (TextView) findViewById(R.id.tvMan);
        intent = getIntent();
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
            case R.id.newEmployee:
                this.addEmployee();
                break;
            case R.id.newItem:
                addItem();
                break;
            case R.id.newWashingMachine:
                break;
            case R.id.changePassword:
                break;
            case R.id.logout:
                intent.removeExtra("id");
                intent.removeExtra("username");
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                return false;
        }
        return false;
    }
    
    private void addEmployee(){
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.add_employee, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(Manager.this);
        builder.setView(view);
        
        builder.setPositiveButton("REGISTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
        
    }
    private void addItem(){
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.add_item, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(Manager.this);
        builder.setView(view);
    
        builder.setPositiveButton("STOCK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}