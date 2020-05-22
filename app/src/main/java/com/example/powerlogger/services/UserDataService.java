package com.example.powerlogger.services;

import com.example.powerlogger.dto.user.GoogleUserAuthenticationDTO;
import com.example.powerlogger.dto.user.RegisterRequestDTO;
import com.example.powerlogger.dto.user.UserAuthenticationResponseDTO;
import com.example.powerlogger.dto.user.LoginRequestDTO;
import com.example.powerlogger.dto.user.UserDTO;
import com.example.powerlogger.dto.user.UserSettingsDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserDataService {

    @POST("api/v1/users/signin")
    Call<UserAuthenticationResponseDTO> signin(@Body LoginRequestDTO loginRequestDTO);

    @POST("api/v1/users/signup")
    Call<UserAuthenticationResponseDTO> signup(@Body RegisterRequestDTO registerRequestDTO);

    @POST("api/v1/users/{username}/token-validation")
    Call<Void> validateToken(@Header("Authorization") String token, @Path("username") String username);

    @POST("api/v1/users/google-account")
    Call<GoogleUserAuthenticationDTO> validateGoogleAccount(@Header("Google-Token") String token);

    @POST("api/v1/users/{username}/settings")
    Call<UserDTO> setUserSettings(@Header("Authorization") String token, @Path("username") String username, @Body UserSettingsDTO settingsDTO);

    @PUT("api/v1/users/{username}")
    Call<UserDTO> updateUser(@Header("Authorization") String token, @Path("username") String username, @Body UserDTO userDTO);

    @PUT("api/v1/users/{username}/units")
    Call<UserSettingsDTO> patchUnits(@Header("Authorization") String token, @Path("username") String username, @Body UserSettingsDTO userSettingsDTO);
}
