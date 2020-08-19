package com.eduardv.powerlogger.ui.groups;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eduardv.powerlogger.R;
import com.eduardv.powerlogger.dto.GroupDTO;
import com.eduardv.powerlogger.lib.lists.SwipeListAdapter;
import com.eduardv.powerlogger.lib.lists.SwipeListViewHolder;

public class GroupsViewHolder extends RecyclerView.ViewHolder implements SwipeListViewHolder {
    private final View itemView;
    private LinearLayout viewForeground;
    private int viewType = SwipeListAdapter.VIEW_TYPE;

    GroupsViewHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView = itemView;
        this.viewForeground = itemView.findViewById(R.id.groupsLayout);
    }

    void bindExercise(GroupDTO groupDTO) {
        if (viewType == SwipeListAdapter.AD_TYPE) {
            return;
        }

        TextView tv = itemView.findViewById(R.id.groupName);
        tv.setText(groupDTO.getName());

        itemView.setTag(groupDTO.getId());
    }

    public void setPosition(int position) {
        itemView.setTag(position);
    }

    @Override
    public LinearLayout getViewForeground() {
        return viewForeground;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
