package com.satyam.clubgariya.callbacks;

import com.satyam.clubgariya.modals.Chat;

public interface ChatAdapterCallback {
    void currentItemCount(int count);
    void renderingBottomItem(int count);
    void itemCLick(Chat chat);
    void itemImageListClick(Chat chat);
    void itemLongClick(Chat chat);
}
