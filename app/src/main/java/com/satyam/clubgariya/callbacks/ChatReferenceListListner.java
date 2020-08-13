package com.satyam.clubgariya.callbacks;

import com.satyam.clubgariya.database.tables.User;
import com.satyam.clubgariya.modals.ChatReference;

public interface ChatReferenceListListner {
    void onUserClick(ChatReference reference);
    void renderingBottomItem(int count);
}
