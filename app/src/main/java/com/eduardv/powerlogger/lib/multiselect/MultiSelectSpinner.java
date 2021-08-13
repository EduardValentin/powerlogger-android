package com.eduardv.powerlogger.lib.multiselect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.eduardv.powerlogger.R;
import com.eduardv.powerlogger.lib.SelectableItem;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MultiSelectSpinner extends TextInputEditText implements DialogInterface.OnMultiChoiceClickListener {
    private static final String CONFIRM_TXT = "OK";
    private static final String NEGATIVE_TXT = "Close";

    private List<? extends SelectableItem> items;
    private MutableLiveData<List<Boolean>> selectedItemsLiveData;
    private DialogInterface.OnMultiChoiceClickListener onMultiChoiceClickListener;
    private Consumer<List<? extends SelectableItem>> onConfirmConsumer;
    private String title;

    public MultiSelectSpinner(Context context) {
        super(context);
        setup();
    }

    public MultiSelectSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public MultiSelectSpinner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup();
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public void setOnConfirmConsumer(Consumer<List<? extends SelectableItem>> onConfirmConsumer) {
        this.onConfirmConsumer = onConfirmConsumer;
    }

    public DialogInterface.OnMultiChoiceClickListener getOnMultiChoiceClickListener() {
        return onMultiChoiceClickListener;
    }

    public void setOnMultiChoiceClickListener(DialogInterface.OnMultiChoiceClickListener onMultiChoiceClickListener) {
        this.onMultiChoiceClickListener = onMultiChoiceClickListener;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setItems(List<? extends SelectableItem> items) {
        if (items == null) return;

        this.items = items;

        List<Boolean> sel = new ArrayList<>();
        this.items.forEach(item -> sel.add(false));

        selectedItemsLiveData.setValue(sel);
    }


    /*
     * Sets the selection value of an item and returns true if operation succeeded or false otherwise
     * */
    public boolean setSelectedAtIndex(int index, boolean selected) {
        try {
            updateSelected(index, selected);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public List<? extends SelectableItem> getSelectedItems() {
        List<SelectableItem> selectedItemsList = new ArrayList<>();

        for (int i = 0; i < items.size(); ++i) {
            if (isSelectedAtIndex(i)) {
                selectedItemsList.add(items.get(i));
            }
        }

        return selectedItemsList;
    }

    public Boolean isSelectedAtIndex(int i) {
        return selectedItemsLiveData.getValue().get(i);
    }

    public void setSelectedItemsData(List<Boolean> selected) {
        this.selectedItemsLiveData.setValue(selected);
    }

    public LiveData<List<Boolean>> getSelectedItemsLiveData() {
        return selectedItemsLiveData;
    }

    @Override
    public void onClick(DialogInterface dialog, int idx, boolean isChecked) {
        if (this.onMultiChoiceClickListener != null) {
            this.onMultiChoiceClickListener.onClick(dialog, idx, isChecked);
        }

        if (selectedItemsLiveData != null && idx < selectedItemsLiveData.getValue().size()) {
            updateSelected(idx, isChecked);
        }
    }

    public void performClick(View v) {
        final AlertDialog.Builder builder = new MaterialAlertDialogBuilder(getContext());

        builder.setTitle(title);

        builder.setPositiveButton(CONFIRM_TXT, (dialogInterface, arg1) -> {
            if (onConfirmConsumer != null) {
                onConfirmConsumer.accept(getSelectedItems());
            }
        });

        builder.setNegativeButton(NEGATIVE_TXT, null);

        if (items.isEmpty()) {
            builder.setMessage("No items to select");
            builder.show();
            return;
        }

        String[] itemNames = new String[items.size()];

        for (int i = 0; i < items.size(); i++) {
            itemNames[i] = items.get(i).getName();
        }

        boolean[] selectedItems = new boolean[items.size()];
        List<Boolean> selectedItemsList = selectedItemsLiveData.getValue();

        for (int i = 0; i < selectedItemsList.size(); i++) {
            selectedItems[i] = isSelectedAtIndex(i);
        }

        builder.setMultiChoiceItems(itemNames, selectedItems, this);

        builder.show();
    }

    private void setup() {
        this.items = new ArrayList<>();

        this.selectedItemsLiveData = new MutableLiveData<>();
        this.selectedItemsLiveData.setValue(new ArrayList<>());

        this.setOnClickListener(this::performClick);
        this.setClickable(true);
        this.setFocusable(false);
    }

    private void updateSelected(int index, Boolean value) {
        List<Boolean> sel = selectedItemsLiveData.getValue();
        sel.set(index, value);
        selectedItemsLiveData.setValue(sel);
    }
}
