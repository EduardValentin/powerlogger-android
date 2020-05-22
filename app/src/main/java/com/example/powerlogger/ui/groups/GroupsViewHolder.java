package com.example.powerlogger.ui.groups;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.powerlogger.R;
import com.example.powerlogger.dto.GroupDTO;

public class GroupsViewHolder extends RecyclerView.ViewHolder {
    private final View itemView;

    GroupsViewHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView = itemView;
    }

    void bindExercise(GroupDTO groupDTO) {
        TextView tv = itemView.findViewById(R.id.groupName);
        tv.setText(groupDTO.getName());
    }

    public void setPosition(int position) {
        itemView.setTag(position);
    }
}
