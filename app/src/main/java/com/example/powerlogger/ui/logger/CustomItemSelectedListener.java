package com.example.powerlogger.ui.logger;

import android.view.View;
import android.widget.AdapterView;

import java.util.function.Consumer;

public class CustomItemSelectedListener<T> implements AdapterView.OnItemSelectedListener {
    private Consumer<T> consumer;

    public CustomItemSelectedListener(Consumer<T> consumer) {
        this.consumer = consumer;
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        consumer.accept((T) parent.getItemAtPosition(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
