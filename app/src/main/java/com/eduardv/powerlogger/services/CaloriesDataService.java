package com.eduardv.powerlogger.services;

import com.eduardv.powerlogger.Env;
import com.eduardv.powerlogger.dto.CaloriesInfoDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface CaloriesDataService {
    @GET(Env.BASE_API_PREFIX + "/calories")
    Call<CaloriesInfoDTO> fetchCalorieInfo(@Header("Authorization") String token);
}
