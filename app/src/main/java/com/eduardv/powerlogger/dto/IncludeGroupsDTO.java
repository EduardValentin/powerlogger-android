package com.eduardv.powerlogger.dto;

import java.util.Date;
import java.util.List;

public class IncludeGroupsDTO {
    private Date date;
    private List<String> checkedGroupsIds;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setCheckedGroupsIds(List<String> checkedGroupsIds) {
        this.checkedGroupsIds = checkedGroupsIds;
    }

    public List<String> getCheckedGroupsIds() {
        return checkedGroupsIds;
    }

    public IncludeGroupsDTO() {
    }

    public IncludeGroupsDTO(Date date, List<String> checkedGroupsIds) {
        this.date = date;
        this.checkedGroupsIds = checkedGroupsIds;
    }
}
