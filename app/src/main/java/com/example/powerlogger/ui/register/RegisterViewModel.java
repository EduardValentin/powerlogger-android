package com.example.powerlogger.ui.register;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.powerlogger.dto.RegisterRequestDTO;
import com.example.powerlogger.dto.UserResponseDTO;
import com.example.powerlogger.repositories.UserRepository;
import com.example.powerlogger.ui.CustomTextWatcher;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterViewModel extends ViewModel {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private MutableLiveData<RegisterFormState> registerFormStateMutableLiveData;
    private UserRepository userRepository = UserRepository.getInstance();

    private MutableLiveData<Map<String, String>> settingsFormErrors = new MutableLiveData<>();
    private RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO();
    private TextWatcher textWatcher;
    private TextWatcher userSettingsTextWatcher;

    public String confirmPassword;

    public RegisterViewModel() {

        registerFormStateMutableLiveData = new MutableLiveData<>();

        textWatcher = new CustomTextWatcher(this::onUserRegistrationTextChange);
        userSettingsTextWatcher = new CustomTextWatcher(this::onUserSettingsTextChange);
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
            passwordError = "Password length needs to be greater than 6 characters";
        } else if (confirmPassword == null || confirmPassword.length() == 0) {
            confirmPassError = "Confirmation is required";
        } else if (!passwordsMatch(password, confirmPassword)) {
            confirmPassError = "Passwords don't match";
        }

        if (username == null || username.length() == 0) {
            usernameError = "Username is required";
        } else if (username.length() < 3) {
            usernameError = "Username length must be greater than 3";
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

    public LiveData<Map<String, String>> getSettingsFormErrors() {
        return settingsFormErrors;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public TextWatcher getUserSettingsTextWatcher() {
        return userSettingsTextWatcher;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public boolean isUserSettingsDataValid() {
        return settingsFormErrors.getValue().size() == 0;
    }

    private boolean passwordsMatch(String pass, String confirm) {
        return pass.equals(confirm);
    }

    private boolean isLongEnough(String pass) {
        // TODO: maybe change to a more secure method
        return pass.length() > 7;
    }

    private void onUserRegistrationTextChange(Editable s) {
        valuesChanged(
                registerRequestDTO.getUsername(),
                registerRequestDTO.getEmail(),
                registerRequestDTO.getPassword(),
                confirmPassword);
    }

    private void onUserSettingsTextChange(Editable s) {

        Map<String, String> errors = new HashMap<>();

        if (registerRequestDTO.getSettings().getHeight() <= 100) {
            errors.put(UserConstants.USER_HEIGHT_ERR, "Height cannot be lower than 100 cm");
        }

        if (registerRequestDTO.getSettings().getWeight() <= 20) {
            errors.put(UserConstants.USER_WEIGHT_ERR, "Weight cannot be lower than 20 kg");
        }

        String birthDate = registerRequestDTO.getSettings().getBirthDate();

        if (birthDate != null && birthDate.length() > 0) {
            LocalDate ld = LocalDate.parse(birthDate);

            if (ld.isAfter(LocalDate.now().minusYears(5))) {
                errors.put(UserConstants.USER_BIRTH_DATE_ERR, "Birth date cannot be in the future or less than 5 years into the past");
            }
        } else {
            errors.put(UserConstants.USER_BIRTH_DATE_ERR, "Birth date cannot be null");
        }

        settingsFormErrors.setValue(errors);

    }

    private boolean validateMail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }
}
