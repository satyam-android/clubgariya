package com.satyam.clubgariya.services;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.satyam.clubgariya.AppApplication;
import com.satyam.clubgariya.R;
import com.satyam.clubgariya.helper.FirebaseObjectHandler;
import com.satyam.clubgariya.repositories.MessagingRepository;
import com.satyam.clubgariya.ui.MainActivity;
import com.satyam.clubgariya.utils.AppConstants;
import com.satyam.clubgariya.utils.AppSharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MessagingService extends FirebaseMessagingService {

    private static final String TAG = "MessagingService";
    private NotificationCompat.Builder notificationBuilder;
    private NotificationManagerCompat notificationManager;
    private String notificationType = "";
    private String viewStack = "";
    private String title = "";
    private String content = "";
    private String uid = "";
    private String userChatId = "";
    private String userTransId = "";
    private String chatId = "";
    private String transId = "";
    private String profile_image = "";
    private String media="";
    private Bitmap image;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> params = remoteMessage.getData();
        JSONObject object = new JSONObject(params);
        Log.e("JSON_OBJECT", object.toString());
        viewStack = new AppSharedPreference(getBaseContext()).getStringData(AppConstants.VIEW_STACK);
        notificationManager = NotificationManagerCompat.from(this);
        try {
            notificationType = object.getString("channel");
            if (notificationType.equalsIgnoreCase(AppConstants.CHANNEL_MESSAGE)) {
                prepareNotificationForMessage(object);
            } else {
                prepareNotificationForPayment(object);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        notificationBuilder.setSmallIcon(R.drawable.icon_loop);
        notificationBuilder.setAutoCancel(true);

        if (viewStack.toLowerCase().contains(notificationType)) {
            if (notificationType.contains("message")) {
                //Notification for message
                if (!AppConstants.CURRENT_CHAT_ID.contains(uid)) {
                    notificationManager.notify(0, notificationBuilder.build());
                }
            } else {
                //Notification for Payment
                if (!AppConstants.CURRENT_TRANSACTION_ID.contains(uid)) {
                    notificationManager.notify(0, notificationBuilder.build());
                }
            }
        }

    }


    public void prepareNotificationForMessage(JSONObject object) throws JSONException {
        Intent chatIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, chatIntent, 0);
        notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), AppApplication.CHANNEL_ID_MESSAGE);
         image = BitmapFactory.decodeResource(getResources(), R.drawable.madhupur_junction);
//        RemoteInput remoteInput=new RemoteInput.Builder("key_text_reply").setLabel("Reply").build();
//        NotificationCompat.Action action=new NotificationCompat.Action.Builder(R.drawable.ic_send_icon,"Reply").addRemoteInput(remoteInput);
        title = object.getString("title");
        content = object.getString("content");
        profile_image = object.getString("profile_image");
        uid = object.getString("uid");
        userChatId = object.getString("users_chat_id");
        chatId = object.getString("chat_id");
        media = object.getString("media");
        chatIntent.putExtra("display_name", title);
        chatIntent.putExtra("profile_image", profile_image);
        chatIntent.putExtra("uid", uid);
        chatIntent.putExtra("notification_type", notificationType);
//        notificationBuilder.setContentText(content);
        JSONArray mediaArray=new JSONArray(media);
        if(mediaArray.length()>0){
            content=content+"\n Additional : "+mediaArray.length()+"  Media file";
        }else{
            content = object.getString("content");
        }
        notificationBuilder.setColor(Color.BLUE);
        notificationBuilder.setLargeIcon(image);
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(content);
        notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(content).setBigContentTitle(title));
        notificationBuilder.setOnlyAlertOnce(true);
        notificationBuilder.setContentIntent(pendingIntent);
        Map<String, Object> map = new HashMap<>();
        map.put("chatStatus", "DELIVERED");
        FirebaseObjectHandler.getInstance().getChatCollection(userChatId).document(chatId).update(map);

    }

    public void prepareNotificationForPayment(JSONObject object) throws JSONException{
        Intent chatIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, chatIntent, 0);
        notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), AppApplication.CHANNEL_ID_PAYMENT);
        image = BitmapFactory.decodeResource(getResources(), R.drawable.madhupur_junction);
//        RemoteInput remoteInput=new RemoteInput.Builder("key_text_reply").setLabel("Reply").build();
//        NotificationCompat.Action action=new NotificationCompat.Action.Builder(R.drawable.ic_send_icon,"Reply").addRemoteInput(remoteInput);
        title = object.getString("title");
        content = object.getString("content");
        profile_image = object.getString("profile_image");
        uid = object.getString("uid");
        userTransId = object.getString("users_trans_id");
        transId = object.getString("trans_id");
        media = object.getString("media");
        chatIntent.putExtra("display_name", title);
        chatIntent.putExtra("profile_image", profile_image);
        chatIntent.putExtra("uid", uid);
        chatIntent.putExtra("notification_type", notificationType);
//        notificationBuilder.setContentText(content);
        JSONArray mediaArray=new JSONArray(media);
        notificationBuilder.setColor(Color.BLUE);
        notificationBuilder.setLargeIcon(image);
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(content);
        notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(content).setBigContentTitle(title));
        notificationBuilder.setOnlyAlertOnce(true);
        notificationBuilder.setContentIntent(pendingIntent);
        Map<String, Object> map = new HashMap<>();
        map.put("chatStatus", "DELIVERED");
        FirebaseObjectHandler.getInstance().getTransactionCollection(userTransId).document(transId).update(map);


    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        MessagingRepository.getInstance().updateFcmToken(s);
    }
}
