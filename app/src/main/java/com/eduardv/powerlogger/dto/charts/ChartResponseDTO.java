package com.eduardv.powerlogger.dto.charts;

import java.util.List;

public class ChartResponseDTO {
    private ChartResponseStatus status;
    private List<AxisPoint> points;

    public ChartResponseDTO() {
    }

    public ChartResponseDTO(ChartResponseStatus status, List<AxisPoint> points) {
        this.status = status;
        this.points = points;
    }

    public ChartResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ChartResponseStatus status) {
        this.status = status;
    }

    public List<AxisPoint> getPoints() {
        return points;
    }

    public void setPoints(List<AxisPoint> points) {
        this.points = points;
    }
}
