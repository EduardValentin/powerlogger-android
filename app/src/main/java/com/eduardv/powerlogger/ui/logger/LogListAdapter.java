package com.eduardv.powerlogger.ui.logger;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eduardv.powerlogger.BuildConfig;
import com.eduardv.powerlogger.R;
import com.eduardv.powerlogger.dto.ExerciseCategoryDTO;
import com.eduardv.powerlogger.dto.logs.LogDTO;
import com.eduardv.powerlogger.lib.lists.SwipeListAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

public class LogListAdapter extends RecyclerView.Adapter<LogListItemViewHolder> implements SwipeListAdapter<LogDTO> {
    private List<LogDTO> logs;
    private List<ExerciseCategoryDTO> categories;

    private View.OnClickListener onItemClick;

    LogListAdapter(List<LogDTO> data, View.OnClickListener onItemClick) {
        this.logs = data;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public LogListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.log_list_row, parent, false);
            view.setOnClickListener(onItemClick);
        } else {
            view = new AdView(parent.getContext());

            AdView adView = (AdView) view;
            adView.setAdSize(AdSize.BANNER);
            adView.setAdUnitId(BuildConfig.LIST_ADD_UNIT_CODE);
            AdRequest adRequest = new AdRequest.Builder().build();
            float density = parent.getContext().getResources().getDisplayMetrics().density;
            int height = Math.round(AdSize.BANNER.getHeight() * density);
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, height);
            view.setLayoutParams(params);

            adView.loadAd(adRequest);

        }

        LogListItemViewHolder viewHolder = new LogListItemViewHolder(view);
        viewHolder.setViewType(viewType);

        return viewHolder;
    }

    @Override
    public void removeItem(int position) {
        logs.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void restoreItem(LogDTO item, int position) {
        logs.add(position, item);
        notifyItemInserted(position);
    }

    @Override
    public void onBindViewHolder(@NonNull LogListItemViewHolder holder, int position) {
        holder.bindLog(logs.get(position), categories);
        holder.setPosition(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return AD_TYPE;
        }

        return VIEW_TYPE;
    }

    @Override
    public int getItemCount() {
        return logs.size();
    }

    public void setCategories(List<ExerciseCategoryDTO> categories) {
        this.categories = categories;
    }
}
