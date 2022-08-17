package com.example.iiifa_fan_android.utils;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;


import com.example.iiifa_fan_android.R;

import java.util.ArrayList;
import java.util.List;

public class SpinnerAdapter extends ArrayAdapter {

    private final LayoutInflater mInflater;
    private final Context mContext;
    private final int mResource;
    private List<String> items = new ArrayList<>();

    public SpinnerAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mResource = resource;
        this.items = objects;
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public String getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }


    //overrride method to get custom view of spinner
    private View createItemView(int position, View convertView, ViewGroup parent) {
        final View v = mInflater.inflate(mResource, parent, false);

        TextView textView = v.findViewById(R.id.text1);
//        textView.setTypeface(ResourcesCompat.getFont(mContext, R.font.lato_medium));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f);
//        textView.setHintTextColor(ContextCompat.getColor(mContext, R.color.text_color));
        textView.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        if (position == 0) {
            textView.setHint(items.get(position)); //"Hint to be displayed"
        } else {
            textView.setText(items.get(position));
        }
        return v;
    }
}
