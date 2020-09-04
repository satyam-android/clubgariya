package com.satyam.clubgariya.callbacks;

import com.satyam.clubgariya.database.tables.User;

public interface UserGroupListner {
    void onCHatUserChecked(User user);
    void onCHatUserUnChecked(User user);
}
