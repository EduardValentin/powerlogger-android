package com.eduardv.powerlogger.ui.exercises;

import androidx.lifecycle.ViewModel;

import com.eduardv.powerlogger.dto.ExerciseDTO;
import com.eduardv.powerlogger.repositories.ExerciseRepository;

import java.util.function.Consumer;

public class ExercisesViewModel extends ViewModel {
    private final ExerciseRepository exerciseRepository = ExerciseRepository.getInstance();

    public void deleteExercise(String id, Consumer<Object> onSuccess, Consumer<Throwable> onError) {
        exerciseRepository.deleteExercise(id, onSuccess, onError);
    }

    public void removeExerciseFromCache(int position) {
        exerciseRepository.removeExerciseFromCache(position);
    }

    public void addExerciseToCache(ExerciseDTO exercise) {
        exerciseRepository.addExerciseToCache(exercise);
    }
}
