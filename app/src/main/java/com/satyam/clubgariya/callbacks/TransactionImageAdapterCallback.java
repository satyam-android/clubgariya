package com.satyam.clubgariya.callbacks;

import com.satyam.clubgariya.modals.Chat;
import com.satyam.clubgariya.modals.Transaction;

public interface TransactionImageAdapterCallback {
    void onTransactionImageItemClick(Transaction transaction, int imageGridPosition);
}
