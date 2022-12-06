package com.example.affordawashapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
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

import java.util.ArrayList;

public class Employee extends AppCompatActivity {

    LinearLayout customerList;
    ArrayList<Transaction> transactions;
    EditText etCustomername;
    DatabaseHelper databaseHelper;
    Transaction data;
    int spinnerPosition = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
        ((TextView) findViewById(R.id.tvEmployeeeUseername)).setText(getIntent().getStringExtra("username"));
        databaseHelper = new DatabaseHelper(Employee.this);
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
                            builderItem.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    try {
                                        int quantity = Integer.parseInt(((EditText) viewItem.findViewById(R.id.etQuantity)).getText().toString());
                                        if(quantity > 0){
                                            data.addListItems(itemListFromDb[spinnerPosition], quantity);
                                            orderSummary(data);
                                        }
                                    }catch (Exception e){
                                        displayInfo(e.toString());
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
        btn.setBackgroundResource(R.drawable.rounded_corner);
        customerList.addView(btn);
    }

    private void orderSummary(Transaction data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Employee.this);
        View view = Employee.this.getLayoutInflater().inflate(R.layout.view_transact, null);
        //builder.setView(view);
        try {
            //builder.setNegative("COMPLETE TRANSACTION
            //builder.setPostive("BACK
            String tmp = data.isWashing + ":Washing " + data.isDrying + ":Drying";
            for (Transaction.PairItem listItem : data.listItems) {
                tmp += listItem.getItemname() + " " + listItem.getItemQuantity() + "\n";
            }
            builder.setMessage(tmp);
        } catch (Exception e){
            displayInfo(e.toString());
        }
        builder.show();
    }

    public void onClickAddCustomer(View view){
        String name = ((EditText) findViewById(R.id.etCustomerName)).getText().toString();
        if(name.equals("")){
            displayInfo("Customer name is empty");
        } else {
            transactions.add(new Transaction(name, getIntent().getIntExtra("id", 0)));
            updateTransactions();
        }
    }

    private void displayInfo(String msg){
        Toast toast = Toast.makeText(getApplicationContext(), "\n" + msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.END | Gravity.TOP, 0, 75);
        toast.show();
    }

}