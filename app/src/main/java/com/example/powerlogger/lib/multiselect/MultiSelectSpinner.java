package com.example.powerlogger.lib.multiselect;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.ArrayAdapter;

import androidx.appcompat.widget.AppCompatSpinner;

import com.example.powerlogger.lib.MultiSelectItem;

import java.util.ArrayList;
import java.util.List;

public class MultiSelectSpinner extends AppCompatSpinner implements DialogInterface.OnMultiChoiceClickListener {
    private List<MultiSelectItem> items;
    private List<Boolean> selectedItems;
    private ArrayAdapter adapter;

    public MultiSelectSpinner(Context context) {
        super(context);

        adapter = new ArrayAdapter(context,
                android.R.layout.simple_spinner_item);
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

    }
}
