package com.eduardv.powerlogger.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eduardv.powerlogger.APIClient;
import com.eduardv.powerlogger.CreateExerciseRequestDTO;
import com.eduardv.powerlogger.dto.ExerciseDTO;
import com.eduardv.powerlogger.dto.GroupDTO;
import com.eduardv.powerlogger.services.ExerciseDataService;
import com.eduardv.powerlogger.utils.APICallsUtils;
import com.eduardv.powerlogger.utils.ArrayUtills;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExerciseRepository {
    private static ExerciseRepository INSTANCE;
    private MutableLiveData<List<ExerciseDTO>> exerciseCache;
    private ExerciseDataService exerciseDataService;
    private final UserRepository userRepository;

    private ExerciseRepository() {
        this.exerciseCache = new MutableLiveData<>();
        this.exerciseCache.setValue(new ArrayList<>());

        this.userRepository = UserRepository.getInstance();
        this.exerciseDataService = APIClient.getRetrofitInstance().create(ExerciseDataService.class);
    }

    public LiveData<List<ExerciseDTO>> getExerciseCache() {
        return exerciseCache;
    }

    public void updateCache(List<ExerciseDTO> exercises) {
        exerciseCache.setValue(exercises);
    }

    // To avoid O(N^2) searching for selected items
    public static Map<String, Integer> getMappingFromExerciseIdToIndex(List<ExerciseDTO> exercises) {
        Map<String, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < exercises.size(); i++) {
            indexMap.put(exercises.get(i).getId(), i);
        }
        return indexMap;
    }

    public static ExerciseRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ExerciseRepository();
        }

        return INSTANCE;
    }

    public void fetchExercises(Consumer<Object> handleSuccess, Consumer<Throwable> handleError) {
        exerciseDataService.fetchAllExercises(userRepository.getToken()).enqueue(new Callback<List<ExerciseDTO>>() {
            @Override
            public void onResponse(Call<List<ExerciseDTO>> call, Response<List<ExerciseDTO>> response) {
                if (response.code() == 200) {
                    exerciseCache.setValue(response.body());
                    APICallsUtils.getHandlerOrDefault(handleSuccess).accept(response.body());
                } else {
                    Log.e("FETCH EXERCISES ERROR", "Error while fetching exercises for user: " + userRepository.getToken());
                    exerciseCache.setValue(new ArrayList<>());
                    APICallsUtils.getHandlerOrDefault(handleError).accept(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(Call<List<ExerciseDTO>> call, Throwable t) {
                APICallsUtils.getHandlerOrDefault(handleError).accept(t);
            }
        });
    }

    public ExerciseDTO addNewExercise(CreateExerciseRequestDTO exerciseDTO, Consumer<Object> handleSuccess, Consumer<Throwable> handleError) {
        exerciseDataService.addNewExercise(exerciseDTO, userRepository.getToken()).enqueue(new Callback<ExerciseDTO>() {
            @Override
            public void onResponse(Call<ExerciseDTO> call, Response<ExerciseDTO> response) {
                if (response.code() == 200) {
                    exerciseCache.getValue().add(response.body());
                    APICallsUtils.getHandlerOrDefault(handleSuccess).accept(response.body());
                } else {
                    Log.e("ADD EXERCISE ERROR", "Error while adding exercise for user: " + userRepository.getToken());
                    APICallsUtils.getHandlerOrDefault(handleError).accept(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(Call<ExerciseDTO> call, Throwable t) {
                APICallsUtils.getHandlerOrDefault(handleError).accept(t);
            }
        });
        return exerciseDTO;
    }

    public void updateExercise(String id, CreateExerciseRequestDTO exerciseDTO, Consumer<Object> handleSuccess, Consumer<Throwable> handleError) {
        exerciseDataService.updateExercise(id, exerciseDTO, userRepository.getToken()).enqueue(new Callback<ExerciseDTO>() {
            @Override
            public void onResponse(Call<ExerciseDTO> call, Response<ExerciseDTO> response) {
                if (response.code() == 200) {
                    int index = ArrayUtills.findIndexByPredicate(exerciseCache.getValue(), ex -> ex.getId().equals(id));

                    List<ExerciseDTO> oldList = exerciseCache.getValue();
                    oldList.set(index, response.body());

                    exerciseCache.setValue(oldList);
                    APICallsUtils.getHandlerOrDefault(handleSuccess).accept(response.body());
                } else {
                    Log.e("UPDATE EXERCISE ERROR", "Error while updating exercise for user: " + userRepository.getToken());
                    APICallsUtils.getHandlerOrDefault(handleError).accept(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(Call<ExerciseDTO> call, Throwable t) {
                APICallsUtils.getHandlerOrDefault(handleError).accept(t);
            }
        });
    }


    public void deleteExercise(String id, Consumer<Object> handleSuccess, Consumer<Throwable> handleError) {
        exerciseDataService.deleteExercise(id, userRepository.getToken()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    List<ExerciseDTO> list = exerciseCache.getValue();
                    list.removeIf(exercise -> exercise.getId().toString().equals(id));
                    exerciseCache.setValue(list);

                    APICallsUtils.getHandlerOrDefault(handleSuccess).accept(response.body());
                } else {
                    Log.e("DELETE EXERCISE NO SUCCESS", "No success while deleting exercise for user: " + userRepository.getToken());
                    APICallsUtils.getHandlerOrDefault(handleError).accept(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("DELETE EXERCISE ERROR", "Error while deleting exercise for user: " + userRepository.getToken());
                APICallsUtils.getHandlerOrDefault(handleError).accept(t);
            }
        });
    }

    public void removeExerciseFromCache(int position) {
        List<ExerciseDTO> exercises = exerciseCache.getValue();
        exercises.remove(position);
        exerciseCache.setValue(exercises);
    }

    public void addExerciseToCache(ExerciseDTO group) {
        List<ExerciseDTO> exercises = exerciseCache.getValue();
        exercises.add(group);
        exerciseCache.setValue(exercises);
    }
}
