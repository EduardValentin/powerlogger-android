package com.eduardv.powerlogger.ui.userSettings;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eduardv.powerlogger.dto.units.HeightUnit;
import com.eduardv.powerlogger.dto.units.WeightUnit;
import com.eduardv.powerlogger.dto.user.UserSettingsDTO;
import com.eduardv.powerlogger.ui.CustomAfterTextWatcher;
import com.eduardv.powerlogger.ui.register.UserConstants;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class UserSettingsViewModel extends ViewModel {
    private UserSettingsDTO userSettingsDTO;
    private TextWatcher userSettingsTextWatcher;
    private MutableLiveData<Map<String, String>> settingsFormErrors = new MutableLiveData<>();
    private Map<String, Float> weightConstraintsMap = new HashMap<>();
    private Map<String, Float> heightConstraintsMap = new HashMap<>();

    public UserSettingsViewModel() {
        this.userSettingsDTO = new UserSettingsDTO();

        userSettingsDTO.setHeightUnit(HeightUnit.CENTIMETERS);
        userSettingsDTO.setWeightUnit(WeightUnit.KILOGRAM);

        this.userSettingsTextWatcher = new CustomAfterTextWatcher(this::onUserSettingsTextChange);

        weightConstraintsMap.put(WeightUnit.KILOGRAM.toString(), 30f);
        weightConstraintsMap.put(WeightUnit.POUND.toString(), 66.13f);

        heightConstraintsMap.put(HeightUnit.CENTIMETERS.toString(), 100f);
        heightConstraintsMap.put(HeightUnit.INCH.toString(), 39.37f);
        heightConstraintsMap.put(HeightUnit.FEET.toString(), 3.28f);

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

        WeightUnit wu = userSettingsDTO.getWeightUnit();
        HeightUnit hu = userSettingsDTO.getHeightUnit();

        float heightConstraint = heightConstraintsMap.get(hu.toString());
        float weightConstraint = weightConstraintsMap.get(wu.toString());

        if (userSettingsDTO.getHeight() <= heightConstraint) {
            errors.put(UserConstants.USER_HEIGHT_ERR, "Height cannot be lower than  " + heightConstraint + " " + hu.getShortname());
        }

        if (userSettingsDTO.getWeight() <= weightConstraint) {
            errors.put(UserConstants.USER_WEIGHT_ERR, "Weight cannot be lower than " + weightConstraint + " " + wu.getShortname());
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
