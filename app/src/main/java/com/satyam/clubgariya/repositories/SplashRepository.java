package com.satyam.clubgariya.repositories;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.satyam.clubgariya.callbacks.ISplashRepoCallback;
import com.satyam.clubgariya.helper.FirebaseObjectHandler;
import com.satyam.clubgariya.database.tables.User;

public class SplashRepository {
    private static final String TAG = "SplashRepository";
    private static SplashRepository repository;
    private static ISplashRepoCallback callback;

    public static SplashRepository getInstance(ISplashRepoCallback listner) {
        callback = listner;
        if (repository == null) repository = new SplashRepository();
        return repository;
    }

    public void getUserData(String uid) {
        FirebaseObjectHandler.getInstance().getUserDocumentReference(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    User user = documentSnapshot.toObject(User.class);
                    callback.onSuccess(user);
                }else{
                    callback.onFailure("User Detail Not Found");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFailure(e.getMessage());
            }
        });
    }
}
