package com.example.powerlogger.services;

import com.example.powerlogger.Env;
import com.example.powerlogger.dto.CaloriesInfoDTO;
import com.example.powerlogger.dto.ExerciseDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ExerciseDataService {
    @GET(Env.BASE_API_PREFIX + "/exercises")
    Call<List<ExerciseDTO>> fetchAllExercises(@Header("Authorization") String token);

    @POST(Env.BASE_API_PREFIX + "/exercises")
    Call<ExerciseDTO> addNewExercise(ExerciseDTO exerciseDTO, @Header("Authorization") String token);
}
