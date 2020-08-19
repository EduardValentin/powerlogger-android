package com.eduardv.powerlogger.services;

import com.eduardv.powerlogger.Env;
import com.eduardv.powerlogger.dto.ExerciseCategoryDTO;
import com.eduardv.powerlogger.dto.GroupDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ExerciseCategoryDataService {

    String AUTHORIZATION = "Authorization";

    @GET(Env.BASE_API_PREFIX + "/categories")
    Call<List<ExerciseCategoryDTO>> fetchAllCategories(@Header(AUTHORIZATION) String token);
}
