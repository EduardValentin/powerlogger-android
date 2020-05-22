package com.example.powerlogger.ui.logger;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.powerlogger.R;
import com.example.powerlogger.dto.LogDTO;

import java.util.List;

public class LogListAdapter extends RecyclerView.Adapter<LogListItemViewHolder> {
    private List<LogDTO> logs;
    private View.OnClickListener onItemClick;

    LogListAdapter(List<LogDTO> data, View.OnClickListener onItemClick) {
        this.logs = data;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public LogListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.log_list_row, parent, false);
        view.setOnClickListener(onItemClick);

        LogListItemViewHolder viewHolder = new LogListItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LogListItemViewHolder holder, int position) {
        holder.bindLog(logs.get(position));
        holder.setPosition(position);
    }

    @Override
    public int getItemCount() {
        return logs.size();
    }
}
