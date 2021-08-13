package com.eduardv.powerlogger.dto.charts;

public class AxisPoint {
    private String xpoint;
    private String ypoint;

    public AxisPoint() {
    }

    public AxisPoint(String xPoint, String ypoint) {
        this.xpoint = xPoint;
        this.ypoint = ypoint;
    }

    public String getYpoint() {
        return ypoint;
    }

    public void setYpoint(String ypoint) {
        this.ypoint = ypoint;
    }

    public String getXpoint() {
        return xpoint;
    }

    public void setXpoint(String xpoint) {
        this.xpoint = xpoint;
    }
}
