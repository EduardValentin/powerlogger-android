package com.example.powerlogger.ui.includeWorkouts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.powerlogger.dto.ExerciseDTO;
import com.example.powerlogger.dto.GroupDTO;
import com.example.powerlogger.dto.LogDTO;
import com.example.powerlogger.repositories.LogRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class IncludeGroupsViewModel extends ViewModel {
    private LogRepository logRepository = LogRepository.getInstance();

    private List<ExerciseDTO> exercises;
    private List<GroupDTO> groups;
    private Map<String, Boolean> checkedGroups;
    private LocalDate date;

    private MutableLiveData<Map<String, LogDTO>> includedUnsavedLogs;

    public IncludeGroupsViewModel() {
        checkedGroups = new HashMap<>();

        includedUnsavedLogs = new MutableLiveData<>();
        includedUnsavedLogs.setValue(new HashMap<>());
    }

    public void setGroups(List<GroupDTO> groups) {
        this.groups = groups;
    }

    public void setExercises(List<ExerciseDTO> exercises) {
        this.exercises = exercises;
    }

    public void cleanCheckedGroups() {
        checkedGroups.clear();
    }

    public List<ExerciseDTO> getExercises() {
        return exercises;
    }

    public List<GroupDTO> getGroups() {
        return groups;
    }

    public Map<String, Boolean> getCheckedGroups() {
        return checkedGroups;
    }

    public MutableLiveData<Map<String, LogDTO>> getIncludedUnsavedLogsLiveData() {
        return includedUnsavedLogs;
    }

    public void setIncludedUnsavedLogs(Map<String, LogDTO> includedUnsavedLogs) {
        this.includedUnsavedLogs.setValue(includedUnsavedLogs);
    }

    public void updateLogCalories(LogDTO logDTO, Consumer<LogDTO> callback) {
        logRepository.getLogCalories(logDTO.getExercise().getCategory().toString(),
                logDTO.getMinutes(), callback, null);
    }

    public void replaceUnsavedLog(LogDTO logDTO) {
        Map<String, LogDTO> currentUnsavedLogs = this.includedUnsavedLogs.getValue();
        currentUnsavedLogs.replace(logDTO.getId(), logDTO);
        this.includedUnsavedLogs.setValue(currentUnsavedLogs);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
