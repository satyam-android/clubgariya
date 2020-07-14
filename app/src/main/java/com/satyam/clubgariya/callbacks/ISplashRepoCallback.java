package com.satyam.clubgariya.callbacks;

import com.satyam.clubgariya.modals.UserRegister;

public interface ISplashRepoCallback {
    void onSuccess(UserRegister userRegister);
    void onFailure(String message);
}
