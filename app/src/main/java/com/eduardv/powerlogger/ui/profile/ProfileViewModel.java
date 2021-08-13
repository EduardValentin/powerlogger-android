package com.eduardv.powerlogger.ui.profile;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eduardv.powerlogger.dto.units.HeightUnit;
import com.eduardv.powerlogger.dto.units.WeightUnit;
import com.eduardv.powerlogger.dto.user.UserDTO;
import com.eduardv.powerlogger.dto.user.UserSettingsDTO;
import com.eduardv.powerlogger.repositories.UserRepository;
import com.eduardv.powerlogger.ui.CustomAfterTextWatcher;
import com.google.gson.Gson;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileViewModel extends ViewModel {
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private UserDTO dto;
    private TextWatcher textWatcher;
    private UserRepository userRepository;
    private MutableLiveData<Map<String, String>> errorsLiveData;
    private Map<String, Float> weightConstraintsMap = new HashMap<>();
    private Map<String, Float> heightConstraintsMap = new HashMap<>();
    private final UserDTO currentUser;

    public ProfileViewModel() {
        errorsLiveData = new MutableLiveData<>();
        errorsLiveData.setValue(new HashMap<>());
        userRepository = UserRepository.getInstance();
        textWatcher = new CustomAfterTextWatcher(this::onUserRegistrationTextChange);
        dto = new UserDTO();
        currentUser = userRepository.getUser();
        weightConstraintsMap.put(WeightUnit.KILOGRAM.toString(), 30f);
        weightConstraintsMap.put(WeightUnit.POUND.toString(), 66.13f);

        heightConstraintsMap.put(HeightUnit.CENTIMETERS.toString(), 100f);
        heightConstraintsMap.put(HeightUnit.INCH.toString(), 39.37f);
        heightConstraintsMap.put(HeightUnit.FEET.toString(), 3.28f);
    }

    private void onUserRegistrationTextChange(Editable editable) {

//        String usernameError = null;
        String emailError = null;
        String heightError = null;
        String weightError = null;
        String birthDateError = null;

//        if (dto.getUsername().length() < 3) {
//            usernameError = "Username length must be greater than 3";
//        }

        if (dto.getEmail().length() == 0) {
            emailError = "Email is required";
        } else if (!validateMail(dto.getEmail())) {
            emailError = "Email is not valid";
        }

        UserSettingsDTO settingsDTO = dto.getSettings();

        WeightUnit wu = currentUser.getSettings().getWeightUnit();
        HeightUnit hu = currentUser.getSettings().getHeightUnit();
        float weightConstraint = weightConstraintsMap.get(wu.toString());
        float heightConstraint = heightConstraintsMap.get(hu.toString());

        if (!validateHeight(hu, settingsDTO.getHeight())) {
            heightError = "Height cannot be lower than  " + heightConstraint + " " + hu.getShortname();
        }

        if (!validateWeight(wu, settingsDTO.getWeight())) {
            weightError = "Weight cannot be lower than " + weightConstraint + " " + wu.getShortname();
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
//        errorsMap.put("username", usernameError);
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
        Gson gson = new Gson();
        dto = gson.fromJson(gson.toJson(repoUser), UserDTO.class);
    }

    private boolean validateMail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }


    private boolean validateHeight(HeightUnit unit, float value) {
        HeightUnit hu = currentUser.getSettings().getHeightUnit();
        float heightThreshold = heightConstraintsMap.get(hu.toString());

        if (unit.equals(HeightUnit.CENTIMETERS) && value < heightThreshold) {
            return false;
        }

        if (unit.equals(HeightUnit.INCH) && value < heightThreshold) {
            return false;
        }

        return true;
    }

    private boolean validateWeight(WeightUnit unit, float value) {
        WeightUnit wu = currentUser.getSettings().getWeightUnit();
        float weightUnit = weightConstraintsMap.get(wu.toString());

        if (unit.equals(WeightUnit.KILOGRAM) && value < weightUnit) {
            return false;
        }

        if (unit.equals(WeightUnit.POUND) && value < weightUnit) {
            return false;
        }

        return true;
    }
}
