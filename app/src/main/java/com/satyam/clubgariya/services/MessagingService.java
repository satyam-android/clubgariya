package com.satyam.clubgariya.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.satyam.clubgariya.R;
import com.satyam.clubgariya.repositories.FirebaseMessagingRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class MessagingService extends FirebaseMessagingService {

    private String CHANNEL_ID = "CLubGariya";
    private String name = "CLubGariya";
    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> params = remoteMessage.getData();
        JSONObject object = new JSONObject(params);
        Log.e("JSON_OBJECT", object.toString());
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableVibration(true);
            notificationChannel.setSound(null, null);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationBuilder=new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID);
        notificationBuilder.setSmallIcon(R.drawable.ic_send_icon);
        notificationBuilder.setContentTitle("Title");
        try {
            notificationBuilder.setContentText(object.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        notificationManager.notify(0,notificationBuilder.build());

    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        FirebaseMessagingRepository.getInstance().updateFcmToken(s);
    }
}
