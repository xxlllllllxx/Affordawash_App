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
    String[] managerData;
    Intent intent;
    TextView tvName;
    TextView tvTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        databaseHelper = new DatabaseHelper(Manager.this);
        intent = getIntent();
        tvName = (TextView) findViewById(R.id.tvManagerName);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        updateInfo();
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
            case  R.id.viewTransactions:
                if(databaseHelper.getCount(DatabaseHelper.TBLCUSTOMER, "id") > 0) {
                    Intent intent = new Intent(Manager.this, ViewActivity.class);
                    intent.putExtra("table", DatabaseHelper.TBLCUSTOMER);
                    intent.putExtra("resource", R.layout.list_customer);
                    startActivity(intent);
                } else {
                    displayInfo("Transaction Table is Empty");
                }
                break;
            case R.id.changePassword:
                changePassword();
                break;
            case R.id.logout:
                intent.removeExtra("id");
                intent.removeExtra("username");
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.about:
                about();
                break;
            case R.id.profile:
                profile();
                break;
            default:
                return false;
        }
        return false;
    }
    
    private void displayInfo(String msg){
        Toast toast = Toast.makeText(getApplicationContext(), "\n" + msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.END | Gravity.TOP, 0, 75);
        toast.show();
    }

    public void onClickButtons(View view){
        intent = new Intent(Manager.this, ViewActivity.class);
        String tbl = "";
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
            case R.id.btnViewE:
                intent.putExtra("table", DatabaseHelper.TBLEMPLOYEE);
                intent.putExtra("resource", R.layout.list_employee);
                tbl = DatabaseHelper.TBLEMPLOYEE;
                break;
            case R.id.btnViewI:
                intent.putExtra("table", DatabaseHelper.TBLITEM);
                intent.putExtra("resource", R.layout.list_item);
                tbl = DatabaseHelper.TBLITEM;
                break;
            case R.id.btnViewM:
                intent.putExtra("table", DatabaseHelper.TBLMACHINE);
                intent.putExtra("resource", R.layout.list_machine);
                tbl = DatabaseHelper.TBLMACHINE;
                break;
            default:
                break;
        }
        if(!tbl.equals("")) {
            if(databaseHelper.getCount(tbl, "id") > 0) {
                startActivity(intent);
            } else {
                displayInfo("Selected view Empty");
            }
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
                EditText etWholename = (EditText) view.findViewById(R.id.etEmployeeWholeName);
                EditText etusername = (EditText) view.findViewById(R.id.etEmployeeUsername);
                EditText etpassword = (EditText) view.findViewById(R.id.etEmployeePassword);
                EditText etsalary = (EditText) view.findViewById(R.id.etEmployeeSalary);
                try {
                    if (etWholename.getText().length() == 0 || etusername.getText().toString().length() == 0 || etpassword.getText().toString().length() == 0 || etsalary.getText().toString().length() == 0) {
                        displayInfo("Fields cannot be empty");
                    } else {
                        String wholename = etWholename.getText().toString();
                        String username = etusername.getText().toString();
                        String password = etpassword.getText().toString();
                        String salary = etsalary.getText().toString();
                        if (databaseHelper.createData(databaseHelper.TBLEMPLOYEE, new String[]{username, password, wholename, salary})) {
                            displayInfo("Employee Information Saved");
                        } else {
                            displayInfo("Employee Information not Saved");
                        }
                    }
                } catch (Exception e){
                    displayInfo("Fields cannot be empty");
                }
                dialog.dismiss();
                updateInfo();
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
                updateInfo();
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
                        String machineName = etMachineName.getText().toString();
                        String washing = (cbWash.isChecked())?"1" : "0";
                        String drying = (cbDry.isChecked())?"1" : "0";
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
                updateInfo();
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
    
    private void test() {
        //For testing purposes
    }

    private void changePassword(){
        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.change_password, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(Manager.this);
        builder.setView(view);
        builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String oldPass = ((EditText) view.findViewById(R.id.etOldPassword)).getText().toString();
                String newPass = ((EditText) view.findViewById(R.id.etNewPass)).getText().toString();
                String confirm = ((EditText) view.findViewById(R.id.etConfirmNewPAss)).getText().toString();

                if(newPass.equals(confirm)){
                    if(databaseHelper.changePassword(DatabaseHelper.TBLMANAGER, getIntent().getIntExtra("id", 0), oldPass, confirm)){
                        displayInfo("Password changed");
                    } else {
                        displayInfo("Old password not match");
                    }
                } else {
                    displayInfo("New password not match");
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.show();
    }
    
    private void updateInfo(){
        try {
            managerData = databaseHelper.retrieveData(DatabaseHelper.TBLMANAGER, intent.getIntExtra("id", 0))[0];
            tvName.setText(managerData[3].toUpperCase());
            tvTitle.setText("AFFORDAWASH "+ managerData[4].toUpperCase());
        } catch (Exception e){
            displayInfo("Error retrieving information");
        }
        TextView tvECount = (TextView) findViewById(R.id.tvEmployeeCount);
        TextView tvICount = (TextView) findViewById(R.id.tvItemCount);
        TextView tvMCount = (TextView) findViewById(R.id.tvMachineCount);
        
        tvECount.setText("" + databaseHelper.getCount(DatabaseHelper.TBLEMPLOYEE, DatabaseHelper.employeeFields[0]));
        tvICount.setText("" + databaseHelper.getCount(DatabaseHelper.TBLITEM, DatabaseHelper.itemFields[0]));
        tvMCount.setText("" + databaseHelper.getCount(DatabaseHelper.TBLMACHINE, DatabaseHelper.machineFields[0]));
        
    }

    private void about(){
        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.about, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(Manager.this);
        builder.setView(view);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.show();
    }

    private void profile(){
        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.profile, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(Manager.this);
        builder.setView(view);
        int id = getIntent().getIntExtra("id", 1);
        EditText etWholename = (EditText) view.findViewById(R.id.etMname);
        EditText etUsername = (EditText) view.findViewById(R.id.etMUsername);
        EditText etTitle = (EditText) view.findViewById(R.id.etMTitle);
        try {
            String[][] data = databaseHelper.retrieveData(DatabaseHelper.TBLMANAGER, id);
            if (data[0][0].equals("NO DATA!")) {
                displayInfo("No data!");
            } else {
                etWholename.setText(data[0][3]);
                etUsername.setText(data[0][1]);
                etTitle.setText(data[0][4]);
            }
        }catch (Exception e){
            displayInfo("No data 2");
        }

        builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String wholename = etWholename.getText().toString();
                String username = etUsername.getText().toString();
                String title = etTitle.getText().toString();
                try {
                    if (databaseHelper.updateString(DatabaseHelper.TBLMANAGER, DatabaseHelper.managerFields[1], username, id)
                            && databaseHelper.updateString(DatabaseHelper.TBLMANAGER, DatabaseHelper.managerFields[3], wholename, id)
                            && databaseHelper.updateString(DatabaseHelper.TBLMANAGER, DatabaseHelper.managerFields[4], title, id)) {
                        displayInfo("Profile Updated");
                        updateInfo();
                    } else {
                        displayInfo("Changes not saved");
                    }
                } catch (Exception e){
                    displayInfo("Changes not saved 2");
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    @Override
    protected void onResume() {
        updateInfo();
        super.onResume();
    }
}