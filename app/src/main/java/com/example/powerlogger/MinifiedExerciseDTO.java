package com.example.powerlogger;

import com.example.powerlogger.dto.ExerciseDTO;

import java.util.ArrayList;
import java.util.List;

public class MinifiedExerciseDTO extends ExerciseDTO {
    private List<String> groupIds;

    public MinifiedExerciseDTO(ExerciseDTO exercise) {
        this.name = exercise.name;
        this.category = exercise.category;
        this.id = exercise.id;
        this.groups = exercise.groups;
        this.groupIds = new ArrayList<>();
    }

    public MinifiedExerciseDTO() {
    }

    public List<String> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(List<String> groupIds) {
        this.groupIds = groupIds;
    }
}
