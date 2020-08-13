package com.satyam.clubgariya.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.satyam.clubgariya.callbacks.UserListListner;

public class FinancialUserListViewModel extends AndroidViewModel {

    private UserListListner transactionListListner;

    public FinancialUserListViewModel(@NonNull Application application) {
        super(application);
        listenToTransactionReferenceChange();
    }

    public void setTransactionlistner(UserListListner transactionlistner) {
        this.transactionListListner = transactionlistner;
    }

    public void listenToTransactionReferenceChange() {
    }
}