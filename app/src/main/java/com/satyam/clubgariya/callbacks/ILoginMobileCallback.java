package com.satyam.clubgariya.callbacks;

import androidx.fragment.app.Fragment;

import com.satyam.clubgariya.database.tables.User;

public interface ILoginMobileCallback {
public void onAuthenticationSuccessful(User user);
public void onAuthenticationFail(String message);
public void onCodeSend();

}
