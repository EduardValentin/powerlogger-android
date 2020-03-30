package com.example.powerlogger.ui.logger;

import androidx.lifecycle.ViewModel;

import com.example.powerlogger.dto.ExerciseDTO;
import com.example.powerlogger.dto.LogDTO;
import com.example.powerlogger.model.ExerciseCategory;
import com.example.powerlogger.repositories.ExerciseRepository;
import com.example.powerlogger.repositories.LogRepository;

import java.util.function.Consumer;

public class CreateOrEditLogViewModel extends ViewModel {
    private boolean isCreatingNewExercise;

    public ExerciseDTO creatingExercise;
    public LogDTO logDTO;

    private ExerciseRepository exerciseRepository;
    private LogRepository logRepository;

    private ExerciseDTO selectedExercise;

    public CreateOrEditLogViewModel() {
        this.exerciseRepository = ExerciseRepository.getInstance();
        this.logRepository = LogRepository.getInstance();

        this.creatingExercise = new ExerciseDTO();
        this.logDTO = new LogDTO();
        this.isCreatingNewExercise = false;
    }

    public void setCreatingNewExercise(boolean value) {
        isCreatingNewExercise = value;
    }

    public void onSave() {
        if (isCreatingNewExercise) {
            exerciseRepository.addNewExercise(creatingExercise, this::addLog, null);
        } else {
            addLog();
        }
    }

    public void addLog() {
        logRepository.addLog(logDTO, this.selectedExercise,null, null);
    }

    public void addLog(Object exercise) {
        ExerciseDTO convertedExercise = (ExerciseDTO) exercise;
        this.selectedExercise = convertedExercise;
        addLog();
    }

    public void createExercise(Consumer<Object> handleSuccess, Consumer<Throwable> handleError) {
        exerciseRepository.addNewExercise(creatingExercise, handleSuccess, handleError);
    }

    public ExerciseDTO getCreatingExercise() {
        return creatingExercise;
    }

    public LogDTO getLogDTO() {
        return logDTO;
    }

    public ExerciseDTO getSelectedExercise() {
        return selectedExercise;
    }

    public void setSelectedExercise(ExerciseDTO selectedExercise) {
        this.selectedExercise = selectedExercise;
    }

    public boolean isCreatingNewExercise() {
        return isCreatingNewExercise;
    }
}
