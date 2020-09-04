package com.satyam.clubgariya.callbacks;

import com.satyam.clubgariya.modals.TransactionReference;

import java.util.List;

public interface TransactionReferenceListFragmentListner {
    void onUserClick(TransactionReference reference);
    void renderingBottomItem(int count);
    void onTransactionReferenceChange(List<TransactionReference> transactionReferences);
}
