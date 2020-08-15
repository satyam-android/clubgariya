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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MessagingRepository {
    private static final String TAG = "MessagingRepository";
    private static MessagingRepository repository;
    private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    private String serverKey =
            "key=" + "AAAAeo-Fg5w:APA91bH42u0eQDaBa68GxjwbrXvlWzouQBhkZMB5z16OIrOZ4dqfhmppf3hiLZs5Ea4U4boeipUimXw5Cv7x58w2T0KZ-j3AhmyI_TT4tdOFdzglV2XKBzH6vfhsx2ovqokVazvMiKS5";
    private String contentType = "application/json";
    private RequestQueue mRequestQueue;
    private JsonObjectRequest jsonObjectRequest;
    private Context context;

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

    public void sendNotificationToTopic(Context context, String topicName, String title, String message, JSONObject notificationData) {
//        String token="dJnaxD7BTkqXY8WYsqWxWE:APA91bH1PLjCVofJttfakBcHjvIamg6EWqeDR17R_TcOLX5bIdzNhbd5FaBIhiglTcFl3VGVWvFkwYoax4TLjFJ2toglGisJ1PXWAcwGW3ln_Ftemmk8fahC0T9LUmTQOoowD7vmk8Dr";
//        FirebaseMessaging.getInstance().subscribeToTopic("/topics/Enter_your_topic_name");
        FirebaseMessaging.getInstance().subscribeToTopic(topicName);
//        String topic = "/topics/Enter_your_topic_name"; //topic has to match what the receiver subscribed to
        this.context = context;
        if (!TextUtils.isEmpty(topicName)) {
            JSONObject notification = new JSONObject();
            try {
                notification.put("to", topicName);
                notification.put("title", title);
                notification.put("message", message);
                notification.put("data", notificationData);

            } catch (JSONException e) {

            }

            sendNotification(notification);
        }
    }

    public void sendNotificationToToken(Context context, String token, String title, String message, JSONObject notificationData) {
//      String token="dJnaxD7BTkqXY8WYsqWxWE:APA91bH1PLjCVofJttfakBcHjvIamg6EWqeDR17R_TcOLX5bIdzNhbd5FaBIhiglTcFl3VGVWvFkwYoax4TLjFJ2toglGisJ1PXWAcwGW3ln_Ftemmk8fahC0T9LUmTQOoowD7vmk8Dr";
//        FirebaseMessaging.getInstance().subscribeToTopic("/topics/Enter_your_topic_name");
//        String topic = "/topics/Enter_your_topic_name"; //topic has to match what the receiver subscribed to
        this.context = context;
        if (!TextUtils.isEmpty(token)) {
            JSONObject notification = new JSONObject();
            try {
                notification.put("to", token);
                notification.put("title", title);
                notification.put("message", message);
                notification.put("data", notificationData);

            } catch (JSONException e) {

            }

            sendNotification(notification);
        }
    }

    private void sendNotification(JSONObject jsonObject) {
//RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(context);
        jsonObjectRequest = new JsonObjectRequest(FCM_API, jsonObject, response -> {
            Log.e(TAG, "sendNotification: Success");
        }, error -> {
            Log.e(TAG, "sendNotification: error " + error.getLocalizedMessage());
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        mRequestQueue.add(jsonObjectRequest);

    }
}
