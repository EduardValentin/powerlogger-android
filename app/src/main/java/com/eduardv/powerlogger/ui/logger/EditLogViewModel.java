package com.eduardv.powerlogger.ui.logger;

import androidx.lifecycle.ViewModel;

import com.eduardv.powerlogger.dto.logs.LogDTO;
import com.eduardv.powerlogger.repositories.LogRepository;

public class EditLogViewModel extends ViewModel {
    private LogDTO logToEdit;
    private LogRepository logRepository = LogRepository.getInstance();

    public EditLogViewModel() { }

    public void setLogToEdit(LogDTO logToEdit) {
        this.logToEdit = logToEdit;
    }

    public LogDTO getLogToEdit() {
        return logToEdit;
    }
}
