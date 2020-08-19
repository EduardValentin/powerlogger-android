package com.eduardv.powerlogger.utils;

import java.util.function.Consumer;

public class APICallsUtils {
    public static <T> Consumer<T> getHandlerOrDefault(Consumer<T> handler) {
        if (handler == null) {
            return (t) -> {};
        }
        return handler;
    }
}
