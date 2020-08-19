package com.eduardv.powerlogger.ui.logger.listeners;

import com.eduardv.powerlogger.dto.logs.LogDTO;

import java.util.function.Consumer;

public interface OnEditLogListener {
    void onEditLog(LogDTO log, Consumer<Object> onSuccess, Consumer<Throwable> onError);
}
