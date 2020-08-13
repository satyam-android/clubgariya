package com.satyam.clubgariya.callbacks;

import com.satyam.clubgariya.modals.ChatReference;
import com.satyam.clubgariya.modals.TransactionReference;

import java.util.List;

public interface IBaseFragmentListner {
    void onProfileUpdate();
    void onChatReferenceChange(List<ChatReference> options);
    void onTransactionReferenceChange(List<TransactionReference> transactionReferences);


}
