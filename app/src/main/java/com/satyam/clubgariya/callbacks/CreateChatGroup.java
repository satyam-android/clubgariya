package com.satyam.clubgariya.callbacks;

import com.satyam.clubgariya.modals.ChatReference;
import com.satyam.clubgariya.modals.TransactionReference;

public interface CreateChatGroup {
    void onGroupCreatedSuccess(ChatReference reference, TransactionReference transactionReference);
    void onGroupCreatedFail();
}
