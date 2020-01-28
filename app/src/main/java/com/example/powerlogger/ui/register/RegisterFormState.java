package com.example.powerlogger.ui.register;

public class RegisterFormState {

    private String usernameErrror;
    private String emailError;
    private String passwordError;
    private String confirmPasswordError;
    private String weightError;

    public RegisterFormState( String usernameError, String emailError, String passwordError, String confirmPasswordError, String weightError) {
        this.usernameErrror = usernameError;
        this.weightError = weightError;
        this.emailError = emailError;
        this.passwordError = passwordError;
        this.confirmPasswordError = confirmPasswordError;
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
        return usernameErrror== null && weightError == null && emailError == null && confirmPasswordError == null && passwordError == null;
    }
}
