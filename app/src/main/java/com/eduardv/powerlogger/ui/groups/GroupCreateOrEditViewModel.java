package com.eduardv.powerlogger.ui.groups;

import androidx.lifecycle.ViewModel;

import com.eduardv.powerlogger.dto.ExerciseDTO;
import com.eduardv.powerlogger.dto.GroupDTO;
import com.eduardv.powerlogger.dto.groups.GroupAddExercisesResponse;
import com.eduardv.powerlogger.repositories.GroupRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GroupCreateOrEditViewModel extends ViewModel {
    private String name;
    private List<ExerciseDTO> addedExercises;

    private final GroupRepository groupRepository = GroupRepository.getInstance();

    public GroupCreateOrEditViewModel() {
        this.addedExercises = new ArrayList<>();
    }

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
            if (!addedExercises.isEmpty()) {
                this.addExercisesToGroup(createdGroup);
            }
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
