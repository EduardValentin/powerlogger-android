package com.example.powerlogger.ui.exercises;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.powerlogger.R;
import com.example.powerlogger.dto.ExerciseDTO;

import java.util.List;

public class ExercisesAdapter extends RecyclerView.Adapter<ExerciseViewHolder> {
    private final List<ExerciseDTO> exercises;
    private final View.OnClickListener onItemClick;

    public ExercisesAdapter(List<ExerciseDTO> exercises, View.OnClickListener onItemClick) {
        this.exercises = exercises;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_list_layout, parent, false);
        view.setOnClickListener(onItemClick);

        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        holder.bindExercise(exercises.get(position));
        holder.setPosition(position);
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }
}
