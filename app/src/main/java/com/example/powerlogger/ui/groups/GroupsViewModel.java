package com.example.powerlogger.ui.groups;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.powerlogger.dto.GroupDTO;
import com.example.powerlogger.repositories.GroupRepository;

import java.util.List;

public class GroupsViewModel extends ViewModel {
    private MutableLiveData<List<GroupDTO>> groupsLiveData = new MutableLiveData<>();
    private GroupRepository groupRepository = GroupRepository.getInstance();

    public GroupsViewModel() {
        groupRepository.fetchGroups(null, null);
        this.groupsLiveData.setValue(groupRepository.getGroupCache().getValue());
        groupRepository.getGroupCache().observeForever(groupDTOS -> this.groupsLiveData.setValue(groupDTOS));
    }

    public LiveData<List<GroupDTO>> getGroupsLiveData() {
        return groupsLiveData;
    }
}
