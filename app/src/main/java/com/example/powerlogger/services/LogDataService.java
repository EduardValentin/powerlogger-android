package com.example.powerlogger.services;

import com.example.powerlogger.Env;
import com.example.powerlogger.dto.LogDTO;

import java.time.LocalDate;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface LogDataService {

    @GET(Env.BASE_API_PREFIX + "/logs?date={date}")
    Call<List<LogDTO>> fetchAllLogs(@Header ("Authorization") String token, @Path("date") LocalDate date);

    @GET(Env.BASE_API_PREFIX + "/logs/{id}")
    Call<LogDTO> fetchOneLog(@Header ("Authorization") String token, @Path("id") int id);

    @POST(Env.BASE_API_PREFIX + "/logs")
    Call<LogDTO> postNewLog(@Header ("Authorization") String token, @Body LogDTO log);

    @PUT(Env.BASE_API_PREFIX + "/logs/{id}")
    Call<LogDTO> updateLog(@Header ("Authorization") String token, @Path ("id") String id, @Body LogDTO log);
}
