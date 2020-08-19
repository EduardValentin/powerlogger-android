package com.eduardv.powerlogger.lib;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import android.content.Context;
import android.util.AttributeSet;
import android.content.DialogInterface;
import android.view.View;

import com.eduardv.powerlogger.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SingleSelectSpinner<T extends SelectableItem> extends TextInputEditText implements DialogInterface.OnClickListener {
    private static final String CONFIRM_TXT = "OK";
    private static final String NEGATIVE_TXT = "Close";

    private List<T> items;
    private String title;

    private Consumer<T> onConfirmConsumer;
    private int selectedItemIndex = -1;

    public SingleSelectSpinner(@NonNull Context context) {
        super(context);
        setup();
    }

    public SingleSelectSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public SingleSelectSpinner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup();
    }

    public void setOnConfirmConsumer(Consumer<T> onConfirmConsumer) {
        this.onConfirmConsumer = onConfirmConsumer;
    }

    public void setItems(List<T> items) {
        this.items = new ArrayList<>(items);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void updateInputValue() {
        if (selectedItemIndex == -1) {
            return;
        }
        this.setText(this.items.get(selectedItemIndex).getName());
    }

    public void setSelectedItem(int selectedItemIndex) {
        this.selectedItemIndex = selectedItemIndex;
        T item = items.get(selectedItemIndex);

        this.setText(item.getName());
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        this.selectedItemIndex = which;
    }

    public void setDisabled(boolean disabled) {
        if (disabled) {
            this.setEnabled(false);
            this.setAlpha(0.1f);
            return;
        }

        this.setEnabled(true);
        this.setAlpha(1f);
    }

    public boolean performClick(View v) {
        final AlertDialog.Builder builder = new MaterialAlertDialogBuilder(getContext());
        builder.setTitle(title);

        if (items.isEmpty()) {
            builder.setMessage("No items to select");
            builder.show();
            return true;
        }

        String[] itemNames = new String[items.size()];

        for (int i = 0; i < items.size(); i++) {
            itemNames[i] = items.get(i).getName();
        }

        builder.setSingleChoiceItems(itemNames, selectedItemIndex, this);

        builder.setPositiveButton(CONFIRM_TXT, ((dialog, which) -> {

            if (selectedItemIndex == -1) {
                return;
            }

            T selected = this.items.get(selectedItemIndex);

            this.setText(selected.getName());

            if (onConfirmConsumer != null) {
                onConfirmConsumer.accept(selected);
            }
        }));

        builder.setNegativeButton(NEGATIVE_TXT, null);

        builder.show();
        return true;
    }

    private void setup() {
        this.setOnClickListener(this::performClick);
        this.setClickable(true);
        this.setFocusable(false);
    }

}
