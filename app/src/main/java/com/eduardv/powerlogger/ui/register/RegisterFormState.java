package com.eduardv.powerlogger.ui.register;

public class RegisterFormState {

    private String usernameError;
    private String emailError;
    private String passwordError;
    private String confirmPasswordError;

    public RegisterFormState( String usernameError, String emailError, String passwordError, String confirmPasswordError) {
        this.usernameError = usernameError;
        this.emailError = emailError;
        this.passwordError = passwordError;
        this.confirmPasswordError = confirmPasswordError;
    }

    public String getUsernameError() {
        return usernameError;
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
        return usernameError == null && emailError == null && confirmPasswordError == null && passwordError == null;
    }
}
