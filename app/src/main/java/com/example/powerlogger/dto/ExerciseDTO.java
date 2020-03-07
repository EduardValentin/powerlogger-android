package com.example.powerlogger.dto;

import com.example.powerlogger.model.ExerciseCategory;

import com.example.powerlogger.ui.logger.Identifiable;

import java.util.List;
import java.util.UUID;

public class ExerciseDTO implements Identifiable {
    private UUID id;
    private String name;
    private ExerciseCategory category;
    private List<GroupDTO> groups;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    @Override
    public String toString() {
        return name;
    }
}

