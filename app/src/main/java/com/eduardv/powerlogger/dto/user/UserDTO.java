package com.eduardv.powerlogger.dto.user;

public class UserDTO {
    private String id;
    private String email;
    private String username;
    private ExternalType externalType;
    private UserSettingsDTO settings;
    private String externalId;

    public UserDTO() {
        settings = new UserSettingsDTO();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ExternalType getExternalType() {
        return externalType;
    }

    public void setExternalType(ExternalType externalType) {
        this.externalType = externalType;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public UserSettingsDTO getSettings() {
        return settings;
    }

    public void setSettings(UserSettingsDTO settings) {
        this.settings = settings;
    }
}
