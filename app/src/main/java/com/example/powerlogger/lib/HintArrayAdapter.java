package com.example.powerlogger.lib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.powerlogger.R;

public class HintArrayAdapter<T extends SelectableItem> extends ArrayAdapter {

    private SelectableItem hint = () -> "Select";

    public HintArrayAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public SelectableItem getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = () -> hint;

        this.clear();
        this.add(this.hint);
    }

    @Override
    public void clear() {
        super.clear();
        this.add(hint.getName());
    }

    @Override
    public boolean isEnabled(int position) {
        if (position == 0) {
            return false;
        } else {
            return true;
        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView,
                        ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView tv = (TextView) view;
        if (position == 0) {
            tv.setText("");
            tv.setHint(this.hint.getName());

            tv.setHintTextColor(ContextCompat.getColor(getContext(), R.color.fadedTextColor));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        }
        return view;
    }
}
