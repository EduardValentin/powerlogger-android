package com.eduardv.powerlogger.ui.logger;

import com.eduardv.powerlogger.CreateExerciseRequestDTO;

import java.util.function.Consumer;

public interface OnEditExerciseFragmentSubmitListener {
    void onEditExercise(CreateExerciseRequestDTO exercise, Consumer<Object> onSuccess, Consumer<Throwable> onError);
}
