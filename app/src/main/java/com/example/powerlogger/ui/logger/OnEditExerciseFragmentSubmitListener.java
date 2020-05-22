package com.example.powerlogger.ui.logger;

import com.example.powerlogger.MinifiedExerciseDTO;
import com.example.powerlogger.dto.ExerciseDTO;

import java.util.function.Consumer;

public interface OnEditExerciseFragmentSubmitListener {
    void onEditExercise(MinifiedExerciseDTO exercise, Consumer<Object> onSuccess, Consumer<Throwable> onError);
}
