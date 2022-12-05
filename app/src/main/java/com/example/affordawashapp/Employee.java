package com.example.affordawashapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Employee extends AppCompatActivity {

    LinearLayout customerList;
    ArrayList<Transaction> transactions;
    EditText etCustomername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
        etCustomername = (EditText) findViewById(R.id.etCustomerName);
        customerList = (LinearLayout) findViewById(R.id.llCustomerList);
        transactions = new ArrayList<>();
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
                    builder.setMessage(customerName);
                    builder.setPositiveButton("ADD ITEM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            AlertDialog.Builder builderItem = new AlertDialog.Builder(Employee.this);
                            View viewItem = Employee.this.getLayoutInflater().inflate(R.layout.item_picker, null);
                            builderItem.setView(viewItem);
                            builderItem.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    orderSummary();
                                }
                            });
                            builderItem.show();
                        }
                    });
                    builder.setNegativeButton("ADD SERVICES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            AlertDialog.Builder builderItem = new AlertDialog.Builder(Employee.this);
                            View viewItem = Employee.this.getLayoutInflater().inflate(R.layout.service_picker, null);
                            builderItem.setView(viewItem);
                            builderItem.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    orderSummary();
                                }
                            });
                            builderItem.show();
                        }
                    });
                    builder.setNeutralButton("VIEW TRANSACTION", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            orderSummary();
                        }
                    });
                    builder.show();
                }
            });
        }catch (Exception e){
            displayInfo(e.toString());
        }
        btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Employee.this);
                builder.setMessage("Delete this customer?");
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

    private void orderSummary() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Employee.this);
        View view = Employee.this.getLayoutInflater().inflate(R.layout.view_transact, null);
        builder.setView(view);
        builder.show();
    }

    public void onClickAddCustomer(View view){
        String name = ((EditText) findViewById(R.id.etCustomerName)).getText().toString();
        if(name.equals("")){
            displayInfo("Customer name is empty");
        } else {
            transactions.add(new Transaction(name));
            updateTransactions();
        }
    }

    private void displayInfo(String msg){
        Toast toast = Toast.makeText(getApplicationContext(), "\n" + msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.END | Gravity.TOP, 0, 75);
        toast.show();
    }

}