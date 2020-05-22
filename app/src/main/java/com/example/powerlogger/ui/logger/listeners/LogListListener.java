package com.example.powerlogger.ui.logger.listeners;

import androidx.fragment.app.FragmentManager;

import com.example.powerlogger.dto.LogDTO;

public interface LogListListener {
    void removeLog(LogDTO logDTO);
}
