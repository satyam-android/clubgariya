package com.satyam.clubgariya.callbacks;

import com.satyam.clubgariya.database.tables.User;

public interface ILoginRepository {
    void onAuthSuccess();
    void onUserDataSuccess(User user);
    void onAuthFailure(String message);
}
