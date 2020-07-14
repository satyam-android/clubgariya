package com.satyam.clubgariya.callbacks;

import androidx.fragment.app.Fragment;

public interface LoginViewModalListner {
    void onAuthSuccess();
    void onAuthFailed(String reason);
    void changeFragment(Fragment fragment);
}
