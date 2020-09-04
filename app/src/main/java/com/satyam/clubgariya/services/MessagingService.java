package com.satyam.clubgariya.services;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.satyam.clubgariya.AppApplication;
import com.satyam.clubgariya.R;
import com.satyam.clubgariya.database.tables.User;
import com.satyam.clubgariya.helper.FirebaseObjectHandler;
import com.satyam.clubgariya.repositories.MessagingRepository;
import com.satyam.clubgariya.ui.MainActivity;
import com.satyam.clubgariya.utils.AppConstants;
import com.satyam.clubgariya.utils.AppDatabaseHelper;
import com.satyam.clubgariya.utils.AppSharedPreference;
import com.satyam.clubgariya.utils.UtilFunction;

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
    private String message = "";
    private String uid = "";
    private String userChatId = "";
    private String userTransId = "";
    private String chatId = "";
    private String transId = "";
    private String profile_image = "";
    String transAmount = "";
    String transactionType = "";
    String appendMessage="";
    //    private String media="";
    private PendingIntent pendingIntent;
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
    }

    public void showNotification(){
        notificationBuilder.setColor(Color.BLUE);
        notificationBuilder.setLargeIcon(image);
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(message);
        notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(message).setBigContentTitle(title));
        notificationBuilder.setOnlyAlertOnce(true);
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setSmallIcon(R.drawable.icon_loop);
        notificationBuilder.setAutoCancel(true);

        if (viewStack.toLowerCase().contains(notificationType)) {
            if (notificationType.contains("message")) {
                //Notification for message
                if (!AppConstants.CURRENT_CHAT_ID.contains(userChatId)) {
                    notificationManager.notify(0, notificationBuilder.build());
                }
            } else {
                //Notification for Payment
//                if (!AppConstants.CURRENT_TRANSACTION_ID.contains(uid)) {
                notificationManager.notify(0, notificationBuilder.build());
//                }
            }
        }
    }


    public void prepareNotificationForMessage(JSONObject object) throws JSONException {
        Intent chatIntent = new Intent(getApplicationContext(), MainActivity.class);
         pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, chatIntent, 0);
        notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), AppApplication.CHANNEL_ID_MESSAGE);
        image = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_round);
//        RemoteInput remoteInput=new RemoteInput.Builder("key_text_reply").setLabel("Reply").build();
//        NotificationCompat.Action action=new NotificationCompat.Action.Builder(R.drawable.ic_send_icon,"Reply").addRemoteInput(remoteInput);
        title = object.getString("title");
        message = object.getString("message");
        profile_image = object.getString("profile_image");
        uid = object.getString("uid");
        userChatId = object.getString("chatReferenceId");
        chatId = object.getString("chatId");
        transactionType=object.getString("transactionType");
//        media = object.getString("media");
        chatIntent.putExtra("display_name", title);
        chatIntent.putExtra("profile_image", profile_image);
        chatIntent.putExtra("uid", uid);
        chatIntent.putExtra("notification_type", notificationType);
//        notificationBuilder.setContentText(content);
//        JSONArray mediaArray=new JSONArray(media);
//        if(mediaArray.length()>0){
//            content=content+"\n Additional : "+mediaArray.length()+"  Media file";
//        }else{
//            content = object.getString("content");
//        }
        AppDatabaseHelper.getInstance(getApplication()).getUserByUid(uid, userLocal -> {
            if(userLocal!=null && !TextUtils.isEmpty(userLocal.getName()))
                title=userLocal.getName();
            showNotification();
        });
        Map<String, Object> map = new HashMap<>();
        map.put("chatStatus", "DELIVERED");
        FirebaseObjectHandler.getInstance().getChatCollection(userChatId).document(chatId).update(map);

    }

    public void prepareNotificationForPayment(JSONObject object) throws JSONException {
        Intent chatIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, chatIntent, 0);
        notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), AppApplication.CHANNEL_ID_PAYMENT);
        image = BitmapFactory.decodeResource(getResources(), R.drawable.madhupur_junction);
//        RemoteInput remoteInput=new RemoteInput.Builder("key_text_reply").setLabel("Reply").build();
//        NotificationCompat.Action action=new NotificationCompat.Action.Builder(R.drawable.ic_send_icon,"Reply").addRemoteInput(remoteInput);
        title = object.getString("title");
        message = object.getString("message");
        profile_image = object.getString("profile_image");
        transAmount = object.getString("transAmount");
        transactionType=object.getString("transactionType");
        userTransId = object.getString("transactionReferenceId");
        transId = object.getString("transId");
        if(transactionType.equals(AppConstants.NOTIFICATION_TYPE_TRANSACTION_INITIATED)){
            appendMessage="Transaction of "+ UtilFunction.getInstance().getLocalCurrencySymbol(getApplicationContext())+" "+transAmount+" has been initiated";
        }else if(transactionType.equals(AppConstants.NOTIFICATION_TYPE_TRANSACTION_CONFIRM)){
            appendMessage="Transaction of "+ UtilFunction.getInstance().getLocalCurrencySymbol(getApplicationContext())+" "+transAmount+" has been accepted";
        }else if(transactionType.equals(AppConstants.NOTIFICATION_TYPE_TRANSACTION_DISPUTE)){
            appendMessage="Transaction of "+ UtilFunction.getInstance().getLocalCurrencySymbol(getApplicationContext())+" "+transAmount+" has been rejected";
        }
        message=message+"\n"+appendMessage;
//        media = object.getString("media");
//        chatIntent.putExtra("display_name", title);
//        chatIntent.putExtra("profile_image", profile_image);
//        chatIntent.putExtra("uid", uid);
//        chatIntent.putExtra("notification_type", notificationType);
//        notificationBuilder.setContentText(content);
//        JSONArray mediaArray=new JSONArray(media);
        notificationBuilder.setColor(Color.BLUE);
        notificationBuilder.setLargeIcon(image);
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(message);
        notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(message).setBigContentTitle(title));
        notificationBuilder.setOnlyAlertOnce(true);
        notificationBuilder.setContentIntent(pendingIntent);
        Map<String, Object> map = new HashMap<>();
        map.put("deliveryStatus", "DELIVERED");
        FirebaseObjectHandler.getInstance().getTransactionCollection(userTransId).document(transId).update(map);


    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        MessagingRepository.getInstance().updateFcmToken(s);
    }
}
