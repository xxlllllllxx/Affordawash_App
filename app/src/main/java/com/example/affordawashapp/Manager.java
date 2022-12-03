package com.example.affordawashapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Manager extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        databaseHelper = new DatabaseHelper(Manager.this);
        intent = getIntent();
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
            case R.id.changePassword:
                test();
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
        toast.setGravity(Gravity.END | Gravity.TOP, 0, 60);
        toast.show();
    }

    public void onClickButtons(View view){
        switch (view.getId()){
            case R.id.btnAddE:
                addEmployee();
                break;
            case R.id.btnAddI:
                addItem();
                break;
            case R.id.btnAddM:
                addMachine();
                break;
            default:
        }
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
                EditText etpassword = (EditText) view.findViewById(R.id.etEmployeePassword);
                EditText etsalary = (EditText) view.findViewById(R.id.etEmployeeSalary);
                try {
                    if (etusername.getText().toString().length() == 0 || etpassword.getText().toString().length() == 0 || etsalary.getText().toString().length() == 0) {
                        displayInfo("Fields cannot be empty");
                    } else {
                        String username = etusername.getText().toString();
                        String password = etpassword.getText().toString();
                        String salary = etsalary.getText().toString();
                        if (databaseHelper.createData(databaseHelper.TBLEMPLOYEE, new String[]{username, password, salary})) {
                            displayInfo("Employee Information Saved");
                        } else {
                            displayInfo("Employee Information not Saved");
                        }
                    }
                } catch (Exception e){
                    displayInfo("Fields cannot be empty");
                }
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
        final View view = inflater.inflate(R.layout.add_item, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(Manager.this);
        builder.setView(view);
    
        builder.setPositiveButton("STOCK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText etItemName = (EditText) view.findViewById(R.id.etItemName);
                EditText etItemQuantity = (EditText) view.findViewById(R.id.etItemQuantity);
                EditText etItemCost = (EditText) view.findViewById(R.id.etItemCost);
                EditText etItemLPrice = (EditText) view.findViewById(R.id.etItemLowestP);
                EditText etItemSPrice = (EditText) view.findViewById(R.id.etItemSellingP);
                try {
                    if ((etItemName.getText().toString().equals("")) || (etItemQuantity.getText().toString().equals("")) || (etItemCost.getText().toString().equals("")) || (etItemLPrice.getText().toString().equals("")) || (etItemSPrice.getText().toString().equals(""))) {
                        displayInfo("Fields cannot be empty");
                    } else {
                        String itemName = etItemName.getText().toString();
                        String itemQuantity = etItemQuantity.getText().toString();
                        String itemCost = etItemCost.getText().toString();
                        String itemLPrice = etItemLPrice.getText().toString();
                        String itemSPrice = etItemSPrice.getText().toString();
                        if (databaseHelper.createData(databaseHelper.TBLITEM, new String[]{itemName, itemQuantity, itemCost, itemLPrice, itemSPrice})) {
                            displayInfo("Product Information Saved");
                        } else {
                            displayInfo("Product Information not Saved");
                        }
                    }
                }catch (Exception e){
                    displayInfo("Fields cannot be empty");
                }
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

    private void addMachine(){
        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.add_machine, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(Manager.this);
        builder.setView(view);

        builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText etMachineName = (EditText) view.findViewById(R.id.etMachineName);
                CheckBox cbWash = (CheckBox) view.findViewById(R.id.cbWashing);
                CheckBox cbDry = (CheckBox) view.findViewById(R.id.cbDrying);
                EditText etWashP = (EditText) view.findViewById(R.id.etWashingP);
                EditText etDryP = (EditText) view.findViewById(R.id.etDryingP);
                try {
                    if (etMachineName.getText().toString().equals("") || (!cbWash.isChecked() && !cbDry.isChecked())) {
                        displayInfo("Fields cannot be empty");
                    } else {
                        test();
                        String machineName = etMachineName.getText().toString();
                        String washing = (cbWash.isChecked())?"true" : "false";
                        String drying = (cbDry.isChecked())?"true" : "false";
                        String washP = (cbWash.isChecked())? etWashP.getText().toString() : "0.0";
                        String dryP = (cbDry.isChecked())? etDryP.getText().toString(): "0.0";
                        if (databaseHelper.createData(databaseHelper.TBLMACHINE, new String[]{machineName, washing, drying, washP, dryP})) {
                            displayInfo("Machine Information Saved");
                        } else {
                            displayInfo("Machine Information not Saved");
                        }
                    }
                } catch (Exception e){
                    displayInfo("Fields cannot be empty");

                }
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
    }

    private void test(){
        String[][] data = databaseHelper.retrieveData(DatabaseHelper.TBLMACHINE, 0);
        String str = "";
        for (String[] s : data) {
            for (String s1 : s) {
                str += s1 + " ";
            }
            str += "\n";
        }
    }

}