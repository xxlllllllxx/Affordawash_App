package com.example.affordawashapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class ViewActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        databaseHelper = new DatabaseHelper(ViewActivity.this);
        ListView list = (ListView) findViewById(R.id.listView);
        switch (getIntent().getStringExtra("table")){
            case DatabaseHelper.TBLEMPLOYEE:
                String[][] dataE = databaseHelper.retrieveData(DatabaseHelper.TBLEMPLOYEE, 0);
                if(dataE[0][0].equals("NO DATA!")){
                    finish();
                } else {
                    String[] idE = new String[dataE.length];
                    for (int i = 0; i < dataE.length; i++) {
                        idE[i] = dataE[i][0];
                        dataE[i][2] = String.valueOf(databaseHelper.getCountSame(DatabaseHelper.TBLCUSTOMER, DatabaseHelper.customerFields[2], String.valueOf(idE[i])));
                    }
                    try{
                        CustomViewAdapter adapter = new CustomViewAdapter(ViewActivity.this, R.layout.list_employee, DatabaseHelper.TBLEMPLOYEE, dataE, idE);
                        list.setAdapter(adapter);
                    }catch (Exception e){
                        finish();
                    }
                }
                break;
            case DatabaseHelper.TBLITEM:
                String[][] dataI = databaseHelper.retrieveData(DatabaseHelper.TBLITEM, 0);
                if(dataI[0][0].equals("NO DATA!")){
                    finish();
                } else {
                    String[] idI = new String[dataI.length];
                    for (int i = 0; i < dataI.length; i++) {
                        idI[i] = dataI[i][0];
                    }
                    try {
                        CustomViewAdapter adapter = new CustomViewAdapter(ViewActivity.this, getIntent().getIntExtra("resource", -1), DatabaseHelper.TBLITEM, dataI, idI);
                        list.setAdapter(adapter);
                    } catch (Exception e){
                        finish();
                    }
                }
                break;
            case DatabaseHelper.TBLMACHINE:
                String[][] dataM = databaseHelper.retrieveData(DatabaseHelper.TBLMACHINE, 0);
                if(dataM[0][0].equals("NO DATA!")){
                    finish();
                } else {
                    String[] idM = new String[dataM.length];
                    for (int i = 0; i < dataM.length; i++) {
                        idM[i] = dataM[i][0];
                    }
                    try {
                        CustomViewAdapter adapter = new CustomViewAdapter(ViewActivity.this, getIntent().getIntExtra("resource", -1), DatabaseHelper.TBLMACHINE, dataM, idM);
                        list.setAdapter(adapter);
                    } catch (Exception e){
                        finish();
                    }
                }
                break;
            default:
                finish();
        }

//        String[][] data = databaseHelper.retrieveData(DatabaseHelper.TBLCUSTOMER, 0);
//        String str = "";
//        for (String[] datum : data) {
//            for (String s : datum) {
//                str += s + " ";
//            }
//            str += "\n";
//        }
//
//        ((TextView) findViewById(R.id.tvTesting)).setText(str);
    }
}