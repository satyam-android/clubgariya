package com.satyam.clubgariya.callbacks;

import com.satyam.clubgariya.modals.ChatReference;

import java.util.List;

public interface ChatReferenceListFragmentListner {
    void onUserClick(ChatReference chatReference);
    void renderingBottomItem(int count);
    void onChatReferenceChange(List<ChatReference> chatReferences);
}
