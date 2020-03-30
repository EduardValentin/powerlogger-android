package com.example.powerlogger.ui.logger;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.powerlogger.dto.GroupDTO;
import com.example.powerlogger.dto.LogDTO;
import com.example.powerlogger.repositories.GroupRepository;
import com.example.powerlogger.repositories.LogRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

public class LoggerViewModel extends ViewModel {
    private LogRepository logRepository = LogRepository.getInstance();
    private GroupRepository groupRepository = GroupRepository.getInstance();

    private MutableLiveData<List<LogDTO>> logCacheForUI = new MutableLiveData<>();
    private MutableLiveData<List<GroupDTO>> groupsLiveData = new MutableLiveData<>();
    private MutableLiveData<LocalDate> currentDateInViewLive = new MutableLiveData<>();

    public LoggerViewModel() {
        currentDateInViewLive.setValue(LocalDate.now());

        logCacheForUI.setValue(logRepository.getLogsCahce().getValue());
        logRepository.getLogsCahce().observeForever(logCacheForUI::setValue);
        groupRepository.getGroupCache().observeForever(groupsLiveData::setValue);

        fetchLogs(LocalDate.now());
    }


    public LiveData<List<LogDTO>> getLogs() {
        return logCacheForUI;
    }

    public LiveData<List<GroupDTO>> getGroups() {
        if (groupRepository.getGroupCache().getValue() == null || groupRepository.getGroupCache().getValue().size() == 0) {
            groupRepository.fetchGroups(null, null);
        }

        return groupsLiveData;
    }

    public MutableLiveData<LocalDate> getCurrentDateInViewLive() {
        return currentDateInViewLive;
    }

    public void addDaysToCurrentDateInView(int days) {
        LocalDate current = currentDateInViewLive.getValue();
        currentDateInViewLive.setValue(current.plusDays(days));
    }

    public void fetchLogs(LocalDate date) {
        logRepository.fetchLogs(date, null, null);
    }

}