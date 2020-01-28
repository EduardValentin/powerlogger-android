package com.example.powerlogger.ui.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.powerlogger.dto.RegisterRequestDTO;
import com.example.powerlogger.dto.UserResponseDTO;
import com.example.powerlogger.repositories.UserRepository;

import java.util.function.Consumer;

public class RegisterViewModel extends ViewModel {
    private MutableLiveData<RegisterFormState> registerFormStateMutableLiveData;
    private UserRepository userRepository = UserRepository.getInstance();

    public RegisterViewModel() {
        registerFormStateMutableLiveData = new MutableLiveData<>();
    }

    /*
    *   Called when one of the inputs is changed. It checks for errors and if it finds any it sets them in the state variable.
    */
    public void valuesChanged(String username, String email, String password, String confirmPassword, String weight) {

        String usernameError = null;
        String emailError= null;
        String confirmPassError = null;
        String passwordError = null;
        String weightError = null;

        if (!isLongEnough(password)) {
            passwordError = "Password length needs to be greater than 6 characters";
        } else if(confirmPassword.length() > 0 && !passwordsMatch(password, confirmPassword)) {
            confirmPassError = "Passwords don't match";
        } else if(confirmPassword.length() <= 0) {
            confirmPassError = "Confirmation is required";
        }

        if (weight.length() == 0) {
            weightError = "Weight is required";
        }

        if (username.length() == 0) {
            usernameError = "Username is required";
        }

        if(email.length() == 0) {
            emailError = "Email name is required";
        }

        registerFormStateMutableLiveData.setValue(new RegisterFormState(emailError, passwordError, confirmPassError, weightError, usernameError));
    }

    public LiveData<RegisterFormState> getRegisterFormStateMutableLiveData() {
        return registerFormStateMutableLiveData;
    }

    public void signup(RegisterRequestDTO registerRequestDTO, Consumer<Object> callback) {
        userRepository.signup(registerRequestDTO, callback);
    }

    private boolean passwordsMatch(String pass, String confirm) {
        return pass.equals(confirm);
    }

    private boolean isLongEnough(String pass) {
        // TODO: maybe change to a more secure method
        return pass.length() > 6;
    }
}
