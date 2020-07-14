package com.satyam.clubgariya.repositories;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.satyam.clubgariya.helper.FirebaseObjectHandler;

import java.util.HashMap;
import java.util.Map;

public class FirebaseMessagingRepository {
    private static final String TAG = "FirebaseMessagingReposi";
    private static FirebaseMessagingRepository repository;

    private FirebaseMessagingRepository() {

    }

    public static FirebaseMessagingRepository getInstance() {
        if (repository == null) repository = new FirebaseMessagingRepository();
        return repository;
    }

    public void updateFcmToken(final String token) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("fcm_token", token);
        if (FirebaseObjectHandler.getInstance().getFirebaseAuth().getUid()!= null) {
            FirebaseObjectHandler.getInstance().getUserDocumentReference(FirebaseObjectHandler.getInstance().getFirebaseAuth().getUid()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.e(TAG, "onSuccess: " );
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "onFailure: " );
                }
            });
        } else {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateFcmToken(token);
                }
            }, 2000);
        }

    }
}
