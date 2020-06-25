package com.example.powerlogger.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.powerlogger.APIClient;
import com.example.powerlogger.dto.ExerciseDTO;
import com.example.powerlogger.dto.GroupDTO;
import com.example.powerlogger.dto.groups.GroupAddExercisesResponse;
import com.example.powerlogger.lib.ApiGenericCallback;
import com.example.powerlogger.services.GroupDataService;
import com.example.powerlogger.utils.APICallsUtils;
import com.example.powerlogger.utils.ArrayUtills;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

        groupDataService.fetchAllGroups(userRepository.getToken()).enqueue(
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

        groupDataService.postNewGroup(userRepository.getToken(), groupDTO).enqueue(
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

        groupDataService.updateGroup(userRepository.getToken(), groupId, groupDTO).enqueue(
                new ApiGenericCallback<>(onSuccessResponse, onError, "Groups Update API"));
    }

    public void removeExerciseFromGroup(String groupId, String exerciseId, Consumer<Object> onSuccess, Consumer<Throwable> onError) {
        Consumer<GroupDTO> onSuccessResponse = res -> {
            APICallsUtils.getHandlerOrDefault(onSuccess)
                    .accept(res);

            int index = ArrayUtills.findIndexByPredicate(groupCache.getValue(), g -> g.getId().equals(groupId));
            groupCache.getValue().remove(index);
        };

        groupDataService.removeExerciseFromGroup(userRepository.getToken(), groupId, exerciseId).enqueue(
                new ApiGenericCallback<>(onSuccessResponse, onError, "Remove Exercise from Group")
        );
    }

    public void removeGroup(String groupId, Consumer<Void> onSuccess, Consumer<Throwable> onError) {
        Consumer<Void> processSuccess = r -> {
            List<GroupDTO> groups = this.groupCache.getValue();
            groups.removeIf(g -> g.getId().equals(groupId));
            groupCache.setValue(groups);

            APICallsUtils.getHandlerOrDefault(onSuccess).accept(r);
        };

        groupDataService.removeGroup(userRepository.getToken(), groupId).enqueue(
                new ApiGenericCallback<>(processSuccess, onError, "REMOVE_GROUP")
        );
    }

    public void addExercises(String groupId, List<ExerciseDTO> exercises, Consumer<GroupAddExercisesResponse> onSuccess, Consumer<Throwable> onFail) {
        Consumer<GroupAddExercisesResponse> processSucess = response -> {
            exerciseRepository.fetchExercises(null, null);
            APICallsUtils.getHandlerOrDefault(onSuccess).accept(response);
        };

        groupDataService.addExercises(userRepository.getToken(), groupId,
                exercises.stream().map(ExerciseDTO::getId).collect(Collectors.toList()))
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

    private GroupDTO getGroupWithId(String groupId) {
        return groupCache.getValue().stream()
                .filter(g -> g.getId().equals(groupId))
                .limit(1)
                .collect(Collectors.toList()).get(0);
    }
}
