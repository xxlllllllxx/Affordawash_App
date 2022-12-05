package com.example.affordawashapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
                    displayInfo("NO DATA IN THIS LIST!");
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
                    displayInfo("NO DATA IN THIS LIST!");
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
                    displayInfo("NO DATA IN THIS LIST!");
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
                        displayInfo("NO DATA IN THIS LIST!");
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
                
            default:
                displayInfo("Error Occured!");
        }
        tvLabel.setText(label);
        
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
    
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ViewActivity.this, "long click", Toast.LENGTH_SHORT).show();
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