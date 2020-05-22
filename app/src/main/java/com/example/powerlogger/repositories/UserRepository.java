package com.example.powerlogger.repositories;

import android.util.Log;

import com.example.powerlogger.APIClient;
import com.example.powerlogger.dto.user.GoogleUserAuthenticationDTO;
import com.example.powerlogger.dto.user.RegisterRequestDTO;
import com.example.powerlogger.dto.user.UserAuthenticationResponseDTO;
import com.example.powerlogger.dto.user.LoginRequestDTO;
import com.example.powerlogger.dto.user.UserDTO;
import com.example.powerlogger.dto.user.UserSettingsDTO;
import com.example.powerlogger.lib.ApiGenericCallback;
import com.example.powerlogger.services.UserDataService;
import com.example.powerlogger.utils.Result;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class UserRepository {

    private static UserRepository instance;

    private UserDataService userDataService = APIClient.getRetrofitInstance().create(UserDataService.class);

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore

    private String token;
    private UserDTO user;

    public String getToken() {
        return token;
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

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public void login(String username, String password, Consumer<Object> callback) {

        try {
            LoginRequestDTO loginRequestDTO = new LoginRequestDTO(username, password);
            Call<UserAuthenticationResponseDTO> call = userDataService.signin(loginRequestDTO);

            call.enqueue(new Callback<UserAuthenticationResponseDTO>() {
                @Override
                public void onResponse(Call<UserAuthenticationResponseDTO> call, Response<UserAuthenticationResponseDTO> response) {
                    UserAuthenticationResponseDTO body = response.body();

                    if (response.code() == 200) {
                        if (callback != null) {
                            callback.accept(new Result.Success<>(body));
                        }
                        token = body.getToken();
                        user = body.getUser();
                    } else {
                        callback.accept(new Result.Error(new Exception("Incorect username or password.")));
                    }
                }

                @Override
                public void onFailure(Call<UserAuthenticationResponseDTO> call, Throwable t) {
                    Log.e("Error at login", t.getMessage());
                }
            });

        } catch (Exception e) {
            if (callback != null) {
                callback.accept(new Result.Error(e));
            }
        }
    }

    public void signup(RegisterRequestDTO registerRequestDTO, Consumer<Object> callback) {
        Call<UserAuthenticationResponseDTO> call = userDataService.signup(registerRequestDTO);
        call.enqueue(new Callback<UserAuthenticationResponseDTO>() {
            @Override
            public void onResponse(Call<UserAuthenticationResponseDTO> call, Response<UserAuthenticationResponseDTO> response) {
                UserAuthenticationResponseDTO body = response.body();

                if (response.code() == 200 && body != null) {
                    callback.accept(new Result.Success<>(body));
                    token = body.getToken();
                    user = body.getUser();
                } else {
                    callback.accept(new Result.Error(new Exception("Something went wrong while creating account.")));
                }
            }

            @Override
            public void onFailure(Call<UserAuthenticationResponseDTO> call, Throwable t) {
                Log.e("REGISTER_API_CALL", "Error:" + t.getMessage());
            }
        });
    }

    public void saveUserSettings(UserSettingsDTO userSettings) {
        userDataService.setUserSettings(token, user.getUsername(), userSettings)
                .enqueue(new ApiGenericCallback<>(null, null, "SET_USER_SETTINGS"));
    }

    public void validateToken(String token, String username, Consumer<Void> onSuccess, Consumer<Throwable> onFail) {
        userDataService.validateToken(token, username)
                .enqueue(new ApiGenericCallback<>(onSuccess, onFail, "TOKEN_VALIDATION"));
    }

    public void validateAndCreateIfNotExistsGoogleAccount(String token, Consumer<GoogleUserAuthenticationDTO> onSuccess, Consumer<Throwable> onFail) {
        userDataService.validateGoogleAccount(token)
                .enqueue(new ApiGenericCallback<>(onSuccess, onFail, "GOOGLE_VALIDATION"));
    }

    public void updateUser(Consumer<UserDTO> onSuccess, Consumer<Throwable> onFail, UserDTO userDTO) {
        Consumer<UserDTO> processSuccess = user -> {
            this.user = user;
            onSuccess.accept(user);
        };

        userDataService.updateUser(token, user.getUsername(), userDTO)
                .enqueue(new ApiGenericCallback<>(processSuccess, onFail, "UPDATE_USER"));
    }

    public void patchUnits(Consumer<UserSettingsDTO> onSuccess, Consumer<Throwable> onFail, UserSettingsDTO userSettingsDTO) {
        Consumer<UserSettingsDTO> processSuccess = settings -> {
            this.user.setSettings(settings);
            onSuccess.accept(settings);
        };

        userDataService.patchUnits(token, user.getUsername(), userSettingsDTO)
                .enqueue(new ApiGenericCallback<>(processSuccess, onFail, "PATCH_UNITS"));
    }

    public void setToken(String token) {
        this.token = token;
    }
}
