package com.example.affordawashapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

public class Employee extends AppCompatActivity {

    LinearLayout customerList;
    ArrayList<Transaction> transactions;
    EditText etCustomername;
    DatabaseHelper databaseHelper;
    Transaction data;
    int spinnerPosition = 0;
    int spinnerServicePosition = 0;
    double change;
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
        } catch (Exception e) {
            displayInfo(e.toString());
        }
        etCustomername = findViewById(R.id.etCustomerName);
        customerList = findViewById(R.id.llCustomerList);
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
            case R.id.viewTransactions:
                if(databaseHelper.getCount(DatabaseHelper.TBLCUSTOMER, "id") > 0) {
                    Intent intent = new Intent(Employee.this, ViewActivity.class);
                    intent.putExtra("table", DatabaseHelper.TBLCUSTOMER);
                    intent.putExtra("resource", R.layout.list_customer);
                    startActivity(intent);
                } else {
                    displayInfo("Transaction Table is Empty");
                }
                break;
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

    private void addCustomer(String n){
        final String customerName = n;
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
                    builder.setMessage(data.name.toUpperCase());
                    builder.setPositiveButton("ADD ITEM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            AlertDialog.Builder builderItem = new AlertDialog.Builder(Employee.this);
                            final View viewItem = Employee.this.getLayoutInflater().inflate(R.layout.item_picker, null);
                            Spinner spinnerItem = viewItem.findViewById(R.id.spinItem);
                            final String[] itemIdFromDb = databaseHelper.itemList(0);
                            final String[] itemListFromDb = databaseHelper.itemList(1);
                            final String[] itemPriceFromDb = databaseHelper.itemList(5);
                            final String[] itemStockFromDb = databaseHelper.itemList(2);
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
                                        if(Integer.parseInt(((TextView) viewItem.findViewById(R.id.tvItemStock)).getText().toString()) >= 0) {
                                            int quantity = Integer.parseInt(((EditText) viewItem.findViewById(R.id.etQuantity)).getText().toString());
                                            if (quantity > 0) {
                                                data.addListItems(itemListFromDb[spinnerPosition], quantity, Double.parseDouble(itemPriceFromDb[spinnerPosition]), Integer.parseInt(itemIdFromDb[spinnerPosition]));
                                                orderSummary(data);
                                            }
                                        } else {
                                            displayInfo("Item stock not enough");
                                        }
                                    }catch (Exception e){
                                        displayInfo("No Input");
                                    }

                                }
                            });

                            builderItem.show();
                        }
                    });
                    builder.setNegativeButton("ADD SERVICES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            AlertDialog.Builder builderItem = new AlertDialog.Builder(Employee.this);
                            final View viewService = Employee.this.getLayoutInflater().inflate(R.layout.service_picker, null);
                            Spinner spinnerService = viewService.findViewById(R.id.spinService);
                            final String[] serviceMachinesId = databaseHelper.machineList(0);
                            final String[] serviceListFromDb = databaseHelper.machineList(1);
                            final String[] serviceMachineWash = databaseHelper.machineList(2);
                            final String[] serviceMachineDry= databaseHelper.machineList(3);
                            final String[] servicewashPrice = databaseHelper.machineList(4);
                            final String[] servicedryPrice = databaseHelper.machineList(5);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Employee.this, R.layout.list_items_item, R.id.tvItemListItem, serviceListFromDb);
                            spinnerService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    spinnerServicePosition = i;
                                    ((CheckBox) viewService.findViewById(R.id.cbWashingCust)).setChecked(serviceMachineWash[i].equals("1"));
                                    ((CheckBox) viewService.findViewById(R.id.cbDryingCust)).setChecked(serviceMachineDry[i].equals("1"));
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                            spinnerService.setAdapter(adapter);
                            builderItem.setView(viewService);
                            builderItem.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    try{
                                        data.isWashing = ((CheckBox) viewService.findViewById(R.id.cbWashingCust)).isChecked();
                                        data.isDrying = ((CheckBox) viewService.findViewById(R.id.cbDryingCust)).isChecked();
                                        data.wash = Double.parseDouble(servicewashPrice[spinnerServicePosition]);
                                        data.dry = Double.parseDouble(servicedryPrice[spinnerServicePosition]);
                                        data.machineId = Integer.parseInt(serviceMachinesId[spinnerServicePosition]);
                                        orderSummary(data);
                                    } catch (Exception e) {
                                        displayInfo(e.toString());
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
            public boolean onLongClick(View v) {
                final View view = v;
                AlertDialog.Builder builder = new AlertDialog.Builder(Employee.this);
                builder.setMessage("Delete " + customerName + " in Customers list?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            for (Iterator<Transaction> it = transactions.iterator();  it.hasNext();) {
                                Transaction transaction = it.next();
                                if(transaction.name.equals(((Button) view).getText().toString())){
                                    it.remove();
                                }
                            }
                            updateTransactions();
                            
                        }catch (Exception e){
                            displayInfo("Error");
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

    private void orderSummary(Transaction d) {
        final Transaction data = d;
        AlertDialog.Builder builder = new AlertDialog.Builder(Employee.this);
        final View view = Employee.this.getLayoutInflater().inflate(R.layout.view_transact, null);
        final EditText etAmount = view.findViewById(R.id.etTenderedAmount);
        change = 0;
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
            ((CheckBox) view.findViewById(R.id.cbWashTransact)).setChecked(data.isWashing);
            ((CheckBox) view.findViewById(R.id.cbDryingTransact)).setChecked(data.isDrying);
            ((TextView) view.findViewById(R.id.tvWashingPrice)).setText("" +data.wash);
            ((TextView) view.findViewById(R.id.tvDryingPrice)).setText("" +data.dry);
            ((TextView) view.findViewById(R.id.tvTotalServiceCost)).setText("" + (data.wash + data.dry));
            final double grandtotal = total + data.wash + data.dry;
            ((TextView) view.findViewById(R.id.tvGrandTotal)).setText("" + grandtotal);
            data.payment = grandtotal;
    
            etAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if(etAmount.getText().length() >= 1) {
                        change = (Double.parseDouble(etAmount.getText().toString()) - grandtotal);
                        if(change > 0){
                            ((TextView) view.findViewById(R.id.tvChange)).setText("" + change);
                        } else {
                            ((TextView) view.findViewById(R.id.tvChange)).setText("0");
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        } catch (Exception e){
            displayInfo("Error");
        }
        builder.setPositiveButton("BACK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNeutralButton("COMPLETE PAYMENT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (change < 0 || data.payment < 1 || etAmount.getText().toString().isEmpty()) {
                    displayInfo("Transaction failed");
                } else {
                    AlertDialog.Builder areyousure = new AlertDialog.Builder(Employee.this);
                    areyousure.setMessage("Are you sure you want to complete this transaction? ");
                    areyousure.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            try {
                                String machine = data.machineId + " " + (data.dry + data.wash);
                                String items = "";
                                boolean flag = true;
                                for (Transaction.ItemInfo listItem : data.listItems) {
                                    flag = databaseHelper.itemRemoval(listItem.getItemId(), listItem.getItemQuantity());
                                    items += listItem.getItemId() + " " + listItem.getItemQuantity() + " " + (listItem.getItemQuantity() * listItem.getPrice()) + ":";
                                }
                                if (flag && databaseHelper.createData(DatabaseHelper.TBLCUSTOMER, new String[]{data.name, String.valueOf(intent.getIntExtra("id", 0)), machine, items, String.valueOf(data.payment), data.dateTime})) {
                                    displayInfo("Transaction complete");
                                    transactions.remove(data);
                                    updateTransactions();
                                } else {
                                    displayInfo("Transaction history not saved");
                                }
                            } catch (Exception e){
                                displayInfo(e.toString());
                            }
                        }
                    });
                    areyousure.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    areyousure.show();
                }
            }
        });
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
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
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