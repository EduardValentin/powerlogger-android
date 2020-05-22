package com.example.powerlogger.ui.groups;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.powerlogger.R;
import com.example.powerlogger.dto.GroupDTO;

import java.util.List;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsViewHolder> {

    private final View.OnClickListener onItemClick;
    private final List<GroupDTO> groups;

    public GroupsAdapter(List<GroupDTO> groups, View.OnClickListener onItemClick) {
        this.onItemClick = onItemClick;
        this.groups = groups;
    }

    @NonNull
    @Override
    public GroupsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.groups_list_item_layout, parent, false);
        view.setOnClickListener(onItemClick);

        return new GroupsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupsViewHolder holder, int position) {
        holder.bindExercise(groups.get(position));
        holder.setPosition(position);
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }
}
