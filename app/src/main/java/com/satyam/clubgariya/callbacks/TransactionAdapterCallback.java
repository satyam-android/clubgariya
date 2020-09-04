package com.satyam.clubgariya.callbacks;

import com.satyam.clubgariya.modals.Chat;
import com.satyam.clubgariya.modals.Transaction;

public interface TransactionAdapterCallback {
    void currentItemCount(int count);
    void renderingBottomItem(int count);
    void itemCLick(Transaction transaction);
    void updateTransactionAccept(Transaction transaction);
    void updateTransactionDispute(Transaction transaction);
    void itemImageListClick(Transaction transaction);
    void itemLongClick(Transaction transaction);
}
