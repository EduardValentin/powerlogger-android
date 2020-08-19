package com.eduardv.powerlogger.dto.charts;

public class UserStatsDTO {
    private String allTimePerformanceMessage;
    private String weekPerformanceMessage;

    public UserStatsDTO(String allTimePerformanceMessage, String weekPerformanceMessage) {
        this.allTimePerformanceMessage = allTimePerformanceMessage;
        this.weekPerformanceMessage = weekPerformanceMessage;
    }

    public String getAllTimePerformanceMessage() {
        return allTimePerformanceMessage;
    }

    public void setAllTimePerformanceMessage(String allTimePerformanceMessage) {
        this.allTimePerformanceMessage = allTimePerformanceMessage;
    }

    public String getWeekPerformanceMessage() {
        return weekPerformanceMessage;
    }

    public void setWeekPerformanceMessage(String weekPerformanceMessage) {
        this.weekPerformanceMessage = weekPerformanceMessage;
    }
}
