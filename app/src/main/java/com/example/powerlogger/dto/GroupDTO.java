package com.example.powerlogger.dto;

import androidx.annotation.NonNull;

import java.util.List;

public class GroupDTO {
    private String id;
    private String name;
    private List<LogDTO> logs;

    public GroupDTO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public GroupDTO() {
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NonNull
    @Override
    public String toString() {
        return this.name;
    }

    public List<LogDTO> getLogsList() {
        return logs;
    }

    public void setLogsList(List<LogDTO> logsList) {
        this.logs = logsList;
    }

    public void setName(String name) {
        this.name = name;
    }
}
