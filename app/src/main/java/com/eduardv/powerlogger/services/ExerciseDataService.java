package com.eduardv.powerlogger.services;

import com.eduardv.powerlogger.Env;
import com.eduardv.powerlogger.CreateExerciseRequestDTO;
import com.eduardv.powerlogger.dto.ExerciseDTO;

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
    @GET(Env.BASE_API_PREFIX + "/users/{username}/exercises")
    Call<List<ExerciseDTO>> fetchAllExercises(@Path("username") String username, @Header("Authorization") String token);

    @POST(Env.BASE_API_PREFIX + "/users/{username}/exercises")
    Call<ExerciseDTO> addNewExercise(@Path("username") String username, @Body CreateExerciseRequestDTO exerciseDTO, @Header("Authorization") String token);

    @PUT(Env.BASE_API_PREFIX + "/users/{username}/exercises/{id}")
    Call<ExerciseDTO> updateExercise(@Path("username") String username, @Path("id") String id, @Body CreateExerciseRequestDTO exerciseDTO, @Header("Authorization") String token);

    @DELETE(Env.BASE_API_PREFIX + "/users/{username}/exercises/{id}")
    Call<Void> deleteExercise(@Path("username") String username, @Path("id") String id, @Header("Authorization") String token);
}
