package com.eduardv.powerlogger.ui.groups;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eduardv.powerlogger.BuildConfig;
import com.eduardv.powerlogger.R;
import com.eduardv.powerlogger.dto.GroupDTO;
import com.eduardv.powerlogger.lib.lists.SwipeListAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.List;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsViewHolder> implements SwipeListAdapter<GroupDTO> {

    private final View.OnClickListener onItemClick;
    private final List<GroupDTO> groups;

    public GroupsAdapter(List<GroupDTO> groups, View.OnClickListener onItemClick) {
        this.onItemClick = onItemClick;
        this.groups = groups;
    }

    @NonNull
    @Override
    public GroupsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

        if (viewType == VIEW_TYPE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.groups_list_item_layout, parent, false);
            view.setOnClickListener(onItemClick);
        } else {
            view = new AdView(parent.getContext());

            AdView adView = (AdView) view;
            adView.setAdSize(AdSize.BANNER);
            adView.setAdUnitId(BuildConfig.LIST_ADD_UNIT_CODE);
            adView.setVisibility(View.INVISIBLE);
            AdRequest adRequest = new AdRequest.Builder().build();
            float density = parent.getContext().getResources().getDisplayMetrics().density;
            int height = Math.round(AdSize.BANNER.getHeight() * density);
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, height);
            view.setLayoutParams(params);

            adView.loadAd(adRequest);

        }

        GroupsViewHolder viewHolder = new GroupsViewHolder(view);
        viewHolder.setViewType(viewType);

        return viewHolder;
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

    @Override
    public void removeItem(int position) {
        groups.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return AD_TYPE;
        }

        return VIEW_TYPE;
    }

    @Override
    public void restoreItem(GroupDTO item, int position) {
        groups.add(position, item);
        notifyItemInserted(position);
    }
}
