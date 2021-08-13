package com.eduardv.powerlogger.services;

import com.eduardv.powerlogger.Env;
import com.eduardv.powerlogger.dto.logs.IntensityType;
import com.eduardv.powerlogger.dto.logs.LogDTO;

import java.time.LocalDate;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface LogDataService {

    @GET(Env.BASE_API_PREFIX + "/users/{username}/logs")
    Call<List<LogDTO>> fetchAllLogs(@Path("username") String username, @Header("Authorization") String token, @Query("date") LocalDate date);

    @GET(Env.BASE_API_PREFIX + "/users/{username}/logs/calories")
    Call<LogDTO> getCaloriesForLog(@Path("username") String username, @Header("Authorization") String token,
                                   @Query("category") String category,
                                   @Query("intensity") float intensity,
                                   @Query("intensityType") IntensityType intensityType);

    @POST(Env.BASE_API_PREFIX + "/users/{username}/exercises/{exerciseId}/logs")
    Call<LogDTO> postNewLog(@Path("username") String username, @Header("Authorization") String token, @Path("exerciseId") String exerciseId, @Body LogDTO log);

    @POST(Env.BASE_API_PREFIX + "/users/{username}/logs/batch")
    Call<List<LogDTO>> sendBatchLogs(@Path("username") String username, @Header("Authorization") String token, @Body List<LogDTO> log);

    @PUT(Env.BASE_API_PREFIX + "/users/{username}/logs/{id}")
    Call<LogDTO> updateLog(@Path("username") String username, @Header("Authorization") String token, @Path("id") String id, @Body LogDTO log);

    @DELETE(Env.BASE_API_PREFIX + "/users/{username}/logs/{id}")
    Call<Void> delete(@Path("username") String username, @Header("Authorization") String token, @Path("id") String id);
}
