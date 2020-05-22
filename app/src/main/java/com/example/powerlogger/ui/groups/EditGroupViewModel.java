package com.example.powerlogger.ui.groups;

import androidx.lifecycle.ViewModel;

import com.example.powerlogger.dto.ExerciseDTO;

import java.util.List;

public class EditGroupViewModel extends ViewModel {
    private String name;
    private List<ExerciseDTO> addedExercises;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ExerciseDTO> getAddedExercises() {
        return addedExercises;
    }

    public void setAddedExercises(List<ExerciseDTO> addedExercises) {
        this.addedExercises = addedExercises;
    }
}
