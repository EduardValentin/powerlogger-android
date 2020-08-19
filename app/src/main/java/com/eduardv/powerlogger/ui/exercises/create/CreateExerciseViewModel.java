package com.eduardv.powerlogger.ui.exercises.create;

import androidx.lifecycle.ViewModel;

import com.eduardv.powerlogger.CreateExerciseRequestDTO;
import com.eduardv.powerlogger.dto.ExerciseDTO;
import com.eduardv.powerlogger.repositories.ExerciseRepository;

import java.util.function.Consumer;

public class CreateExerciseViewModel extends ViewModel {
    private final CreateExerciseRequestDTO exercise;
    private final ExerciseRepository exerciseRepository;

    public CreateExerciseViewModel() {
        exercise = new CreateExerciseRequestDTO();
        exerciseRepository = ExerciseRepository.getInstance();
    }

    public void sendCreateExercise(Consumer<Object> onSuccess, Consumer<Throwable> onError) {
        exerciseRepository.addNewExercise(exercise, onSuccess, onError);
    }

    public CreateExerciseRequestDTO getExercise() {
        return exercise;
    }
}
