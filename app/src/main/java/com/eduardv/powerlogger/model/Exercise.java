package com.eduardv.powerlogger.model;

import com.eduardv.powerlogger.dto.GroupDTO;

import java.util.List;

public class Exercise {

    private int id;
    private String name;
    private ExerciseCategory category;
    private List<GroupDTO> groups;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ExerciseCategory getCategory() {
        return category;
    }

    public void setCategory(ExerciseCategory category) {
        this.category = category;
    }

    public List<GroupDTO> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupDTO> groups) {
        this.groups = groups;
    }
}