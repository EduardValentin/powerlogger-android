package com.example.powerlogger.ui.logger;

import android.widget.TextView;

public class LogListItemViewHolder {
    private TextView logName;
    private TextView kcal;
    private int position;

    public LogListItemViewHolder(TextView logName, TextView kcal, int position) {
        this.logName = logName;
        this.kcal = kcal;
        this.position = position;
    }

    public LogListItemViewHolder() {
    }

    public TextView getLogName() {
        return logName;
    }

    public void setLogName(TextView logName) {
        this.logName = logName;
    }

    public TextView getKcal() {
        return kcal;
    }

    public void setKcal(TextView kcal) {
        this.kcal = kcal;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
