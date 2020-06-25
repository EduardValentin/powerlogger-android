package com.example.powerlogger.dto;

public class UserActiveStatsDTO {
    private String peoplePercent;
    private String weekPercent;
    private String weekAdverb;

    public String getPeoplePercent() {
        return peoplePercent;
    }

    public void setPeoplePercent(String peoplePercent) {
        this.peoplePercent = peoplePercent;
    }

    public String getWeekPercent() {
        return weekPercent;
    }

    public void setWeekPercent(String weekPercent) {
        this.weekPercent = weekPercent;
    }

    public String getWeekAdverb() {
        return weekAdverb;
    }

    public void setWeekAdverb(String weekAdverb) {
        this.weekAdverb = weekAdverb;
    }
}
