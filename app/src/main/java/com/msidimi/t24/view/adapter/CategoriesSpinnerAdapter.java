package com.msidimi.t24.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.msidimi.t24.R;
import com.msidimi.t24.model.Category;

import java.util.ArrayList;

/**
 * Created by mucahit on 18/09/16.
 */
public class CategoriesSpinnerAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private ArrayList<Category> categories;

    public CategoriesSpinnerAdapter(Context context, ArrayList<Category> categories) {
        this.categories = categories;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (categories != null)
            return categories.size();
        else
            return 0;
    }

    @Override
    public Category getItem(int position) {
        if (categories != null)
            return categories.get(position);
        else
            return null;
    }

    @Override
    public long getItemId(int position) {
        if (categories != null)
            return categories.get(position).getCategoryID();
        else
            return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.item_category, null);
        ((TextView) convertView.findViewById(R.id.tv_category)).setText(getItem(position).getName());
        return convertView;
    }
}
