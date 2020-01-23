package com.example.powerlogger.ui.register;

public class RegisterFormState {

    private String firstNameError;
    private String lastNameError;
    private String emailError;
    private String passwordError;
    private String confirmPasswordError;

    public RegisterFormState(String firstNameError, String lastNameError, String emailError, String passwordError, String confirmPasswordError) {
        this.firstNameError = firstNameError;
        this.lastNameError = lastNameError;
        this.emailError = emailError;
        this.passwordError = passwordError;
        this.confirmPasswordError = confirmPasswordError;
    }

    public String getFirstNameError() {
        return firstNameError;
    }

    public String getLastNameError() {
        return lastNameError;
    }

    public String getEmailError() {
        return emailError;
    }

    public String getPasswordError() {
        return passwordError;
    }

    public String getConfirmPasswordError() {
        return confirmPasswordError;
    }

    public boolean isDataValid() {
        return firstNameError == null && lastNameError == null && emailError == null && confirmPasswordError == null && passwordError == null;
    }
}
