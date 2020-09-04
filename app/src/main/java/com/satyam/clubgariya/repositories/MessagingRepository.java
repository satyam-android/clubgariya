package com.satyam.clubgariya.repositories;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.satyam.clubgariya.helper.FirebaseObjectHandler;
import com.satyam.clubgariya.services.AppSendNotification;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MessagingRepository {
    private static final String TAG = "MessagingRepository";
    private static MessagingRepository repository;


    private MessagingRepository() {

    }

    public static MessagingRepository getInstance() {
        if (repository == null) repository = new MessagingRepository();
        return repository;
    }

    public void updateFcmToken(final String token) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("fcm_token", token);
        if (FirebaseObjectHandler.getInstance().getFirebaseAuth().getUid() != null) {
            FirebaseObjectHandler.getInstance().getUserDocumentReference(FirebaseObjectHandler.getInstance().getFirebaseAuth().getUid()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.e(TAG, "onSuccess: ");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "onFailure: ");
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
