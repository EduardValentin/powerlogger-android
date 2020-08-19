package com.eduardv.powerlogger.ui.logger;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eduardv.powerlogger.dto.logs.LogDTO;
import com.eduardv.powerlogger.repositories.LogRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

public class LoggerViewModel extends ViewModel {
    private LogRepository logRepository = LogRepository.getInstance();

    private MutableLiveData<List<LogDTO>> logCacheForUI = new MutableLiveData<>();
    private MutableLiveData<LocalDate> currentDateInViewLive = new MutableLiveData<>();

    public LoggerViewModel() {
        currentDateInViewLive.setValue(LocalDate.now());

        logCacheForUI.setValue(logRepository.getLogsCache().getValue());
        logRepository.getLogsCache().observeForever(logCacheForUI::setValue);

        fetchLogs(LocalDate.now(), null);
    }


    public LiveData<List<LogDTO>> getLogs() {
        return logCacheForUI;
    }


    public MutableLiveData<LocalDate> getCurrentDateInViewLive() {
        return currentDateInViewLive;
    }

    public void addDaysToCurrentDateInView(int days) {
        LocalDate current = currentDateInViewLive.getValue();
        currentDateInViewLive.setValue(current.plusDays(days));
    }

    public void fetchLogs(LocalDate date, Consumer<Object> successConsumer) {
        logRepository.fetchLogs(date, successConsumer, null);
    }

}