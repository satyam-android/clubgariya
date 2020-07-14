package com.satyam.clubgariya.modals;

public class LoginError {
    private String userNameError;
    private String passwordError;

    public LoginError(String userNameError, String passwordError) {
        this.userNameError = userNameError;
        this.passwordError = passwordError;
    }

    public LoginError() {
    }

    public String getUserNameError() {
        return userNameError;
    }

    public void setUserNameError(String userNameError) {
        this.userNameError = userNameError;
    }

    public String getPasswordError() {
        return passwordError;
    }

    public void setPasswordError(String passwordError) {
        this.passwordError = passwordError;
    }
}
