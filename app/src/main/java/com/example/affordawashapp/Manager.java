package com.example.affordawashapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
                unpair();
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
    
    private void displayInfo(String msg){
        Toast toast = Toast.makeText(getApplicationContext(), "\n" + msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.END | Gravity.TOP, 0, 40);
        toast.show();
    }
    
    private void addEmployee(){
        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.add_employee, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(Manager.this);
        builder.setView(view);
        
        builder.setPositiveButton("REGISTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText etusername = (EditText) view.findViewById(R.id.etEmployeeUsername);
                EditText etpassword = (EditText) view.findViewById(R.id.etEmployeeUsername);
                EditText etsalary = (EditText) view.findViewById(R.id.etEmployeeUsername);
                
                if(((etusername.getText().length() == 0) || (etpassword.getText().length() == 0)) || (etsalary.getText().length() == 0)) {
                    displayInfo("Fields cannot be empty");
                    dialog.dismiss();
                } else {
                    String username = etusername.getText().toString();
                    String password = etpassword.getText().toString();
                    String salary = etsalary.getText().toString();
                    if (databaseHelper.createData(databaseHelper.TBLEMPLOYEE, new String[]{username, password, salary})) {
                        displayInfo("Employee Information Saved");
                        dialog.dismiss();
                    } else {
                        displayInfo("Employee Information not Saved");
                        dialog.dismiss();
                    }
                }
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
    
    private void unpair(){
        int[][] pair = databaseHelper.unlist("1 2:2 2:4 3:7 15");
        String str = "";
        for (int[] ints : pair) {
            str += ints[0] + " = " + ints[1] + "\n";
        }
        textView.setText(str);
    }
}