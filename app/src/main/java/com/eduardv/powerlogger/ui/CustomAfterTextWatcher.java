package com.eduardv.powerlogger.ui;

import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.RequiresApi;

import java.util.function.Consumer;

public class CustomAfterTextWatcher implements TextWatcher {

    private Consumer<Editable> handler;

    public CustomAfterTextWatcher(Consumer<Editable> handler) {
        this.handler = handler;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (handler == null) {
            throw new RuntimeException("Handler of text watcher can't be null");
        }
        handler.accept(s);
    }
}
