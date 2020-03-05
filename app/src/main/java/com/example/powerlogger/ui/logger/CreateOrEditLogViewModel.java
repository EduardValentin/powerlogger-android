package com.example.powerlogger.ui.logger;

import androidx.lifecycle.ViewModel;

import com.example.powerlogger.dto.ExerciseDTO;
import com.example.powerlogger.dto.LogDTO;
import com.example.powerlogger.model.ExerciseCategory;
import com.example.powerlogger.repositories.ExerciseRepository;
import com.example.powerlogger.repositories.LogRepository;

public class CreateOrEditLogViewModel extends ViewModel {
    private boolean isCreatingNewExercise;

    private ExerciseDTO exerciseDTO;
    private LogDTO logDTO;

    private ExerciseRepository exerciseRepository;
    private LogRepository logRepository;

    public CreateOrEditLogViewModel() {
        this.exerciseRepository = ExerciseRepository.getInstance();
        this.logRepository = LogRepository.getInstance();

        this.exerciseDTO = new ExerciseDTO();
        this.logDTO = new LogDTO();
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
            exerciseRepository.addNewExercise(exerciseDTO, this::addLog, null);
        } else {
            addLog();
        }
    }

    public void addLog() {
        logRepository.addLog(logDTO, null, null);
    }

    public void addLog(Object exercise) {
        ExerciseDTO convertedExercise = (ExerciseDTO) exercise;
        logDTO.setExercise(convertedExercise);
        addLog();
    }

    public ExerciseDTO getExerciseDTO() {
        return exerciseDTO;
    }

    public LogDTO getLogDTO() {
        return logDTO;
    }
}
