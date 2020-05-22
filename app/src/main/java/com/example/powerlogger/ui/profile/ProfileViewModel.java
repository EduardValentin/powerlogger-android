package com.example.powerlogger.ui.profile;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.powerlogger.dto.units.HeightUnit;
import com.example.powerlogger.dto.units.WeightUnit;
import com.example.powerlogger.dto.user.UserDTO;
import com.example.powerlogger.dto.user.UserSettingsDTO;
import com.example.powerlogger.repositories.UserRepository;
import com.example.powerlogger.ui.CustomAfterTextWatcher;
import com.example.powerlogger.ui.register.UserConstants;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileViewModel extends ViewModel {
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private static final int CM_TRESHOLD = 66;
    private static final int INCH_TRESHOLD = 30;
    private static final int PUND_TRESHOLD = 66;
    private static final int KG_TRESHOLD = 30;


    private UserDTO dto;
    private TextWatcher textWatcher;
    private UserRepository userRepository;
    private MutableLiveData<Map<String, String>> errorsLiveData;

    public ProfileViewModel() {
        errorsLiveData = new MutableLiveData<>();
        errorsLiveData.setValue(new HashMap<>());

        userRepository = UserRepository.getInstance();
        textWatcher = new CustomAfterTextWatcher(this::onUserRegistrationTextChange);
        dto = new UserDTO();
    }

    private void onUserRegistrationTextChange(Editable editable) {

        String usernameError = null;
        String emailError = null;
        String heightError = null;
        String weightError = null;
        String birthDateError = null;

        if (dto.getUsername().length() < 3) {
            usernameError = "Username length must be greater than 3";
        }

        if (dto.getEmail().length() == 0) {
            emailError = "Email is required";
        } else if (!validateMail(dto.getEmail())) {
            emailError = "Email is not valid";
        }

        UserSettingsDTO settingsDTO = dto.getSettings();
        UserDTO currentUser = userRepository.getUser();
        if (!validateHeight(currentUser.getSettings().getHeightUnit(), settingsDTO.getHeight())) {
            heightError = "Value too small";
        }

        if (!validateWeight(currentUser.getSettings().getWeightUnit(), settingsDTO.getWeight())) {
            weightError = "Value too small";
        }

        String birthDate = settingsDTO.getBirthDate();

        if (birthDate != null && birthDate.length() > 0) {
            LocalDate ld = LocalDate.parse(birthDate);

            if (ld.isAfter(LocalDate.now().minusYears(5))) {
                birthDateError = "Birth date cannot be in the future or less than 5 years into the past";
            }
        } else {
            birthDateError = "Birth date cannot be null";
        }

        Map<String, String> errorsMap = errorsLiveData.getValue();
        errorsMap.put("username", usernameError);
        errorsMap.put("email", emailError);
        errorsMap.put("height", heightError);
        errorsMap.put("weight", weightError);
        errorsMap.put("birthDate", birthDateError);

        errorsLiveData.setValue(errorsMap);
    }

    public UserDTO getDto() {
        return dto;
    }

    public TextWatcher getTextWatcher() {
        return textWatcher;
    }

    public void sendProfileUpdates(Consumer<UserDTO> onSuccess, Consumer<Throwable> onFail) {
        userRepository.updateUser(onSuccess, onFail, dto);
    }

    public MutableLiveData<Map<String, String>> getErrorsLiveData() {
        return errorsLiveData;
    }

    public void updateDtoWithCurrentProfile() {
        UserDTO repoUser = userRepository.getUser();

        dto.setEmail(repoUser.getEmail());
        dto.setUsername(repoUser.getUsername());
        dto.getSettings().setWeight(repoUser.getSettings().getWeight());
        dto.getSettings().setHeight(repoUser.getSettings().getHeight());
        dto.getSettings().setBirthDate(repoUser.getSettings().getBirthDate());
    }

    private boolean validateMail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }


    private boolean validateHeight(HeightUnit unit, float value) {
        if (unit.equals(HeightUnit.CENTIMETERS) && value < CM_TRESHOLD) {
            return false;
        }

        if (unit.equals(HeightUnit.INCH) && value < INCH_TRESHOLD) {
            return false;
        }

        return true;
    }

    private boolean validateWeight(WeightUnit unit, float value) {
        if (unit.equals(WeightUnit.KILOGRAM) && value < KG_TRESHOLD) {
            return false;
        }

        if (unit.equals(WeightUnit.POUND) && value < PUND_TRESHOLD) {
            return false;
        }

        return true;
    }
}
