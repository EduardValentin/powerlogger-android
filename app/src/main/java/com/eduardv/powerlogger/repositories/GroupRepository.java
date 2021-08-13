package com.eduardv.powerlogger.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eduardv.powerlogger.APIClient;
import com.eduardv.powerlogger.dto.ExerciseDTO;
import com.eduardv.powerlogger.dto.GroupDTO;
import com.eduardv.powerlogger.dto.groups.GroupAddExercisesResponse;
import com.eduardv.powerlogger.lib.ApiGenericCallback;
import com.eduardv.powerlogger.services.GroupDataService;
import com.eduardv.powerlogger.utils.APICallsUtils;
import com.eduardv.powerlogger.utils.ArrayUtills;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class GroupRepository {

    private static GroupRepository _instance;
    private final UserRepository userRepository = UserRepository.getInstance();
    private final ExerciseRepository exerciseRepository = ExerciseRepository.getInstance();

    private final GroupDataService groupDataService;
    private final MutableLiveData<List<GroupDTO>> groupCache = new MutableLiveData<>();


    public static GroupRepository getInstance() {
        if (_instance == null) {
            _instance = new GroupRepository();
        }
        return _instance;
    }

    public void fetchGroups(Consumer<List<GroupDTO>> onSuccess, Consumer<Throwable> onError) {
        Consumer<List<GroupDTO>> onSuccessResponse = res -> {
            groupCache.setValue(res);
            APICallsUtils.getHandlerOrDefault(onSuccess).accept(res);
        };

        groupDataService.fetchAllGroups(userRepository.getUser().getUsername(), userRepository.getToken()).enqueue(
                new ApiGenericCallback<>(onSuccessResponse, onError, "Fetch Groups"));
    }

    public void createGroup(GroupDTO groupDTO, Consumer<GroupDTO> onSuccess, Consumer<Throwable> onError) {
        Consumer<GroupDTO> onSuccessResponse = resp -> {

            List<GroupDTO> old = groupCache.getValue();
            assert old != null;

            old.add(resp);
            groupCache.setValue(old);
            APICallsUtils.getHandlerOrDefault(onSuccess).accept(resp);
        };

        groupDataService.postNewGroup(userRepository.getUser().getUsername(), userRepository.getToken(), groupDTO).enqueue(
                new ApiGenericCallback<>(onSuccessResponse, onError, "Create Group"));
    }

    public void updateGroup(String groupId, GroupDTO groupDTO, Consumer<GroupDTO> onSuccess, Consumer<Throwable> onError) {
        Consumer<GroupDTO> onSuccessResponse = response -> {

            int index = 0;
            List<GroupDTO> old = groupCache.getValue();
            assert old != null;

            for (GroupDTO g : old) {
                if (g.getId().equals(groupId)) {
                    break;
                }
                index++;
            }

            old.remove(index);
            old.add(response);

            groupCache.setValue(old);
            onSuccess.accept(groupDTO);
        };

        groupDataService.updateGroup(userRepository.getUser().getUsername(), userRepository.getToken(), groupId, groupDTO).enqueue(
                new ApiGenericCallback<>(onSuccessResponse, onError, "Groups Update API"));
    }

    public void removeExerciseFromGroup(String groupId, String exerciseId, Consumer<Object> onSuccess, Consumer<Throwable> onError) {
        Consumer<GroupDTO> onSuccessResponse = res -> {
            APICallsUtils.getHandlerOrDefault(onSuccess)
                    .accept(res);

            int index = ArrayUtills.findIndexByPredicate(groupCache.getValue(), g -> g.getId().equals(groupId));
            groupCache.getValue().remove(index);
        };

        groupDataService.removeExerciseFromGroup(userRepository.getUser().getUsername(), userRepository.getToken(), groupId, exerciseId).enqueue(
                new ApiGenericCallback<>(onSuccessResponse, onError, "Remove Exercise from Group")
        );
    }

    public void removeGroup(String groupId, Consumer<Void> onSuccess, Consumer<Throwable> onError) {
        List<GroupDTO> groups = this.groupCache.getValue();
        groups.removeIf(g -> g.getId().equals(groupId));
        groupCache.setValue(groups);

        Consumer<Void> processSuccess = v -> {
            exerciseRepository.fetchExercises(null, null);
            APICallsUtils.getHandlerOrDefault(onSuccess).accept(v);
        };

        groupDataService.removeGroup(userRepository.getUser().getUsername(), userRepository.getToken(), groupId).enqueue(
                new ApiGenericCallback<>(processSuccess, onError, "REMOVE_GROUP")
        );
    }

    public void addExercises(String groupId, List<ExerciseDTO> exercises, Consumer<GroupAddExercisesResponse> onSuccess, Consumer<Throwable> onFail) {
        Consumer<GroupAddExercisesResponse> processSucess = response -> {
            exerciseRepository.fetchExercises(null, null);
            APICallsUtils.getHandlerOrDefault(onSuccess).accept(response);
        };

        groupDataService.addExercises(userRepository.getUser().getUsername(), userRepository.getToken(), groupId, exercises.stream()
                .map(ExerciseDTO::getId)
                .collect(Collectors.toList()))
                .enqueue(new ApiGenericCallback<>(processSucess, onFail, "ADD_EXERCISES_TO_GROUP"));
    }

    public LiveData<List<GroupDTO>> getGroupCache() {
        return groupCache;
    }

    private GroupRepository() {
        List<GroupDTO> mock = new ArrayList<>();
        this.groupCache.setValue(mock);
        this.groupDataService = APIClient.getRetrofitInstance().create(GroupDataService.class);
    }

    public void removeGroupFromCache(int position) {
        List<GroupDTO> groups = groupCache.getValue();
        groups.remove(position);
        groupCache.setValue(groups);
    }

    public void addGroupToCache(GroupDTO group) {
        List<GroupDTO> groups = groupCache.getValue();
        groups.add(group);
        groupCache.setValue(groups);
    }

    private GroupDTO getGroupWithId(String groupId) {
        return groupCache.getValue().stream()
                .filter(g -> g.getId().equals(groupId))
                .limit(1)
                .collect(Collectors.toList()).get(0);
    }
}
