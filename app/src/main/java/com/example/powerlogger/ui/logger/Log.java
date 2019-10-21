package com.example.powerlogger.ui.logger;

public class Log {
    private String name;
    private LogType type;

    public int getAmmount() {
        return ammount;
    }

    public void setAmmount(int ammount) {
        this.ammount = ammount;
    }

    private int ammount;

    public Log(String name, LogType type, int ammount) {
        this.name = name;
        this.type = type;
        this.ammount = ammount;
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
