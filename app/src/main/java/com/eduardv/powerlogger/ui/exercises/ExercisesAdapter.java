package com.eduardv.powerlogger.ui.exercises;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eduardv.powerlogger.BuildConfig;
import com.eduardv.powerlogger.R;
import com.eduardv.powerlogger.dto.ExerciseCategoryDTO;
import com.eduardv.powerlogger.dto.ExerciseDTO;
import com.eduardv.powerlogger.lib.lists.SwipeListAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.List;

public class ExercisesAdapter extends RecyclerView.Adapter<ExerciseViewHolder> implements SwipeListAdapter<ExerciseDTO> {
    private final List<ExerciseDTO> exercises;
    private List<ExerciseCategoryDTO> categories;
    private final View.OnClickListener onItemClick;

    public ExercisesAdapter(List<ExerciseDTO> exercises, View.OnClickListener onItemClick) {
        this.exercises = exercises;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_list_layout, parent, false);
        view.setOnClickListener(onItemClick);
        if (viewType == VIEW_TYPE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_list_layout, parent, false);
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

        ExerciseViewHolder viewHolder = new ExerciseViewHolder(view);
        viewHolder.setViewType(viewType);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        holder.bindExercise(exercises.get(position), categories);
        holder.setPosition(position);
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    @Override
    public void removeItem(int position) {
        exercises.remove(position);
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
    public void restoreItem(ExerciseDTO item, int position) {
        exercises.add(position, item);
        notifyItemInserted(position);
    }

    public void setCategories(List<ExerciseCategoryDTO> categories) {
        this.categories = categories;
    }
}
