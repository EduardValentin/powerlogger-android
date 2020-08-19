package com.eduardv.powerlogger.model;

public class Log {
    private String name;
    private int intensity;

    public Log(String name, int intensity) {
        this.name = name;
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
}
