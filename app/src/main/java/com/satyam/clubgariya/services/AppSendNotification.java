package com.satyam.clubgariya.services;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AppSendNotification extends Thread {
    private static final String TAG = "HandleChatNotification";
    private Context context;
    private String to;
    private RequestQueue mRequestQueue;
    private JsonObjectRequest jsonObjectRequest;
    private JSONObject jsonObject;
    private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    private String serverKey =
            "key=" + "AAAAeo-Fg5w:APA91bH42u0eQDaBa68GxjwbrXvlWzouQBhkZMB5z16OIrOZ4dqfhmppf3hiLZs5Ea4U4boeipUimXw5Cv7x58w2T0KZ-j3AhmyI_TT4tdOFdzglV2XKBzH6vfhsx2ovqokVazvMiKS5";
    private String contentType = "application/json";

    public AppSendNotification(Context context, String to,boolean isTopic,JSONObject jsonObject) {
        this.context = context;
        this.to = to;
        if(isTopic)
            FirebaseMessaging.getInstance().subscribeToTopic("/topics/Enter_your_topic_name");
        this.jsonObject = jsonObject;
    }

    @Override
    public void run() {
        if (!TextUtils.isEmpty(to)) {
            JSONObject notification = new JSONObject();
            try {
                notification.put("to", to);
                notification.put("data", jsonObject);

            } catch (JSONException e) {

            }
            sendNotification(notification);
        }
    }

    private void sendNotification(JSONObject notification) {
//RequestQueue initialized
/*        if(Looper.getMainLooper()==Looper.myLooper())
            Log.e(TAG, "sendNotification: MAIN" );
         else    Log.e(TAG, "sendNotification: BG" );*/
        mRequestQueue = Volley.newRequestQueue(context);
        jsonObjectRequest = new JsonObjectRequest(FCM_API, notification, response -> {
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
