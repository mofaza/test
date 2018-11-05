package com.example.elias.shoppinglist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import java.util.List;
import java.util.zip.Inflater;

public class ShoppingListAdapter extends ArrayAdapter<ShoppingItem> {

    public ShoppingListAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View result = convertView;
        if (result == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            result = inflater.inflate(R.layout.shopping_item, null);
        }

        CheckBox checkbox = result.findViewById(R.id.shopping_item);
        ShoppingItem item = getItem(position);
        checkbox.setText(item.getText());
        checkbox.setChecked(item.isChecked());
        return result;
    }
}
