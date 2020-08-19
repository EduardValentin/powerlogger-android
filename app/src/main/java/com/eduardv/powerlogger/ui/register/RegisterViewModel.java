package com.eduardv.powerlogger.ui.register;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eduardv.powerlogger.dto.user.RegisterRequestDTO;
import com.eduardv.powerlogger.repositories.UserRepository;
import com.eduardv.powerlogger.ui.CustomAfterTextWatcher;

import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterViewModel extends ViewModel {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private MutableLiveData<RegisterFormState> registerFormStateMutableLiveData;
    private UserRepository userRepository = UserRepository.getInstance();

    private RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO();
    private TextWatcher textWatcher;

    public String confirmPassword;

    public RegisterViewModel() {
        registerFormStateMutableLiveData = new MutableLiveData<>();
        textWatcher = new CustomAfterTextWatcher(this::onUserRegistrationTextChange);
    }

    /*
     *   Called when one of the inputs is changed. It checks for errors and if it finds any it sets them in the state variable.
     */
    public void valuesChanged(String username, String email, String password, String confirmPassword) {

        String usernameError = null;
        String emailError = null;
        String confirmPassError = null;
        String passwordError = null;

        if (password == null || !isLongEnough(password)) {
            passwordError = "Password must contain at least 6 characters";
        } else if (confirmPassword == null || confirmPassword.length() == 0) {
            confirmPassError = "Confirmation is required";
        } else if (!passwordsMatch(password, confirmPassword)) {
            confirmPassError = "Passwords don't match";
        }

        if (username == null || username.length() == 0) {
            usernameError = "Username is required";
        } else if (username.length() < 3) {
            usernameError = "Username must have at least 3 characters";
        }

        if (email == null || email.length() == 0) {
            emailError = "Email is required";
        } else if (email != null && !validateMail(email)) {
            emailError = "Email is not valid";
        }

        registerFormStateMutableLiveData.setValue(new RegisterFormState(usernameError, emailError, passwordError, confirmPassError));
    }

    public RegisterRequestDTO getRegisterRequestDTO() {
        return registerRequestDTO;
    }

    public LiveData<RegisterFormState> getRegisterFormStateMutableLiveData() {
        return registerFormStateMutableLiveData;
    }

    public void signup(Consumer<Object> callback) {
        userRepository.signup(registerRequestDTO, callback);
    }

    public TextWatcher getTextWatcher() {
        return textWatcher;
    }


    public String getConfirmPassword() {
        return confirmPassword;
    }


    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }


    private boolean passwordsMatch(String pass, String confirm) {
        return pass.equals(confirm);
    }

    private boolean isLongEnough(String pass) {
        // TODO: maybe change to a more secure method
        return pass.length() >= 6;
    }

    private void onUserRegistrationTextChange(Editable s) {
        valuesChanged(
                registerRequestDTO.getUsername(),
                registerRequestDTO.getEmail(),
                registerRequestDTO.getPassword(),
                confirmPassword);
    }

    private boolean validateMail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }
}
