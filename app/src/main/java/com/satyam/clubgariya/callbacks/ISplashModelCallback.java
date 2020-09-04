package com.satyam.clubgariya.callbacks;

import com.satyam.clubgariya.database.tables.User;

public interface ISplashModelCallback {
    void onSuccess(User user);
    void onFailure(String message);
}
