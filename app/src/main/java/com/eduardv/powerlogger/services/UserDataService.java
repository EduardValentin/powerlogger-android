package com.eduardv.powerlogger.services;

import com.eduardv.powerlogger.dto.charts.ChartResponseDTO;
import com.eduardv.powerlogger.dto.charts.UserStatsDTO;
import com.eduardv.powerlogger.dto.user.ForgotPasswordRequestDTO;
import com.eduardv.powerlogger.dto.user.GoogleUserAuthenticationDTO;
import com.eduardv.powerlogger.dto.user.LoginRequestDTO;
import com.eduardv.powerlogger.dto.user.RegisterRequestDTO;
import com.eduardv.powerlogger.dto.user.UserAuthenticationResponseDTO;
import com.eduardv.powerlogger.dto.user.UserDTO;
import com.eduardv.powerlogger.dto.user.UserSettingsDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserDataService {

    @GET("api/v1/charts/{username}/userStats")
    Call<UserStatsDTO> getUserStats(@Header("Authorization") String token, @Path("username") String username);

    @GET("api/v1/charts/{username}/kcalLastWeek")
    Call<ChartResponseDTO> getKcalLastWeek(@Header("Authorization") String token, @Path("username") String username);

    @GET("api/v1/charts/{username}/getTopTypes")
    Call<ChartResponseDTO> getTopExerciseTypes(@Header("Authorization") String token, @Path("username") String username);

    @GET("api/v1/charts/{username}/getTopGroups")
    Call<ChartResponseDTO> getTopGroups(@Header("Authorization") String token, @Path("username") String username);

    @POST("api/v1/signin")
    Call<UserAuthenticationResponseDTO> signin(@Body LoginRequestDTO loginRequestDTO);

    @POST("api/v1/users/resetPasswordToken")
    Call<Void> sendResetPasswordEmail(@Body ForgotPasswordRequestDTO requestDTO);

    @POST("api/v1/signup")
    Call<UserAuthenticationResponseDTO> signup(@Body RegisterRequestDTO registerRequestDTO);

    @POST("api/v1/users/{username}/token-validation")
    Call<Void> validateToken(@Header("Authorization") String token, @Path("username") String username);

    @POST("api/v1/google-account")
    Call<GoogleUserAuthenticationDTO> validateGoogleAccount(@Header("Google-Token") String token);

    @POST("api/v1/users/{username}/settings")
    Call<UserDTO> setUserSettings(@Header("Authorization") String token, @Path("username") String username, @Body UserSettingsDTO settingsDTO);

    @PUT("api/v1/users/{username}")
    Call<UserDTO> updateUser(@Header("Authorization") String token, @Path("username") String username, @Body UserDTO userDTO);

    @PATCH("api/v1/users/{username}/settings")
    Call<UserSettingsDTO> patchUnits(@Header("Authorization") String token, @Path("username") String username, @Body UserSettingsDTO userSettingsDTO);
}
