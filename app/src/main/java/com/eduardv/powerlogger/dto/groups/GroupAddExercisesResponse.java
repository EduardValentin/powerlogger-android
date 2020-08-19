package com.eduardv.powerlogger.dto.groups;

import com.eduardv.powerlogger.dto.ExerciseDTO;

import java.util.List;

public class GroupAddExercisesResponse {
    private GroupsAddExercisesStatus status;
    private List<ExerciseDTO> exercises;

    public GroupAddExercisesResponse(GroupsAddExercisesStatus status, List<ExerciseDTO> exercises) {
        this.status = status;
        this.exercises = exercises;
    }

    public List<ExerciseDTO> getExercises() {
        return exercises;
    }

    public void setExercises(List<ExerciseDTO> exercises) {
        this.exercises = exercises;
    }

    public GroupsAddExercisesStatus getStatus() {
        return status;
    }

    public void setStatus(GroupsAddExercisesStatus status) {
        this.status = status;
    }
}
