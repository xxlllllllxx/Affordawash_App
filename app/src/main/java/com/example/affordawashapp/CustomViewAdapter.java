package com.example.affordawashapp;

import android.content.ContentValues;
import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Locale;

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
                ((EditText) convertView.findViewById(R.id.etLsItemQuantity)).setText(data[position][2]);
                ((EditText) convertView.findViewById(R.id.etLsItemCost)).setText(data[position][3]);
                ((EditText) convertView.findViewById(R.id.etLsItemLP)).setText(data[position][4]);
                ((EditText) convertView.findViewById(R.id.etLsItemSP)).setText(data[position][5]);
                break;
            case DatabaseHelper.TBLEMPLOYEE:
                ((TextView) convertView.findViewById(R.id.tvEmUsername)).setText(data[position][1]);
                ((TextView) convertView.findViewById(R.id.tvEmWholename)).setText(data[position][3]);
                ((TextView) convertView.findViewById(R.id.tvCustomerCount)).setText(data[position][2]);
                ((TextView) convertView.findViewById(R.id.tvEmSalary)).setText(data[position][4]);
                break;
            case DatabaseHelper.TBLMACHINE:
                ((TextView) convertView.findViewById(R.id.tvMachineName)).setText(data[position][1]);
                ((TextView) convertView.findViewById(R.id.tvWashing)).setText(data[position][3]);
                ((TextView) convertView.findViewById(R.id.tvDrying)).setText(data[position][4]);
                ((TextView) convertView.findViewById(R.id.tvWashingPrice)).setText(data[position][5]);
                ((TextView) convertView.findViewById(R.id.tvDryingPrice)).setText(data[position][6]);
            default:
        }
        return convertView;
    }
}
