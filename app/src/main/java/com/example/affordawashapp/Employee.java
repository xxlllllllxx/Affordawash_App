package com.example.affordawashapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Employee extends AppCompatActivity {

    LinearLayout customerList;
    ArrayList<Transaction> transactions;
    EditText etCustomername;
    DatabaseHelper databaseHelper;
    Transaction data;
    int spinnerPosition = 0;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
        intent = getIntent();
        databaseHelper = new DatabaseHelper(Employee.this);
        try {
            String[] employeeData = databaseHelper.retrieveData(DatabaseHelper.TBLEMPLOYEE, intent.getIntExtra("id", 0))[0];
            ((TextView) findViewById(R.id.tvEmployeeWholeName)).setText(employeeData[3].toUpperCase());
            ((TextView) findViewById(R.id.tvEmployeeeUseername)).setText(intent.getStringExtra("username"));
        } catch (Exception e){
            displayInfo(e.toString());
        }
        etCustomername = (EditText) findViewById(R.id.etCustomerName);
        customerList = (LinearLayout) findViewById(R.id.llCustomerList);
        transactions = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.employee_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.about:
                about();
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
            default:
                displayInfo(item.getTitle().toString());
        }
        return true;
    }

    public void updateTransactions(){
        customerList.removeAllViews();
        for (Transaction transaction : transactions) {
            addCustomer(transaction.name);
        }
    }

    private void addCustomer(String customerName){
        etCustomername.setText("");
        Button btn = new Button(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 120);
        params.setMargins(15, 5, 15, 5);
        btn.setTextColor(getResources().getColor(R.color.colorLogo));
        btn.setTextSize(20f);
        btn.setLayoutParams(params);
        try {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Employee.this);
                    for (Transaction transaction : transactions) {
                        if(transaction.name.equals(customerName)){
                            data = transaction;
                        }
                    }
                    builder.setMessage(data.name);
                    builder.setPositiveButton("ADD ITEM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            AlertDialog.Builder builderItem = new AlertDialog.Builder(Employee.this);
                            View viewItem = Employee.this.getLayoutInflater().inflate(R.layout.item_picker, null);
                            Spinner spinnerItem = (Spinner) viewItem.findViewById(R.id.spinItem);
                            String[] itemListFromDb = databaseHelper.itemList(1);
                            String[] itemPriceFromDb = databaseHelper.itemList(5);
                            String[] itemStockFromDb = databaseHelper.itemList(2);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Employee.this, R.layout.list_items_item, R.id.tvItemListItem, itemListFromDb);
                            spinnerItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    spinnerPosition = i;
                                    ((TextView) viewItem.findViewById(R.id.tvItemPriceChange)).setText(itemPriceFromDb[i]);
                                    ((TextView) viewItem.findViewById(R.id.tvItemStock)).setText(itemStockFromDb[i]);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                            spinnerItem.setAdapter(adapter);
                            builderItem.setView(viewItem);

                            ((EditText) viewItem.findViewById(R.id.etQuantity)).addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                    try {
                                        String quantity = ((EditText) viewItem.findViewById(R.id.etQuantity)).getText().toString();
                                        double total = Integer.valueOf(quantity) * Double.parseDouble(itemPriceFromDb[spinnerPosition]);
                                        ((TextView) viewItem.findViewById(R.id.tvCalculate)).setText("" + total);
                                        ((TextView) viewItem.findViewById(R.id.tvItemStock)).setText("" + (Integer.parseInt(itemStockFromDb[spinnerPosition]) - Integer.parseInt(quantity)));
                                    } catch (Exception e){
                                    }
                                }

                                @Override
                                public void afterTextChanged(Editable editable) {

                                }
                            });
                            builderItem.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    try {
                                        int quantity = Integer.parseInt(((EditText) viewItem.findViewById(R.id.etQuantity)).getText().toString());
                                        if(quantity > 0){
                                            data.addListItems(itemListFromDb[spinnerPosition], quantity, Double.parseDouble(itemPriceFromDb[spinnerPosition]));
                                            orderSummary(data);
                                        }
                                    }catch (Exception e){
                                        displayInfo("No Input");
                                    }

                                }
                            });

                            builderItem.show();
                        }
                    });
                    builder.setNegativeButton("EDIT SERVICES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            AlertDialog.Builder builderItem = new AlertDialog.Builder(Employee.this);
                            View viewMachine = Employee.this.getLayoutInflater().inflate(R.layout.service_picker, null);
                            builderItem.setView(viewMachine);
                            builderItem.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    try{
                                        data.isWashing = ((CheckBox) viewMachine.findViewById(R.id.cbWashingCust)).isChecked();
                                        data.isDrying = ((CheckBox) viewMachine.findViewById(R.id.cbDryingCust)).isChecked();
                                        String[] availablemachine = databaseHelper.getAvailable(data.isWashing, data.isDrying);
                                        if(availablemachine[0].equals("NO DATA!")){
                                            displayInfo("No available machine");
                                        }
                                        data.machineIdentifier = availablemachine[0];
                                        orderSummary(data);
                                    } catch (Exception e){

                                    }

                                }
                            });
                            builderItem.show();
                        }
                    });
                    builder.setNeutralButton("VIEW TRANSACTION", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            orderSummary(data);
                        }
                    });
                    builder.show();
                }
            });
        }catch (Exception e){
            displayInfo("Transaction Error");
        }
        btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Employee.this);
                builder.setMessage("Delete " + customerName + " in Customers list?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for (Transaction transaction : transactions) {
                            if(transaction.name.equals(((Button) view).getText().toString())){
                                transactions.remove(transaction);
                                transaction = null;
                                updateTransactions();
                            }
                        }
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
                return true;
            }
        });
        btn.setText(customerName);
        btn.setBackgroundResource(R.drawable.dark_rounded_corner);
        customerList.addView(btn);
    }

    private void orderSummary(Transaction data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Employee.this);
        View view = Employee.this.getLayoutInflater().inflate(R.layout.view_transact, null);
        builder.setView(view);
        try {
            ((TextView) view.findViewById(R.id.tvCustomerNameTransact)).setText(data.name);
            ((TextView) view.findViewById(R.id.tvDateTransact)).setText(data.dateTime);
            String strName = "";
            String strPrice = "";
            String strQuantity = "";
            double total = 0;
            for (Transaction.ItemInfo listItem : data.listItems) {
                strName += listItem.getItemname().toUpperCase() + "\n";
                strPrice += listItem.getPrice() + "\n";
                strQuantity += listItem.getItemQuantity() + "\n";
                total += listItem.getPrice() * listItem.getItemQuantity();
            }
            ((TextView) view.findViewById(R.id.tvItemNameTransact)).setText(strName);
            ((TextView) view.findViewById(R.id.tvItemPriceTransact)).setText(strPrice);
            ((TextView) view.findViewById(R.id.tvItemQuantityTransact)).setText(strQuantity);
            ((TextView) view.findViewById(R.id.tvTotalTransact)).setText("" + total);
        } catch (Exception e){
            displayInfo("Error");
        }
        builder.show();
    }

    public void onClickAddCustomer(View view){
        String name = ((EditText) findViewById(R.id.etCustomerName)).getText().toString();
        if(name.equals("")){
            displayInfo("Customer name is empty");
        } else {
            for (Transaction transaction : transactions) {
                if(name.equals(transaction.name)){
                    displayInfo("Customer already exists");
                    return;
                }
            }
            Date date = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            String dateTime = dateFormat.format(date);
            transactions.add(new Transaction(name, getIntent().getIntExtra("id", 0), dateTime));
            updateTransactions();
        }
    }

    private void displayInfo(String msg){
        Toast toast = Toast.makeText(getApplicationContext(), "\n" + msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.END | Gravity.TOP, 0, 75);
        toast.show();
    }

    private void about(){
        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.about, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(Employee.this);
        builder.setView(view);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.show();
    }

    private void changePassword(){
        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.change_password, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(Employee.this);
        builder.setView(view);
        builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String oldPass = ((EditText) view.findViewById(R.id.etOldPassword)).getText().toString();
                String newPass = ((EditText) view.findViewById(R.id.etNewPass)).getText().toString();
                String confirm = ((EditText) view.findViewById(R.id.etConfirmNewPAss)).getText().toString();

                if(newPass.equals(confirm)){
                    if(databaseHelper.changePassword(DatabaseHelper.TBLEMPLOYEE, getIntent().getIntExtra("id", 0), oldPass, confirm)){
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

}