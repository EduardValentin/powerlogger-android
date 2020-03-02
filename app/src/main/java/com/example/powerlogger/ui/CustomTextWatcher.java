package com.example.powerlogger.ui;

import android.text.Editable;
import android.text.TextWatcher;

import java.util.function.Consumer;

public class CustomTextWatcher implements TextWatcher {

    private Consumer<Editable> handler;

    public CustomTextWatcher(Consumer<Editable> handler) {
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
