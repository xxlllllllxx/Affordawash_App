package com.example.affordawashapp;

import android.content.ContentValues;
import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomViewAdapter extends ArrayAdapter<String> {
    Context context;
    String tbl;
    int res;
    String[][] data;
    LayoutInflater inflater;
    
    public CustomViewAdapter(@NonNull Context context, int resource, String tbl, String[][] data, String[] id) {
        super(context, resource, id);
        this.res = resource;
        this.context = context;
        this.tbl = tbl;
        this.data = data;
    }
    
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(res, null);
        switch (tbl){
            case DatabaseHelper.TBLITEM:
                ((TextView) convertView.findViewById(R.id.tvItemName)).setText(data[position][1]);
                ((TextView) convertView.findViewById(R.id.tvItemQuantity)).setText(data[position][2]);
                ((TextView) convertView.findViewById(R.id.tvItemCost)).setText(data[position][3]);
                ((TextView) convertView.findViewById(R.id.tvItemLowestPrice)).setText(data[position][4]);
                ((TextView) convertView.findViewById(R.id.tvItemSellingPrice)).setText(data[position][5]);
                break;
            default:
        }
        return convertView;
    }
}
