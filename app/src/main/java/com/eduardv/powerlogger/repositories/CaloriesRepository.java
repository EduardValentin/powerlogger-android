package com.eduardv.powerlogger.repositories;

import androidx.lifecycle.MutableLiveData;

import com.eduardv.powerlogger.APIClient;
import com.eduardv.powerlogger.dto.CaloriesInfoDTO;
import com.eduardv.powerlogger.services.CaloriesDataService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CaloriesRepository {
    private static CaloriesRepository INSTANCE;

    private MutableLiveData<CaloriesInfoDTO> calorieCache;
    private UserRepository userRepository = UserRepository.getInstance();
    private CaloriesDataService caloriesDataService;

    private CaloriesRepository() {
        this.caloriesDataService = APIClient.getRetrofitInstance().create(CaloriesDataService.class);
    }

    public static CaloriesRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CaloriesRepository();
        }

        return INSTANCE;
    }

    public void fetchCalorieInfo() {
        caloriesDataService.fetchCalorieInfo(userRepository.getToken()).enqueue(new Callback<CaloriesInfoDTO>() {
            @Override
            public void onResponse(Call<CaloriesInfoDTO> call, Response<CaloriesInfoDTO> response) {
                calorieCache.setValue(response.body());
            }

            @Override
            public void onFailure(Call<CaloriesInfoDTO> call, Throwable t) {

            }
        });

    }

    public MutableLiveData<CaloriesInfoDTO> getCalorieCache() {
        return calorieCache;
    }
}
