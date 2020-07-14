package com.satyam.clubgariya.callbacks;

import androidx.fragment.app.Fragment;

public interface RegisterViewModelListner {
    void onRegisterSuccess();
    void onRegisterStart();
    void onRegisterFail(String message);
}
