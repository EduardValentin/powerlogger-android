package com.example.powerlogger;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.powerlogger.dto.ExerciseDTO;
import com.example.powerlogger.dto.GroupDTO;
import com.example.powerlogger.dto.LogDTO;
import com.example.powerlogger.repositories.GroupRepository;
import com.example.powerlogger.repositories.ExerciseRepository;
import com.example.powerlogger.repositories.LogRepository;
import com.example.powerlogger.repositories.UserRepository;
import com.example.powerlogger.utils.APIError;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class MainActivityViewModel extends ViewModel {

    private static final String UI_ERROR = "There was a problem while fetching data from the server";

    private MutableLiveData<List<GroupDTO>> groupsLiveData;
    private MutableLiveData<List<ExerciseDTO>> exerciseLiveData;

    //    private LogRepository logRepository;
    private GroupRepository groupRepository;
    private ExerciseRepository exerciseRepository;
    private LogRepository logRepository;

    private MutableLiveData<APIError> apiLogsError;
    private MutableLiveData<APIError> apiGroupsError;
    private Timer timer;


    public MainActivityViewModel() {
        groupsLiveData = new MutableLiveData<>();
        groupsLiveData.setValue(new ArrayList<>());

        exerciseLiveData = new MutableLiveData<>();
        exerciseLiveData.setValue(new ArrayList<>());

        exerciseRepository = ExerciseRepository.getInstance();
        groupRepository = GroupRepository.getInstance();
        logRepository = LogRepository.getInstance();

        apiLogsError = new MutableLiveData<>();
        apiGroupsError = new MutableLiveData<>();


        exerciseRepository.getExerciseCache().observeForever(exerciseLiveData::setValue);
        groupRepository.getGroupCache().observeForever(groupsLiveData::setValue);

        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.i("Scheduled fetch", "Started fetching data");
                fetchExercises();
                fetchGroups();
            }
        }, 0, 1000L * 30);
    }

    public MutableLiveData<List<GroupDTO>> getGroupsLiveData() {
        return groupsLiveData;
    }

    public void sendBatchLogs(List<LogDTO> logsToSend) {
        logRepository.sendBatchLogs(logsToSend, null, null);
    }

    public void removeGroup(String groupId, Consumer<Void> onSuccess, Consumer<Throwable> onError) {
        groupRepository.removeGroup(groupId, onSuccess, onError);
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

    public Timer getTimer() {
        return timer;
    }

    private void fetchExercises() {
        exerciseRepository.fetchExercises(
                data -> {
                },
                t -> apiLogsError.setValue(new APIError(t.getMessage(), UI_ERROR)));
    }

    private void fetchGroups() {
        groupRepository.fetchGroups(
                data -> {
                },
                t -> apiGroupsError.setValue(new APIError(t.getMessage(), UI_ERROR)));
    }
}
