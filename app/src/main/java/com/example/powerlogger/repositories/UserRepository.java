package com.example.powerlogger.repositories;

import android.util.Log;

import com.example.powerlogger.APIClient;
import com.example.powerlogger.dto.RegisterRequestDTO;
import com.example.powerlogger.dto.UserResponseDTO;
import com.example.powerlogger.dto.LoginRequestDTO;
import com.example.powerlogger.services.UserDataService;
import com.example.powerlogger.utils.Result;

import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class UserRepository {

    private static volatile UserRepository instance;

    private UserDataService userDataService =  APIClient.getRetrofitInstance().create(UserDataService.class);

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private String token = null;

    public String getToken() {
        return "Bearer " + token;
    }

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return token != null;
    }

    public void logout() {
        token = null;
    }

    public void login(String username, String password, Consumer<Object> callback) {

        try {
            LoginRequestDTO loginRequestDTO = new LoginRequestDTO(username, password);
            Call<UserResponseDTO> call = userDataService.signin(loginRequestDTO);

            call.enqueue(new Callback<UserResponseDTO>() {
                @Override
                public void onResponse(Call<UserResponseDTO> call, Response<UserResponseDTO> response) {
                    if(response.code() == 200) {
                        if(callback != null) {
                            callback.accept(new Result.Success<>(response.body()));
                        }
                        token = response.body().getToken();
                    }
                    else {
                        callback.accept(new Result.Error(new Exception("Incorect username or password.")));
                    }
                }

                @Override
                public void onFailure(Call<UserResponseDTO> call, Throwable t) {
                    if(callback != null) {
                        callback.accept(new Result.Error(new Exception(t)));
                    }
                    Log.e("Error at login", t.getMessage());
                    token = null;
                }
            });

        } catch (Exception e) {
            if(callback != null) {
                callback.accept(new Result.Error(e));
            }
            token = null;
        }
    }

    public void signup(RegisterRequestDTO registerRequestDTO, Consumer<Object> callback) {
        Call<UserResponseDTO> call = userDataService.signup(registerRequestDTO);
        call.enqueue(new Callback<UserResponseDTO>() {
            @Override
            public void onResponse(Call<UserResponseDTO> call, Response<UserResponseDTO> response) {
                if (response.code() == 200) {
                    callback.accept(new Result.Success<UserResponseDTO>(response.body()));
                    token = response.body().getToken();
                } else {
                    callback.accept(new Result.Error(new Exception("Something went wrong.")));
                }
            }

            @Override
            public void onFailure(Call<UserResponseDTO> call, Throwable t) {
                callback.accept(new Result.Error(new Exception("Something went wrong.")));
                Log.i("###### Error at register: ", t.getMessage());
            }
        });
    }

}
