package com.example.powerlogger.dto;

public class CaloriesInfoDTO {
    private int today;
//    private int weeek;

    public CaloriesInfoDTO() {
    }

    public CaloriesInfoDTO(int today) {
        this.today = today;
//        this.weeek = weeek;
    }

    public int getToday() {
        return today;
    }

    public void setToday(int today) {
        this.today = today;
    }

//    public int getWeeek() {
//        return weeek;
//    }
//
//    public void setWeeek(int weeek) {
//        this.weeek = weeek;
//    }
}
