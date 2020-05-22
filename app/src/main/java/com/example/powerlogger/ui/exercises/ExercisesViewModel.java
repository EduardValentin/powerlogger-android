package com.example.powerlogger.ui.exercises;

import androidx.lifecycle.ViewModel;

import com.example.powerlogger.dto.ExerciseDTO;
import com.example.powerlogger.repositories.ExerciseRepository;

import java.util.function.Consumer;

public class ExercisesViewModel extends ViewModel {
    private final ExerciseRepository exerciseRepository = ExerciseRepository.getInstance();

    public void deleteExercise(String id, Consumer<Object> onSuccess, Consumer<Throwable> onError) {
        exerciseRepository.deleteExercise(id, onSuccess, onError);
    }
}
