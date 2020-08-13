package com.satyam.clubgariya;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImageTranscoderType;
import com.facebook.imagepipeline.core.MemoryChunkType;
import com.onesignal.OneSignal;

public class AppApplication extends Application {

    private NotificationManager notificationManagerMessage, notificationManagerBlog;
    public static String CHANNEL_ID_MESSAGE = "message";
    public static String CHANNEL_ID_PAYMENT = "payment";
    private String messageChannelName = "Messages";
    private String blogChannelName = "Payment";
    private NotificationChannel notificationChannelMessage,notificationChannelPayment;
    @Override
    public void onCreate() {
        super.onCreate();
        ImagePipelineConfig config=ImagePipelineConfig.newBuilder(getApplicationContext())
                .setMemoryChunkType(MemoryChunkType.BUFFER_MEMORY)
                .setImageTranscoderType(ImageTranscoderType.JAVA_TRANSCODER)
                .experiment().setNativeCodeDisabled(true)
                .build();
        Fresco.initialize(getApplicationContext(),config);
        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        createNotificationChannels();
    }


    public void createNotificationChannels(){
        notificationManagerMessage = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManagerBlog = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannelMessage = new NotificationChannel(CHANNEL_ID_MESSAGE, messageChannelName, NotificationManager.IMPORTANCE_HIGH);
            notificationChannelMessage.setImportance(NotificationManager.IMPORTANCE_HIGH);
            notificationManagerMessage.createNotificationChannel(notificationChannelMessage);
            notificationChannelMessage.setDescription("This channel is dedicated to all type of Messages/Group text of this application");


            notificationChannelPayment = new NotificationChannel(CHANNEL_ID_PAYMENT, blogChannelName, NotificationManager.IMPORTANCE_HIGH);
            notificationChannelPayment.setImportance(NotificationManager.IMPORTANCE_HIGH);
            notificationManagerBlog.createNotificationChannel(notificationChannelPayment);
            notificationChannelPayment.setDescription("This channel is dedicated to all type of Payment/Transaction of this application");

        }
    }
}
