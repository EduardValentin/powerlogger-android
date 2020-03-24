package com.example.powerlogger.lib.multiselect;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import androidx.appcompat.widget.AppCompatSpinner;

import com.example.powerlogger.R;
import com.example.powerlogger.lib.SelectableItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiSelectSpinner extends AppCompatSpinner implements DialogInterface.OnMultiChoiceClickListener {
    private List<? extends SelectableItem> items;
    private boolean[] selectedItems;
    private ArrayAdapter adapter;
    private String placeholderText;

    private String buildSelectedItemString() {
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;

        for (int i = 0; i < items.size(); ++i) {
            if (selectedItems[i]) {
                if (foundOne) {
                    sb.append(", ");
                }

                foundOne = true;

                sb.append(items.get(i).getName());
            }
        }

        return sb.toString();
    }

    public MultiSelectSpinner(Context context) {
        super(context);
        adapter = new ArrayAdapter(context,
                android.R.layout.simple_spinner_item);
    }

    public MultiSelectSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);

        adapter = new ArrayAdapter(context,
                android.R.layout.simple_spinner_item);
        super.setAdapter(adapter);
    }

    public void setPlaceholderText(String placeholderText) {
        this.placeholderText = placeholderText;
        adapter.clear();
        adapter.add(placeholderText);
    }

    public void setItems(List<? extends SelectableItem> items) {
        this.items = items;
        selectedItems = new boolean[this.items.size()];
        adapter.clear();
        adapter.add("");
        Arrays.fill(selectedItems, false);
    }

    @Override
    public void onClick(DialogInterface dialog, int idx, boolean isChecked) {
        if (selectedItems != null && idx < selectedItems.length) {
            selectedItems[idx] = isChecked;
            adapter.clear();
            adapter.add(buildSelectedItemString());
        } else {
            throw new IllegalArgumentException(
                    "'idx' is out of bounds.");
        }
    }

    public List<SelectableItem> getSelectedItems() {
        List<SelectableItem> selectedItemsList = new ArrayList<>();

        for (int i = 0; i < items.size(); ++i) {
            if (selectedItems[i]) {
                selectedItemsList.add(items.get(i));
            }
        }

        return selectedItemsList;
    }

    @Override
    public boolean performClick() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyAlertDialog);
        String[] itemNames = new String[items.size()];

        for (int i = 0; i < items.size(); i++) {
            itemNames[i] = items.get(i).getName();
        }

        builder.setMultiChoiceItems(itemNames, selectedItems, this);
        builder.setPositiveButton("OK", (dialogInterface, arg1) -> {
            // Do nothing
        });

        builder.show();

        return true;
    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        throw new RuntimeException(
                "setAdapter is not supported by MultiSelectSpinner.");
    }
}
