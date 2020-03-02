package com.example.powerlogger.dto;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;

import java.util.ArrayList;
import java.util.List;

public class RegisterRequestDTO {
    public String username;
    public String email;
    public String password;
    public UserSettingsDTO settings;

    public RegisterRequestDTO(String username, String email, String password, UserSettingsDTO userSettingsDTO) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.settings = userSettingsDTO;
    }

    public RegisterRequestDTO() {
        this.settings = new UserSettingsDTO();
    }

    public UserSettingsDTO getSettings() {
        return settings;
    }

    public void setSettings(UserSettingsDTO settings) {
        this.settings = settings;
    }

    public String getUsername() {
        return username;
    }

    public void setUsernaame(String usernaame) {
        this.username = usernaame;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
