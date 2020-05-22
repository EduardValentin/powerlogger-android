package com.example.powerlogger.ui.exercises;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.powerlogger.R;
import com.example.powerlogger.dto.ExerciseDTO;

public class ExerciseViewHolder extends RecyclerView.ViewHolder {
    private final View itemView;

    ExerciseViewHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView = itemView;
    }

    void bindExercise(ExerciseDTO exerciseDTO) {
        TextView tv = itemView.findViewById(R.id.exerciseName);
        tv.setText(exerciseDTO.getName());

        TextView typeTextView = itemView.findViewById(R.id.exerciseType);
        typeTextView.setText(exerciseDTO.getCategory().getName());
    }

    public void setPosition(int position) {
        itemView.setTag(position);
    }

}
