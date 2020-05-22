package com.example.powerlogger.ui.logger;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.powerlogger.R;
import com.example.powerlogger.dto.LogDTO;

public class LogListItemViewHolder extends RecyclerView.ViewHolder {
    private TextView logName;
    private TextView kcal;

    private View itemView;

    public LogListItemViewHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView = itemView;
    }

    public void bindLog(LogDTO logDTO) {
        TextView rowCal = itemView.findViewById(R.id.logsRowLogCal);
        rowCal.setText(logDTO.getKcal() + " Kcal");

        TextView rowName = itemView.findViewById(R.id.logsRowLogName);
        rowName.setText(logDTO.getExercise().getName());
    }

    public void setPosition(int position) {
        itemView.setTag(position);
    }
}
