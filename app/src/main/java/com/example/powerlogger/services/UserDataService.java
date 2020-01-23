package com.example.powerlogger.services;

import com.example.powerlogger.dto.RegisterRequestDTO;
import com.example.powerlogger.dto.UserResponseDTO;
import com.example.powerlogger.dto.LoginRequestDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserDataService {

    @POST("users/signin")
    Call<UserResponseDTO> signin(@Body LoginRequestDTO loginRequestDTO);

    @POST("users/signup")
    Call<UserResponseDTO> signup(@Body RegisterRequestDTO registerRequestDTO);
}
