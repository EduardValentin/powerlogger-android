package com.example.powerlogger.dto;

import androidx.annotation.NonNull;

import com.example.powerlogger.lib.SelectableItem;
import java.util.List;

public class GroupDTO implements SelectableItem {
    private String id;
    private String name;
    private List<LogDTO> logs;
    private boolean selected;

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

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
