package com.example.powerlogger.model;

import com.example.powerlogger.ui.logger.LogType;

public class Log {
    private String name;
    private LogType type;
    private int intensity;

    public Log(String name, LogType type, int intensity) {
        this.name = name;
        this.type = type;
        this.intensity = intensity;
    }

    public int getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LogType getType() {
        return type;
    }

    public void setType(LogType type) {
        this.type = type;
    }
}
