package com.eduardv.powerlogger.ui.groups;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eduardv.powerlogger.dto.GroupDTO;
import com.eduardv.powerlogger.repositories.GroupRepository;

import java.util.List;
import java.util.function.Consumer;

public class GroupsViewModel extends ViewModel {
    private MutableLiveData<List<GroupDTO>> groupsLiveData = new MutableLiveData<>();
    private GroupRepository groupRepository = GroupRepository.getInstance();

    public GroupsViewModel() {
        this.groupsLiveData.setValue(groupRepository.getGroupCache().getValue());
        groupRepository.getGroupCache().observeForever(groupDTOS -> this.groupsLiveData.setValue(groupDTOS));
    }

    public void removeExerciseFromGroup(String exerciseId, String groupId, Consumer<Object> onSuccess, Consumer<Throwable> onError) {
        groupRepository.removeExerciseFromGroup(groupId, exerciseId, onSuccess, onError);
    }

    public void removeGroupFromCache(int position) {
        groupRepository.removeGroupFromCache(position);
    }

    public void addGroupToCache(GroupDTO group) {
        groupRepository.addGroupToCache(group);
    }

    public LiveData<List<GroupDTO>> getGroupsLiveData() {
        return groupsLiveData;
    }
}
