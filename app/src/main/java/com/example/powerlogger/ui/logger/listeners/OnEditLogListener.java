package com.example.powerlogger.ui.logger.listeners;

import com.example.powerlogger.dto.LogDTO;

import java.util.function.Consumer;

public interface OnEditLogListener {
    void onEditLog(LogDTO log, Consumer<Object> onSuccess, Consumer<Throwable> onError);
}
