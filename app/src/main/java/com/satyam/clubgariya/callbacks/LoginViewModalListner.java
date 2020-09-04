package com.satyam.clubgariya.callbacks;

import androidx.fragment.app.Fragment;

import com.satyam.clubgariya.database.tables.User;

public interface LoginViewModalListner {
    void onAuthSuccess(User user);
    void onAuthFailed(String reason);
    void changeFragment(Fragment fragment);
}
