package com.eduardv.powerlogger.ui.logger;

import androidx.lifecycle.ViewModel;

//import com.example.powerlogger.AppDatabase;
import com.eduardv.powerlogger.CreateExerciseRequestDTO;
import com.eduardv.powerlogger.dto.ExerciseDTO;
import com.eduardv.powerlogger.dto.logs.LogDTO;
import com.eduardv.powerlogger.repositories.ExerciseRepository;
import com.eduardv.powerlogger.repositories.LogRepository;

import java.util.ArrayList;
import java.util.function.Consumer;

public class CreateOrEditLogViewModel extends ViewModel {
    private boolean isCreatingNewExercise;

    public CreateExerciseRequestDTO creatingExercise;
    public LogDTO logDTO;

    private ExerciseRepository exerciseRepository;
    private LogRepository logRepository;

    private ExerciseDTO selectedExercise;

    public CreateOrEditLogViewModel() {
        this.exerciseRepository = ExerciseRepository.getInstance();
        this.logRepository = LogRepository.getInstance();

        this.creatingExercise = new CreateExerciseRequestDTO();
        this.creatingExercise.setGroups(new ArrayList<>());
        this.logDTO = new LogDTO();
        this.isCreatingNewExercise = false;
    }

    public void setCreatingNewExercise(boolean value) {
        isCreatingNewExercise = value;
    }

    public void addLog(Consumer<Object> handleSuccess) {
        logRepository.addLog(logDTO, this.selectedExercise, handleSuccess, null);
    }

    public void addLog(Object exercise, Consumer<Object> handleSuccess) {
        ExerciseDTO convertedExercise = (ExerciseDTO) exercise;
        this.selectedExercise = convertedExercise;
        addLog(handleSuccess);
    }

    public void createExercise(Consumer<Object> handleSuccess, Consumer<Throwable> handleError) {
        exerciseRepository.addNewExercise(creatingExercise, handleSuccess, handleError);
    }

    public CreateExerciseRequestDTO getCreatingExercise() {
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
