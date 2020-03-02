package com.example.powerlogger;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.powerlogger.dto.GroupDTO;
import com.example.powerlogger.dto.LogDTO;
import com.example.powerlogger.repositories.GroupRepository;
import com.example.powerlogger.repositories.LogRepository;
import com.example.powerlogger.utils.APIError;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivityViewModel extends ViewModel {

    private MutableLiveData<List<LogDTO>> logsLiveData;
    private MutableLiveData<List<GroupDTO>> groupsLiveData;

    private LogRepository logRepository;
    private GroupRepository groupRepository;

    private MutableLiveData<APIError> apiLogsError;
    private MutableLiveData<APIError> apiGroupsError;

    private MutableLiveData<Date> curreantDateInViewLive = new MutableLiveData<>();

    public MainActivityViewModel() {
        curreantDateInViewLive.setValue(new Date());

        logsLiveData = new MutableLiveData<>();
        logsLiveData.setValue(new ArrayList<>());

        groupsLiveData = new MutableLiveData<>();
        groupsLiveData.setValue(new ArrayList<>());

        logRepository = LogRepository.getInstance();
        groupRepository = GroupRepository.getInstance();

        apiLogsError = new MutableLiveData<>();
        apiGroupsError = new MutableLiveData<>();

        logRepository.getLogsCahce()
                .observeForever(logs -> logsLiveData.setValue(logs));

        groupRepository.getGroupCache()
                .observeForever(groups -> groupsLiveData.setValue(groups));
    }

    public MutableLiveData<List<LogDTO>> getLogsLiveData() {
        return logsLiveData;
    }

    public MutableLiveData<List<GroupDTO>> getGroupsLiveData() {
        return groupsLiveData;
    }

    public void fetchLogs(Date date) {
        logRepository.fetchLogs(
                date,
                data -> {},
                t -> apiLogsError.setValue(new APIError(t.getMessage(), "Could not load logs from the server.")));
    }

    public void fetchGroups() {
        groupRepository.fetchGroups(
                data -> {},
                t -> apiGroupsError.setValue(new APIError(t.getMessage(), "Could not load groups from the server")));
    }

    public LiveData<APIError> getApiLogsError() {
        return apiLogsError;
    }

    public LiveData<APIError> getApiGroupsError() {
        return apiGroupsError;
    }

    public void addDaysToCurrentDateInView(int days) {
        Date current = curreantDateInViewLive.getValue();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(current);
        calendar.add(Calendar.DATE, days);
        curreantDateInViewLive.setValue(calendar.getTime());
    }

    public LiveData<Date> getCurreantDateInViewLive() {
        return curreantDateInViewLive;
    }
}
