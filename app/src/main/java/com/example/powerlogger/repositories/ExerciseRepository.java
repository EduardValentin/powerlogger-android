package com.example.powerlogger.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.powerlogger.APIClient;
import com.example.powerlogger.dto.ExerciseDTO;
import com.example.powerlogger.services.ExerciseDataService;
import com.example.powerlogger.utils.APICallsUtils;

import java.util.ArrayList;
import java.util.List;
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
        this.userRepository = UserRepository.getInstance();
        this.exerciseDataService = APIClient.getRetrofitInstance().create(ExerciseDataService.class);
    }

    public LiveData<List<ExerciseDTO>> getExerciseCache() {
        return exerciseCache;
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

    public ExerciseDTO addNewExercise(ExerciseDTO exerciseDTO, Consumer<Object> handleSuccess, Consumer<Throwable> handleError) {
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
}
