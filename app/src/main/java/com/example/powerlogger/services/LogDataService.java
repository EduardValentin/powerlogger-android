package com.example.powerlogger.services;

import com.example.powerlogger.Env;
import com.example.powerlogger.dto.LogDTO;
import com.example.powerlogger.model.Log;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface LogDataService {

    @GET(Env.BASE_API_PREFIX + "/logs?date={date}")
    Call<List<LogDTO>> fetchAllLogs(@Path("date") LocalDate date);

    @GET(Env.BASE_API_PREFIX + "/logs/{id}")
    Call<LogDTO> fetchOneLog(@Path("id") int id);

    @POST(Env.BASE_API_PREFIX + "/logs")
    Call<LogDTO> postNewLog(@Body LogDTO log);
}
