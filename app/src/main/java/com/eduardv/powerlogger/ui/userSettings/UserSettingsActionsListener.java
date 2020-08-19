package com.eduardv.powerlogger.ui.userSettings;

import com.eduardv.powerlogger.dto.user.UserSettingsDTO;

public interface UserSettingsActionsListener {
    void onConfirmSettings(String username, UserSettingsDTO userSettingsDTO);
}
