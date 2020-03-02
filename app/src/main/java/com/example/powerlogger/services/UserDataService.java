package com.example.powerlogger.services;

import com.example.powerlogger.dto.RegisterRequestDTO;
import com.example.powerlogger.dto.UserResponseDTO;
import com.example.powerlogger.dto.LoginRequestDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserDataService {

    @POST("api/v1/users/signin")
    Call<UserResponseDTO> signin(@Body LoginRequestDTO loginRequestDTO);

    @POST("api/v1/users/signup")
    Call<UserResponseDTO> signup(@Body RegisterRequestDTO registerRequestDTO);
}
