package com.eduardv.powerlogger.ui.units;

import androidx.lifecycle.ViewModel;

import com.eduardv.powerlogger.dto.user.UserSettingsDTO;
import com.eduardv.powerlogger.repositories.UserRepository;

import java.util.function.Consumer;

public class UnitsViewModel extends ViewModel {
    private UserSettingsDTO dto;
    private UserRepository userRepository;

    public UnitsViewModel() {
        this.userRepository = UserRepository.getInstance();
        this.dto = new UserSettingsDTO();
    }

    public UserSettingsDTO getDto() {
        return dto;
    }

    public void sendUnitsUpdate(Consumer<UserSettingsDTO> onSuccess, Consumer<Throwable> onFail) {
        userRepository.patchUnits(onSuccess, onFail, dto);
    }

    public UserSettingsDTO getUserSettings() {
        return userRepository.getUser().getSettings();
    }
}
