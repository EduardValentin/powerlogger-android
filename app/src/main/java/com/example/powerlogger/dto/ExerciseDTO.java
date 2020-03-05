package com.example.powerlogger.dto;

import java.util.List;
import java.util.UUID;

public class ExerciseDTO {
    private UUID id;
    private String name;
    private String category;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
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

