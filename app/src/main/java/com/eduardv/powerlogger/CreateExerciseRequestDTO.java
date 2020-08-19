package com.eduardv.powerlogger;

import com.eduardv.powerlogger.dto.ExerciseDTO;

import java.util.ArrayList;
import java.util.List;

public class CreateExerciseRequestDTO extends ExerciseDTO {
    private List<String> groupIds;

    public CreateExerciseRequestDTO(ExerciseDTO exercise) {
        this.name = exercise.name;
        this.category = exercise.category;
        this.id = exercise.id;
        this.groups = exercise.groups;
        this.groupIds = new ArrayList<>();
    }

    public CreateExerciseRequestDTO() {
    }

    public List<String> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(List<String> groupIds) {
        this.groupIds = groupIds;
    }
}
