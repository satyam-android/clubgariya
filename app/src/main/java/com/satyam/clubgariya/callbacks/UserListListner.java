package com.satyam.clubgariya.callbacks;

import com.satyam.clubgariya.database.tables.User;

public interface UserListListner {
    void onUserClick(String userId);
    void renderingBottomItem(int count);
}
