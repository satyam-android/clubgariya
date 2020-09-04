package com.satyam.clubgariya.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.satyam.clubgariya.callbacks.TransactionReferenceListFragmentListner;
import com.satyam.clubgariya.callbacks.UserListListner;
import com.satyam.clubgariya.helper.CurrentUserData;
import com.satyam.clubgariya.helper.FirebaseObjectHandler;
import com.satyam.clubgariya.modals.TransactionReference;

import java.util.ArrayList;
import java.util.List;

public class FinancialUserListViewModel extends AndroidViewModel {
    private static final String TAG = "FinancialUserListViewMo";

    private TransactionReferenceListFragmentListner transactionListListner;
    private List<TransactionReference> referencesTrans;


    public FinancialUserListViewModel(@NonNull Application application) {
        super(application);
        listenToTransactionReferenceChange();
    }

    public void setTransactionlistner(TransactionReferenceListFragmentListner transactionlistner) {
        this.transactionListListner = transactionlistner;
    }

    public void listenToTransactionReferenceChange() {
        FirebaseObjectHandler.getInstance().getUserTransactionCollectionReference().whereArrayContains("allowedUids", CurrentUserData.getInstance().getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                referencesTrans = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    TransactionReference ref = doc.toObject(TransactionReference.class);
//                    Log.e(TAG, "Trans References: "+ref.getUserName() );
                    ref.setTransReferenceId(doc.getId());
                    referencesTrans.add(ref);
                }
                transactionListListner.onTransactionReferenceChange(referencesTrans);

            }
        });
    }
}