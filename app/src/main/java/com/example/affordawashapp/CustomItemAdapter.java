package com.example.affordawashapp;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

public class CustomItemAdapter extends ArrayAdapter<String> {
    public CustomItemAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }
}
