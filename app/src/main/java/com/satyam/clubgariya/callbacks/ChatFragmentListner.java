package com.satyam.clubgariya.callbacks;

import com.satyam.clubgariya.database.tables.User;
import com.satyam.clubgariya.modals.ChatReference;

public interface ChatFragmentListner {
    void onUserDataUpdate(ChatReference chatReference);
}
