package com.satyam.clubgariya.callbacks;

import androidx.fragment.app.Fragment;

import com.satyam.clubgariya.database.tables.User;

public interface RegisterViewModelListner {
    void onRegisterSuccess(User user);
    void onRegisterStart();
    void onRegisterFail(String message);
}
