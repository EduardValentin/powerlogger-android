package com.example.powerlogger.services;

import com.example.powerlogger.Env;
import com.example.powerlogger.MinifiedExerciseDTO;
import com.example.powerlogger.dto.CaloriesInfoDTO;
import com.example.powerlogger.dto.ExerciseDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ExerciseDataService {
    @GET(Env.BASE_API_PREFIX + "/exercises")
    Call<List<ExerciseDTO>> fetchAllExercises(@Header("Authorization") String token);

    @POST(Env.BASE_API_PREFIX + "/exercises")
    Call<ExerciseDTO> addNewExercise(@Body MinifiedExerciseDTO exerciseDTO, @Header("Authorization") String token);

    @PUT(Env.BASE_API_PREFIX + "/exercises/{id}")
    Call<ExerciseDTO> updateExercise(@Path("id") String id, @Body MinifiedExerciseDTO exerciseDTO, @Header("Authorization") String token);

    @DELETE(Env.BASE_API_PREFIX + "/exercises/{id}")
    Call<Void> deleteExercise(@Path("id") String id, @Header("Authorization") String token);
}
