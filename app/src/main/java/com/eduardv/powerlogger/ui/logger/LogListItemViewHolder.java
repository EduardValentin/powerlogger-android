package com.eduardv.powerlogger.ui.logger;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eduardv.powerlogger.R;
import com.eduardv.powerlogger.dto.ExerciseCategoryDTO;
import com.eduardv.powerlogger.dto.logs.LogDTO;
import com.eduardv.powerlogger.lib.lists.SwipeListAdapter;
import com.eduardv.powerlogger.lib.lists.SwipeListViewHolder;
import com.eduardv.powerlogger.model.ExerciseCategory;

import java.util.List;

public class LogListItemViewHolder extends RecyclerView.ViewHolder implements SwipeListViewHolder {

    private View itemView;
    private LinearLayout viewForeground;
    private int viewType = SwipeListAdapter.VIEW_TYPE;

    public LogListItemViewHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView = itemView;
        this.viewForeground = itemView.findViewById(R.id.logRowContent);
    }

    public void bindLog(LogDTO logDTO, List<ExerciseCategoryDTO> categories) {
        if (viewType == SwipeListAdapter.AD_TYPE) {
            return;
        }

        TextView rowCal = itemView.findViewById(R.id.logsRowLogCal);
        rowCal.setText(logDTO.getKcal() + " Kcal");

        TextView rowName = itemView.findViewById(R.id.logsRowLogName);
        rowName.setText(logDTO.getExercise().getName());

        ExerciseCategoryDTO category = logDTO.getExercise().getCategory();

        if (categories != null) {
            category = categories.stream().filter(cat -> cat.getIdentifierName().equals(logDTO.getExercise().getCategory())).findFirst()
                    .orElse(category);
        }

        TextView exerciseType = itemView.findViewById(R.id.exerciseType);
        exerciseType.setText(category.getDisplayName());
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
