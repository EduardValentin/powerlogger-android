package com.example.powerlogger.ui.logger;

import androidx.lifecycle.ViewModel;

import com.example.powerlogger.dto.ExerciseDTO;
import com.example.powerlogger.model.ExerciseCategory;
import com.example.powerlogger.repositories.ExerciseRepository;

public class CreateOrEditLogViewModel extends ViewModel {
    private boolean isCreatingNewExercise;
    private ExerciseDTO exerciseDTO;

    private ExerciseRepository exerciseRepository;

    public CreateOrEditLogViewModel() {
        this.exerciseRepository = ExerciseRepository.getInstance();
        this.isCreatingNewExercise = false;
    }

    public void setExerciseName(String name) {
        exerciseDTO.setName(name);
    }

    public void setExerciseCategory(ExerciseCategory category) {
        exerciseDTO.setName(category.toString());
    }

    public void setCreatingNewExercise(boolean value) {
        isCreatingNewExercise = value;
    }

    public void onSave() {
        if (isCreatingNewExercise) {
            // Code to create exercise here

        } else {

        }
    }
}
