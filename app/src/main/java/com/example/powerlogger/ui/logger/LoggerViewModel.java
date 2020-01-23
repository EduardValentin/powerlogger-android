package com.example.powerlogger.ui.logger;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.powerlogger.dto.GroupDTO;
import com.example.powerlogger.dto.LogDTO;
import com.example.powerlogger.model.Log;
import com.example.powerlogger.repositories.GroupRepository;
import com.example.powerlogger.repositories.LogRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoggerViewModel extends ViewModel {
    private LogRepository logRepository = LogRepository.getInstance();
    private GroupRepository groupRepository = GroupRepository.getInstance();
    private LocalDate currentDate = LocalDate.now();
    private MutableLiveData<List<LogDTO>> logCacheForUI = new MutableLiveData<>();
    private MutableLiveData<List<GroupDTO>> groupsLiveData = new MutableLiveData<>();

    public LoggerViewModel() {
        logCacheForUI.setValue(logRepository.getLogsCahce().getValue());
        logRepository.getLogsCahce().observeForever(logDTOS -> logCacheForUI.setValue(logDTOS));
        groupRepository.getGroupCache().observeForever(groupDTOS -> groupsLiveData.setValue(groupDTOS));
    }

    public LiveData<List<LogDTO>> getLogs() {

        return logCacheForUI;
    }

    public LiveData<List<GroupDTO>> getGroups() {
        if(groupRepository.getGroupCache().getValue() == null || groupRepository.getGroupCache().getValue().size() == 0) {
            groupRepository.fetchGroups(o -> {}, t -> {});
        }

        return groupsLiveData;
    }

    public void addLog(LogDTO log, Consumer<Object> handleSuccess, Consumer<Throwable> handleError) {
        logRepository.addLog(log, handleSuccess, handleError);
    }

    public void fetchLogs() {
        logRepository.fetchLogs(this.currentDate);
    }
}