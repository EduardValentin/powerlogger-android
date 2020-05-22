package com.example.powerlogger.ui.userSettings;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.powerlogger.dto.user.UserSettingsDTO;
import com.example.powerlogger.ui.CustomAfterTextWatcher;
import com.example.powerlogger.ui.register.UserConstants;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class UserSettingsViewModel extends ViewModel {
    private UserSettingsDTO userSettingsDTO;
    private TextWatcher userSettingsTextWatcher;
    private MutableLiveData<Map<String, String>> settingsFormErrors = new MutableLiveData<>();

    public UserSettingsViewModel() {
        this.userSettingsDTO = new UserSettingsDTO();
        userSettingsTextWatcher = new CustomAfterTextWatcher(this::onUserSettingsTextChange);
    }

    public UserSettingsDTO getUserSettingsDTO() {
        return userSettingsDTO;
    }

    public TextWatcher getUserSettingsTextWatcher() {
        return userSettingsTextWatcher;
    }

    public LiveData<Map<String, String>> getSettingsFormErrors() {
        return settingsFormErrors;
    }

    public boolean isUserSettingsDataValid() {
        return settingsFormErrors.getValue().size() == 0;
    }

    private void onUserSettingsTextChange(Editable s) {

        Map<String, String> errors = new HashMap<>();

        if (userSettingsDTO.getHeight() <= 100) {
            errors.put(UserConstants.USER_HEIGHT_ERR, "Height cannot be lower than 100 cm");
        }

        if (userSettingsDTO.getWeight() <= 20) {
            errors.put(UserConstants.USER_WEIGHT_ERR, "Weight cannot be lower than 20 kg");
        }

        String birthDate = userSettingsDTO.getBirthDate();

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
}
