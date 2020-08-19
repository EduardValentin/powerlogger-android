package com.eduardv.powerlogger.ui.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.eduardv.powerlogger.R;
import com.eduardv.powerlogger.dto.user.GoogleUserAuthenticationDTO;
import com.eduardv.powerlogger.dto.user.UserAuthenticationResponseDTO;
import com.eduardv.powerlogger.dto.user.UserDTO;
import com.eduardv.powerlogger.dto.user.UserSettingsDTO;
import com.eduardv.powerlogger.repositories.UserRepository;

import java.util.function.Consumer;

public class LoginViewModel extends ViewModel implements LifecycleOwner {

    private static final int MIN_USERNAME_LENGTH = 2;
    private final MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private UserRepository userRepository;

    private String userBirthDate;

    LoginViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }


    public void login(String username, String password, Consumer<Object> callback) {
        // can be launched in a separate asynchronous job
        userRepository.login(username, password, callback);
    }

    public void loginDataChanged(String username, String password) {
        Integer usernameError = null;
        Integer passwordError = null;

        if (!isUserNameValid(username)) {
            usernameError = R.string.invalid_username;
        }

        if (!isPasswordValid(password)) {
            passwordError = R.string.invalid_password;
        }

        if (passwordError != null || usernameError != null) {
            loginFormState.setValue(new LoginFormState(usernameError, passwordError));
            return;
        }

        loginFormState.setValue(new LoginFormState(true));
    }

    public void saveUserSettings(Consumer<UserDTO> onSuccess, UserSettingsDTO settingsDTO) {
        userRepository.saveUserSettings(onSuccess, settingsDTO);
    }

    void validateToken(String token, String username, Consumer<Void> onSuccess, Consumer<Throwable> onFail) {
        userRepository.validateToken(token, username, onSuccess, onFail);
    }

    void validateAndCreateIfNotExistsGoogleAccount(String token, Consumer<GoogleUserAuthenticationDTO> onSuccess, Consumer<Throwable> onFail) {
        userRepository.validateAndCreateIfNotExistsGoogleAccount(token, onSuccess, onFail);
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        } else {
            String trimmed = username.trim();
            return !trimmed.isEmpty() && trimmed.length() > MIN_USERNAME_LENGTH;
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    private void onGoogleValidationResponse(Object res) {
        UserAuthenticationResponseDTO authDetails = (UserAuthenticationResponseDTO) res;
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return null;
    }

    public void setUserBirthDate(String userBirthDate) {
        this.userBirthDate = userBirthDate;
    }
}