package com.example.powerlogger.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.powerlogger.APIClient;
import com.example.powerlogger.dto.GroupDTO;
import com.example.powerlogger.services.GroupDataService;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupRepository {

    private static GroupRepository _instance;
    private final UserRepository userRepository = UserRepository.getInstance();

    private GroupDataService groupDataService;
    private MutableLiveData<List<GroupDTO>> groupCache = new MutableLiveData<>();


    public static GroupRepository getInstance() {
        if(_instance == null) {
           _instance = new GroupRepository();
        }
        return _instance;
    }

    public void fetchGroups(Consumer<Object> onSuccess, Consumer<Throwable> onError) {
        groupDataService.fetchAllGroups(userRepository.getToken()).enqueue(new Callback<List<GroupDTO>>() {
            @Override
            public void onResponse(Call<List<GroupDTO>> call, Response<List<GroupDTO>> response) {
                groupCache.setValue(response.body());
                if (onSuccess != null) {
                    onSuccess.accept(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<GroupDTO>> call, Throwable t) {
                if(onError != null) {
                    onError.accept(t);
                }
            }
        });
    }

    public void createGroup(GroupDTO groupDTO, Consumer<Object> onSuccess, Consumer<Throwable> onError) {
        groupDataService.postNewGroup(userRepository.getToken(), groupDTO).enqueue(new Callback<GroupDTO>() {
            @Override
            public void onResponse(Call<GroupDTO> call, Response<GroupDTO> response) {
                List<GroupDTO> old = groupCache.getValue();
                old.add(response.body());
                groupCache.setValue(old);

                if (onSuccess != null) {
                    onSuccess.accept(response.body());
                }
            }

            @Override
            public void onFailure(Call<GroupDTO> call, Throwable t) {
                if (onError != null) {
                    onError.accept(t);
                }
            }
        });
    }

    public void updateGroup(String groupId, GroupDTO groupDTO, Consumer<Object> onSuccess, Consumer<Throwable> onError) {
        groupDataService.updateGroup(userRepository.getToken(), groupId, groupDTO).enqueue(new Callback<GroupDTO>() {
            @Override
            public void onResponse(Call<GroupDTO> call, Response<GroupDTO> response) {
                int index = 0;
                List<GroupDTO> old = groupCache.getValue();
                for (GroupDTO g: old) {
                    if (g.getId().equals(groupId)) {
                        break;
                    }
                    index++;
                }

                old.remove(index);
                old.add(response.body());
                groupCache.setValue(old);
            }

            @Override
            public void onFailure(Call<GroupDTO> call, Throwable t) {
                if (onError != null) {
                    onError.accept(t);
                }
            }
        });
    }

    public LiveData<List<GroupDTO>> getGroupCache() {
        return groupCache;
    }

    private GroupRepository() {
        List<GroupDTO> mock = new ArrayList<>();
        this.groupCache.setValue(mock);
        this.groupDataService = APIClient.getRetrofitInstance().create(GroupDataService.class);
    }
}
