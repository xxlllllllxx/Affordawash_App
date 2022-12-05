package com.example.affordawashapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

public class Employee extends AppCompatActivity {

    LinearLayout itemList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
        itemList = (LinearLayout) findViewById(R.id.llItemList);
    }

    private void addItem(String itemname, String price){

    }

}