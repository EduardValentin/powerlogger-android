package com.example.powerlogger.ui.userSettings;

import com.example.powerlogger.dto.user.UserSettingsDTO;

public interface UserSettingsActionsListener {
    void onConfirmSettings(String username, UserSettingsDTO userSettingsDTO);
}
