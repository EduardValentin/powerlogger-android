package com.eduardv.powerlogger.ui.logger.listeners;

import com.eduardv.powerlogger.dto.logs.LogDTO;

public interface LogListListener {
    void removeLog(LogDTO logDTO);
    void removeLogFromCache(int position);
    void addLogToCache(LogDTO log);
}
