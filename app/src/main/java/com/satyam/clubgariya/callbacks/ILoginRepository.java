package com.satyam.clubgariya.callbacks;

import com.satyam.clubgariya.modals.UserRegister;

public interface ILoginRepository {
    void onAuthSuccess();
    void onUserDataSuccess(UserRegister userRegister);
    void onAuthFailure(String message);
}
