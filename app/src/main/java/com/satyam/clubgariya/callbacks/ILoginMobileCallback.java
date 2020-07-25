package com.satyam.clubgariya.callbacks;

import androidx.fragment.app.Fragment;

public interface ILoginMobileCallback {
public void onAuthenticationSuccessful();
public void onAuthenticationFail(String message);
public void onCodeSend();

}
