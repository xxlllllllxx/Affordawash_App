package com.example.affordawashapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collection;

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
        this.data = reverse(data);
    }
    
    private String[][] reverse(String[][] raw){
        String[][] tmp = new String[raw.length][raw[0].length];
        int ctr = 0;
        for (int i = (raw.length-1); i >= 0; i--) {
            tmp[i] = raw[ctr];
            ctr++;
        }
        return tmp;
    }
    
    
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(res, null);

        switch (tbl){
            case DatabaseHelper.TBLITEM:
                ((TextView) convertView.findViewById(R.id.tvIdentifierI)).setText(data[position][0]);
                ((TextView) convertView.findViewById(R.id.tvItemName)).setText(data[position][1]);
                ((EditText) convertView.findViewById(R.id.etLsItemQuantity)).setText(data[position][2]);
                ((EditText) convertView.findViewById(R.id.etLsItemCost)).setText(data[position][3]);
                ((EditText) convertView.findViewById(R.id.etLsItemLP)).setText(data[position][4]);
                ((EditText) convertView.findViewById(R.id.etLsItemSP)).setText(data[position][5]);
                break;
            case DatabaseHelper.TBLEMPLOYEE:
                ((TextView) convertView.findViewById(R.id.tvIdentifierE)).setText(data[position][0]);
                ((TextView) convertView.findViewById(R.id.tvEmUsername)).setText(data[position][1]);
                ((TextView) convertView.findViewById(R.id.tvEmWholename)).setText(data[position][3]);
                ((TextView) convertView.findViewById(R.id.tvCustomerCount)).setText(data[position][2]);
                ((EditText) convertView.findViewById(R.id.etEmSalary)).setText(data[position][4]);
                break;
            case DatabaseHelper.TBLMACHINE:
                ((TextView) convertView.findViewById(R.id.tvIdentifierM)).setText(data[position][0]);
                ((TextView) convertView.findViewById(R.id.tvMachineName)).setText(data[position][1]);
                ((Switch) convertView.findViewById(R.id.swWashing)).setChecked(data[position][2].equals("1"));
                ((Switch) convertView.findViewById(R.id.swDrying)).setChecked(data[position][3].equals("1"));
                ((EditText) convertView.findViewById(R.id.etWashingPrice)).setText(data[position][4]);
                ((EditText) convertView.findViewById(R.id.etDryingPrice)).setText(data[position][5]);
                break;
            case DatabaseHelper.TBLCUSTOMER:
                ((TextView) convertView.findViewById(R.id.tvIdentifierC)).setText(data[position][0]);
                ((TextView) convertView.findViewById(R.id.tvCustomerAlias)).setText(data[position][1]);
                ((TextView) convertView.findViewById(R.id.tvCEmployeeId)).setText(data[position][2]);
                ((TextView) convertView.findViewById(R.id.tvMachineUsed)).setText(data[position][3]);
                ((TextView) convertView.findViewById(R.id.tvItemsBought)).setText(data[position][4]);
                ((TextView) convertView.findViewById(R.id.tvTotal)).setText(data[position][5]);
                ((TextView) convertView.findViewById(R.id.tvDateTime)).setText(data[position][6]);
                break;
            default:
        }
        return convertView;
    }
    
}
