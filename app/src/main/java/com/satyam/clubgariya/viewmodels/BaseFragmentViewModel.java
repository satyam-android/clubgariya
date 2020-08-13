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
import com.satyam.clubgariya.callbacks.IBaseFragmentListner;
import com.satyam.clubgariya.helper.FirebaseObjectHandler;
import com.satyam.clubgariya.modals.ChatReference;
import com.satyam.clubgariya.modals.TransactionReference;

import java.util.ArrayList;
import java.util.List;

public class BaseFragmentViewModel extends AndroidViewModel {
    private static final String TAG = "BaseFragmentViewModel";
    private IBaseFragmentListner listner;
    private List<ChatReference> references;
    private List<TransactionReference> referencesTrans;

    public BaseFragmentViewModel(@NonNull Application application) {
        super(application);

        if (FirebaseObjectHandler.getInstance().getFirebaseAuth().getCurrentUser() != null) {
            registerChatReferenceChange();
            registerTransactionReferenceChange();
        }
    }

    public void setFragmentListner(IBaseFragmentListner listner) {
        this.listner = listner;
    }




    public void registerChatReferenceChange() {
        FirebaseObjectHandler.getInstance().getUserChatCollectionReference(FirebaseObjectHandler.getInstance().getFirebaseAuth().getUid()).orderBy("lastMessageTime", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                references = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    ChatReference chatReference = doc.toObject(ChatReference.class);
//                    Log.e(TAG, "CHat References: "+chatReference.getChattingWith() );
                    references.add(chatReference);
                }
                listner.onChatReferenceChange(references);
            }
        });
    }

    public void registerTransactionReferenceChange() {
        FirebaseObjectHandler.getInstance().getUserTransactionCollectionReference(FirebaseObjectHandler.getInstance().getFirebaseAuth().getUid()).orderBy("lastMessageTime", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                    referencesTrans.add(ref);
                }
                listner.onTransactionReferenceChange(referencesTrans);

            }
        });
    }
}
