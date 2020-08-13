package com.satyam.clubgariya.callbacks;

import com.satyam.clubgariya.database.tables.User;

public interface ISplashRepoCallback {
    void onSuccess(User user);
    void onFailure(String message);
}
