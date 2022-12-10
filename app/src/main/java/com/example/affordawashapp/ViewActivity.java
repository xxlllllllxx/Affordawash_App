package com.example.affordawashapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collection;

public class ViewActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    TextView tvLabel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        databaseHelper = new DatabaseHelper(ViewActivity.this);
        ListView list = (ListView) findViewById(R.id.listView);
        String label = "LIST";
        tvLabel = (TextView) findViewById(R.id.tvLabel);
        switch (getIntent().getStringExtra("table")){
            case DatabaseHelper.TBLEMPLOYEE:
                label = "EMPLOYEE LIST";
                String[][] dataE = databaseHelper.retrieveData(DatabaseHelper.TBLEMPLOYEE, 0);
                if(dataE[0][0].equals("NO DATA!")){
                    finish();
                } else {
                    String[] idE = new String[dataE.length];
                    try{
                    for (int i = 0; i < dataE.length; i++) {
                        idE[i] = dataE[i][0];
                        dataE[i][2] = String.valueOf(databaseHelper.getCountSame(DatabaseHelper.TBLCUSTOMER, DatabaseHelper.customerFields[2], String.valueOf(idE[i])));
                    }
                        CustomViewAdapter adapter = new CustomViewAdapter(ViewActivity.this, R.layout.list_employee, DatabaseHelper.TBLEMPLOYEE, dataE, idE);
                        list.setAdapter(adapter);
                    }catch (Exception e){
                        displayInfo("Error Occured!");
                    }
                }
                break;
            case DatabaseHelper.TBLITEM:
                label = "ITEM LIST";
                String[][] dataI = databaseHelper.retrieveData(DatabaseHelper.TBLITEM, 0);
                if(dataI[0][0].equals("NO DATA!")){
                    finish();
                } else {
                    String[] idI = new String[dataI.length];
                    try {
                    for (int i = 0; i < dataI.length; i++) {
                        idI[i] = dataI[i][0];
                    }
                        CustomViewAdapter adapter = new CustomViewAdapter(ViewActivity.this, getIntent().getIntExtra("resource", -1), DatabaseHelper.TBLITEM, dataI, idI);
                        list.setAdapter(adapter);
                    } catch (Exception e){
                        displayInfo("Error Occured!");
                    }
                }
                break;
            case DatabaseHelper.TBLMACHINE:
                label = "MACHINE LIST";
                String[][] dataM = databaseHelper.retrieveData(DatabaseHelper.TBLMACHINE, 0);
                if(dataM[0][0].equals("NO DATA!")){
                    finish();
                } else {
                    String[] idM = new String[dataM.length];
                    try {
                    for (int i = 0; i < dataM.length; i++) {
                        idM[i] = dataM[i][0];
                    }
                        CustomViewAdapter adapter = new CustomViewAdapter(ViewActivity.this, getIntent().getIntExtra("resource", -1), DatabaseHelper.TBLMACHINE, dataM, idM);
                        list.setAdapter(adapter);
                    } catch (Exception e){
                        displayInfo("Error Occured!");
                    }
                }
                break;
            case DatabaseHelper.TBLCUSTOMER:
                label = "CUSTOMER TRANSACTION LIST";
                    String[][] dataC = databaseHelper.retrieveData(DatabaseHelper.TBLCUSTOMER, 0);
                    if (dataC[0][0].equals("NO DATA!")) {
                        finish();
                    } else {
                        String[] idC = new String[dataC.length];
                        try {
                        for (int i = 0; i < dataC.length; i++) {
                            idC[i] = dataC[i][0];
                            dataC[i][2] = databaseHelper.getColumnField(DatabaseHelper.TBLEMPLOYEE, DatabaseHelper.employeeFields[1], dataC[i][2]);
                            dataC[i][3] = databaseHelper.unlistMachine(dataC[i][3]);
                            dataC[i][4] = databaseHelper.unlistItem(dataC[i][4]);
                        }
                            CustomViewAdapter adapter = new CustomViewAdapter(ViewActivity.this, getIntent().getIntExtra("resource", -1), DatabaseHelper.TBLCUSTOMER, dataC, idC);
                            list.setAdapter(adapter);
                        } catch (Exception e) {
                            displayInfo("Error Occured!");
                        }
                    }
                break;
            default:
                displayInfo("Error Occured!");
        }
        tvLabel.setText(label);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                final View view = v;
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewActivity.this);
                switch (getIntent().getStringExtra("table")){
                    case DatabaseHelper.TBLEMPLOYEE:
                        builder.setMessage("Employee record actions");
                        builder.setPositiveButton("SAVE CHANGES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try{
                                    String idd = ((TextView) view.findViewById(R.id.tvIdentifierE)).getText().toString();
                                    String wholename = ((TextView) view.findViewById(R.id.tvEmWholename)).getText().toString();
                                    String username = ((TextView) view.findViewById(R.id.tvEmUsername)).getText().toString();
                                    String salary = ((EditText) view.findViewById(R.id.etEmSalary)).getText().toString();
                                    String password = databaseHelper.getColumnField(DatabaseHelper.TBLEMPLOYEE, DatabaseHelper.employeeFields[2], String.valueOf(idd));
                                    if(databaseHelper.updateData(DatabaseHelper.TBLEMPLOYEE, Integer.parseInt(idd), new String[]{username, password, wholename, salary})){
                                        displayInfo("Employee record saved");
                                        finish();
                                        overridePendingTransition(0, 0);
                                        startActivity(getIntent());
                                        overridePendingTransition(0, 0);
                                    } else {
                                        displayInfo("Failed to save record changes");
                                    }
                                } catch (Exception e){
                                    displayInfo("Failed to save changes");
                                }
                            }
                        });
                        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(getIntent());
                                overridePendingTransition(0, 0);
                            }
                        });
                        builder.setNeutralButton("DELETE RECORD", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try{
                                    int id = Integer.parseInt(((TextView) view.findViewById(R.id.tvIdentifierE)).getText().toString());
                                    if(databaseHelper.deleteData(DatabaseHelper.TBLEMPLOYEE, id)){
                                        displayInfo("Record Deletion Successful");
                                        finish();
                                        overridePendingTransition(0, 0);
                                        startActivity(getIntent());
                                        overridePendingTransition(0, 0);
                                    } else {
                                        displayInfo("Record Deletion Failed");
                                    }
                                } catch (Exception e){
                                    displayInfo("Failed to delete record.");
                                }
                            }
                        });
                        break;
                    case DatabaseHelper.TBLITEM:
                        builder.setMessage("Item record actions");
                        builder.setPositiveButton("SAVE CHANGES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try{
                                    String idd = ((TextView) view.findViewById(R.id.tvIdentifierI)).getText().toString();
                                    String name = ((TextView) view.findViewById(R.id.tvItemName)).getText().toString();
                                    String cost = ((EditText) view.findViewById(R.id.etLsItemCost)).getText().toString();
                                    String quantity = ((EditText) view.findViewById(R.id.etLsItemQuantity)).getText().toString();
                                    String lprice = ((EditText) view.findViewById(R.id.etLsItemLP)).getText().toString();
                                    String pprice = ((EditText) view.findViewById(R.id.etLsItemSP)).getText().toString();
                                    if(databaseHelper.updateData(DatabaseHelper.TBLITEM, Integer.parseInt(idd), new String[]{name, quantity, cost, lprice, pprice})){
                                        displayInfo("Item record saved");
                                        finish();
                                        overridePendingTransition(0, 0);
                                        startActivity(getIntent());
                                        overridePendingTransition(0, 0);
                                    } else {
                                        displayInfo("Failed to save item changes");
                                    }
                                } catch (Exception e){
                                    displayInfo("Failed to save changes");
                                }
                            }
                        });
                        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(getIntent());
                                overridePendingTransition(0, 0);
                            }
                        });
                        builder.setNeutralButton("DELETE ITEM", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try{
                                    int id = Integer.parseInt(((TextView) view.findViewById(R.id.tvIdentifierI)).getText().toString());
                                    if(databaseHelper.deleteData(DatabaseHelper.TBLITEM, id)){
                                        displayInfo("Item Deleted");
                                        finish();
                                        overridePendingTransition(0, 0);
                                        startActivity(getIntent());
                                        overridePendingTransition(0, 0);
                                    } else {
                                        displayInfo("Item Deletion Failed");
                                    }
                                } catch (Exception e){
                                    displayInfo("Failed to delete Item.");
                                }
                            }
                        });
                        break;
                    case DatabaseHelper.TBLMACHINE:
                        builder.setMessage("Machine record actions");
                        builder.setPositiveButton("SAVE CHANGES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try{
                                    String idd = ((TextView) view.findViewById(R.id.tvIdentifierM)).getText().toString();
                                    String name = ((TextView) view.findViewById(R.id.tvMachineName)).getText().toString();
                                    boolean w = ((Switch) view.findViewById(R.id.swWashing)).isChecked();
                                    String washing = (w)? "1":"0";
                                    boolean d = ((Switch) view.findViewById(R.id.swDrying)).isChecked();
                                    String drying = (d)? "1":"0";
                                    String washingP = (w)? ((EditText) view.findViewById(R.id.etWashingPrice)).getText().toString(): "0.0";
                                    String dryingP = (d)? ((EditText) view.findViewById(R.id.etDryingPrice)).getText().toString(): "0.0";

                                    if(databaseHelper.updateData(DatabaseHelper.TBLMACHINE, Integer.parseInt(idd), new String[]{name, washing, drying, washingP, dryingP})){
                                        displayInfo("Machine details saved");
                                        finish();
                                        overridePendingTransition(0, 0);
                                        startActivity(getIntent());
                                        overridePendingTransition(0, 0);
                                    } else {
                                        displayInfo("Failed to save record changes");
                                    }
                                } catch (Exception e){
                                    displayInfo(e.toString());
                                }
                            }
                        });
                        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(getIntent());
                                overridePendingTransition(0, 0);
                            }
                        });
                        builder.setNeutralButton("REMOVE MACHINE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try{
                                    int id = Integer.parseInt(((TextView) view.findViewById(R.id.tvIdentifierM)).getText().toString());
                                    if(databaseHelper.deleteData(DatabaseHelper.TBLMACHINE, id)){
                                        displayInfo("Machine Removed");
                                        finish();
                                        overridePendingTransition(0, 0);
                                        startActivity(getIntent());
                                        overridePendingTransition(0, 0);
                                    } else {
                                        displayInfo("Machine Deletion Failed");
                                    }
                                } catch (Exception e){
                                    displayInfo("Failed to remove Item.");
                                }
                            }
                        });
                        break;
                    default:
                        break;
                }
                builder.show();
                return true;
            }
        });
    }
    
    private void displayInfo(String msg){
        Toast toast = Toast.makeText(getApplicationContext(), "\n" + msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.END | Gravity.TOP, 0, 75);
        toast.show();
    }
}