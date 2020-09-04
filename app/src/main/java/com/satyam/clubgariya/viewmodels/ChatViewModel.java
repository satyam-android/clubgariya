package com.satyam.clubgariya.viewmodels;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.satyam.clubgariya.callbacks.ChatFragmentListner;
import com.satyam.clubgariya.database.tables.User;
import com.satyam.clubgariya.helper.CurrentUserData;
import com.satyam.clubgariya.helper.FirebaseObjectHandler;
import com.satyam.clubgariya.modals.Chat;
import com.satyam.clubgariya.modals.ChatReference;
import com.satyam.clubgariya.modals.Transaction;
import com.satyam.clubgariya.modals.TransactionReference;
import com.satyam.clubgariya.services.AppSendNotification;
import com.satyam.clubgariya.utils.AppConstants;
import com.satyam.clubgariya.utils.AppJsonObjectCreator;

import org.json.JSONException;
import org.json.JSONObject;

public class ChatViewModel extends AndroidViewModel {
    private String title = "";
    private String content = "";
    private String transactionType;
    private ChatFragmentListner listner;

    public ChatViewModel(@NonNull Application application) {
        super(application);
    }

    public void setFragmentListner(ChatFragmentListner listner) {
        this.listner = listner;
    }

    public void sendChatNotification(ChatReference chatReference, Chat chat, User partnerDetail) {
        title = CurrentUserData.getInstance().getUserName();
        if(TextUtils.isEmpty(title))
            title=CurrentUserData.getInstance().getUid();
        content = chat.getMsg();
        transactionType = AppConstants.NOTIFICATION_TYPE_MESSAGE_INITIATED;
        JSONObject jsonObject = null;
        try {
            jsonObject = new AppJsonObjectCreator().createMessageNotificationJson(title, content, partnerDetail.getImageUrl(), chatReference.getChatReferenceId(), chat.getChatId(), transactionType, chat.getMediaList());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonObject != null)
            new AppSendNotification(getApplication(), partnerDetail.getFcm_token(), false, jsonObject).start();

    }

/*    public void implementUserDataChangeListner(ChatReference chatReference) {
        FirebaseObjectHandler.getInstance().getChatReferenceCollection().document(chatReference.getChatReferenceId()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                if (documentSnapshot.exists()) {
                    listner.onUserDataUpdate(documentSnapshot.toObject(ChatReference.class));
                } else {
                }

            }
        });
    }*/

}