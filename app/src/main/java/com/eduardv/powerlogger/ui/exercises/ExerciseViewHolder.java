package com.eduardv.powerlogger.ui.exercises;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eduardv.powerlogger.R;
import com.eduardv.powerlogger.dto.ExerciseCategoryDTO;
import com.eduardv.powerlogger.dto.ExerciseDTO;
import com.eduardv.powerlogger.lib.lists.SwipeListAdapter;
import com.eduardv.powerlogger.lib.lists.SwipeListViewHolder;

import java.util.List;

public class ExerciseViewHolder extends RecyclerView.ViewHolder implements SwipeListViewHolder {

    private final View itemView;
    private final LinearLayout foregroundView;
    private int viewType = SwipeListAdapter.VIEW_TYPE;

    ExerciseViewHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView = itemView;
        this.foregroundView = itemView.findViewById(R.id.exerciseLayout);
    }

    void bindExercise(ExerciseDTO exerciseDTO, List<ExerciseCategoryDTO> categories) {
        if (viewType == SwipeListAdapter.AD_TYPE) {
            return;
        }

        TextView tv = itemView.findViewById(R.id.exerciseName);
        tv.setText(exerciseDTO.getName());

        ExerciseCategoryDTO category = exerciseDTO.getCategory();

        if (categories != null) {
            category = categories.stream().filter(cat -> cat.getIdentifierName().equals(exerciseDTO.getCategory())).findFirst()
                    .orElse(category);
        }

        TextView typeTextView = itemView.findViewById(R.id.exerciseType);
        typeTextView.setText(category.getDisplayName());
    }

    public void setPosition(int position) {
        itemView.setTag(position);
    }

    @Override
    public LinearLayout getViewForeground() {
        return foregroundView;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
