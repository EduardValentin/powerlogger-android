package com.example.powerlogger;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.powerlogger.dto.ExerciseDTO;
import com.example.powerlogger.dto.GroupDTO;
import com.example.powerlogger.dto.LogDTO;
import com.example.powerlogger.repositories.GroupRepository;
import com.example.powerlogger.repositories.ExerciseRepository;
import com.example.powerlogger.utils.APIError;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivityViewModel extends ViewModel {

    private MutableLiveData<List<GroupDTO>> groupsLiveData;
    private MutableLiveData<List<ExerciseDTO>> exerciseLiveData;

    //    private LogRepository logRepository;
    private GroupRepository groupRepository;
    private ExerciseRepository exerciseRepository;
    private MutableLiveData<APIError> apiLogsError;
    private MutableLiveData<APIError> apiGroupsError;


    public MainActivityViewModel() {
        groupsLiveData = new MutableLiveData<>();
        groupsLiveData.setValue(new ArrayList<>());

        exerciseLiveData = new MutableLiveData<>();
        exerciseLiveData.setValue(new ArrayList<>());

//        logRepository = LogRepository.getInstance();
        exerciseRepository = ExerciseRepository.getInstance();
        groupRepository = GroupRepository.getInstance();

        apiLogsError = new MutableLiveData<>();
        apiGroupsError = new MutableLiveData<>();



        exerciseRepository.getExerciseCache().observeForever(exerciseLiveData::setValue);
        groupRepository.getGroupCache().observeForever(groupsLiveData::setValue);
    }


    public MutableLiveData<List<GroupDTO>> getGroupsLiveData() {
        return groupsLiveData;
    }

    public void fetchLogs() {
        exerciseRepository.fetchExercises(
                data -> { },
                t -> apiLogsError.setValue(new APIError(t.getMessage(), "Could not load logs from the server.")));
    }


    public void fetchGroups() {
        groupRepository.fetchGroups(
                data -> {
                },
                t -> apiGroupsError.setValue(new APIError(t.getMessage(), "Could not load groups from the server")));
    }

    public MutableLiveData<List<ExerciseDTO>> getExerciseLiveData() {
        return exerciseLiveData;
    }

    public LiveData<APIError> getApiLogsError() {
        return apiLogsError;
    }

    public LiveData<APIError> getApiGroupsError() {
        return apiGroupsError;
    }
}
