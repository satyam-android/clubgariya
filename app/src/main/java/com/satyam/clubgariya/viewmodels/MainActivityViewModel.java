package com.satyam.clubgariya.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.satyam.clubgariya.callbacks.MainActivityListners;
import com.satyam.clubgariya.database.tables.User;
import com.satyam.clubgariya.helper.FirebaseObjectHandler;
import com.satyam.clubgariya.ui.MainActivity;

public class MainActivityViewModel extends AndroidViewModel {
    private static final String TAG = "MainActivityViewModel";
    private MainActivityListners listners;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public void setListner(MainActivityListners listner) {
        this.listners = listner;
    }

    public void listenToUserDetailChange(String uid) {
        FirebaseObjectHandler.getInstance().getUserDocumentReference(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e(TAG, "User Event listening failed: ");
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    listners.onUserDataChange(snapshot.toObject(User.class));
                } else {
                    Log.d(TAG, "Current data: null");
                }

            }
        });
    }
}
