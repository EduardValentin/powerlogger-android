package com.example.powerlogger.utils;

public class APIError {
    private String errorMessage;
    private String uiMessage;

    public APIError() {
    }

    public APIError(String errorMessage, String uiMessage) {
        this.errorMessage = errorMessage;
        this.uiMessage = uiMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getUiMessage() {
        return uiMessage;
    }

    public void setUiMessage(String uiMessage) {
        this.uiMessage = uiMessage;
    }
}
