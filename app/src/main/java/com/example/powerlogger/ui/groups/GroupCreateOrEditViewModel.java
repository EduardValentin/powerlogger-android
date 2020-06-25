package com.example.powerlogger.ui.groups;

import androidx.lifecycle.ViewModel;

import com.example.powerlogger.dto.ExerciseDTO;
import com.example.powerlogger.dto.GroupDTO;
import com.example.powerlogger.dto.groups.GroupAddExercisesResponse;
import com.example.powerlogger.repositories.GroupRepository;

import java.util.List;
import java.util.function.Consumer;

public class GroupCreateOrEditViewModel extends ViewModel {
    private String name;
    private List<ExerciseDTO> addedExercises;

    private final GroupRepository groupRepository = GroupRepository.getInstance();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ExerciseDTO> getAddedExercises() {
        return addedExercises;
    }

    public void setAddedExercises(List<ExerciseDTO> addedExercises) {
        this.addedExercises = addedExercises;
    }

    public void addGroup(Consumer<GroupDTO> onSuccess, Consumer<Throwable> onFail) {
        GroupDTO groupDTO = new GroupDTO(null, name);
        groupRepository.createGroup(groupDTO, createdGroup -> {
            this.addExercisesToGroup(createdGroup);
            onSuccess.accept(createdGroup);
        }, onFail);

    }

    public void editGroup(String groupId, Consumer<GroupDTO> onSuccess, Consumer<Throwable> onFail) {
        GroupDTO groupDTO = new GroupDTO(groupId, name);
        addExercisesToGroup(groupDTO, list ->
                groupRepository.updateGroup(groupId, groupDTO, onSuccess, onFail));
    }

    private void addExercisesToGroup(GroupDTO groupAdded) {
        groupRepository.addExercises(groupAdded.getId(), addedExercises, null, null);
    }

    private void addExercisesToGroup(GroupDTO groupAdded, Consumer<GroupAddExercisesResponse> onSuccess) {
        groupRepository.addExercises(groupAdded.getId(), addedExercises, onSuccess, null);
    }
}
