package com.example.powerlogger.ui.logger;

import androidx.lifecycle.ViewModel;

import com.example.powerlogger.dto.LogDTO;
import com.example.powerlogger.repositories.LogRepository;

import java.util.function.Consumer;

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
