package com.example.powerlogger.ui.logger;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.powerlogger.R;
import com.example.powerlogger.dto.LogDTO;
import com.example.powerlogger.ui.groups.GroupCreateOrEditFragment;

import java.util.List;
import java.util.function.Consumer;

public class LogListAdapter extends ArrayAdapter<LogDTO>{

    private Context context;
    private List<LogDTO> logs;
    private View.OnClickListener onItemClick;

    LogListAdapter(Context context, List<LogDTO> data, View.OnClickListener onItemClick) {
        super(context, R.layout.log_list_row, data);
        this.context = context;
        this.logs = data;
        this.onItemClick = onItemClick;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        LogDTO dataModel = getItem(position);   // Check if an existing view is being reused, otherwise inflate the view
        LogListItemViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {
            viewHolder = new LogListItemViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.log_list_row, parent, false);
            viewHolder.setLogName(convertView.findViewById(R.id.logsRowLogName));
            viewHolder.setKcal( convertView.findViewById(R.id.logsRowLogCal));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (LogListItemViewHolder) convertView.getTag();
        }

        viewHolder.getLogName().setText(dataModel.getExercise().getName());
        viewHolder.getKcal().setText("Kcal: " + dataModel.getKcal());
        viewHolder.setPosition(position);
        convertView.setOnClickListener(onItemClick);

        return convertView;
    }
}
