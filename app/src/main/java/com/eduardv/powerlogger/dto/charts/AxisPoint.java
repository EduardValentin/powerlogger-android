package com.eduardv.powerlogger.dto.charts;

public class AxisPoint {
    private String xPoint;
    private String yPoint;

    public AxisPoint() {
    }

    public AxisPoint(String xPoint, String yPoint) {
        this.xPoint = xPoint;
        this.yPoint = yPoint;
    }

    public String getyPoint() {
        return yPoint;
    }

    public void setyPoint(String yPoint) {
        this.yPoint = yPoint;
    }

    public String getxPoint() {
        return xPoint;
    }

    public void setxPoint(String xPoint) {
        this.xPoint = xPoint;
    }
}
