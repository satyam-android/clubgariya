package com.satyam.clubgariya.utils;

import android.text.TextUtils;

import com.satyam.clubgariya.helper.CurrentUserData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AppJsonObjectCreator {

    private JSONObject jsonObject;

    public AppJsonObjectCreator() {
        jsonObject = new JSONObject();
    }

    public JSONObject createTransactionNotificationJson(String title, String message, String profile_image, String transactionReferenceId,String transId, String transactionType,String transAmount, List<String> mediaList) throws JSONException {
        jsonObject.put("channel",AppConstants.NOTIFICATION_CHANNEL_TRANSACTION);

        if(mediaList!=null && mediaList.size()>0)
            jsonObject.put("mediaSize",mediaList.size());
        if(!TextUtils.isEmpty(transactionReferenceId))
            jsonObject.put("transactionReferenceId",transactionReferenceId);
        if(!TextUtils.isEmpty(title))
        jsonObject.put("title",title);
        if(!TextUtils.isEmpty(message))
            jsonObject.put("message",message);
        if(!TextUtils.isEmpty(profile_image))
            jsonObject.put("profile_image",profile_image);
        if(!TextUtils.isEmpty(transId))
            jsonObject.put("transId",transId);
        if(!TextUtils.isEmpty(transactionType))
            jsonObject.put("transactionType",transactionType);
        jsonObject.put("transAmount",transAmount);
            return jsonObject;

    }

    public JSONObject createMessageNotificationJson(String title, String message, String profile_image, String chatReferenceId,String chatId, String transactionType, List<String> mediaList) throws JSONException {
        jsonObject.put("channel",AppConstants.NOTIFICATION_CHANNEL_MESSAGE);

        if(mediaList!=null && mediaList.size()>0)
            jsonObject.put("mediaSize",mediaList.size());
        if(!TextUtils.isEmpty(chatReferenceId))
            jsonObject.put("chatReferenceId",chatReferenceId);
        if(!TextUtils.isEmpty(title))
            jsonObject.put("title",title);
        if(!TextUtils.isEmpty(message))
            jsonObject.put("message",message);
        if(!TextUtils.isEmpty(profile_image))
            jsonObject.put("profile_image",profile_image);
        if(!TextUtils.isEmpty(chatId))
            jsonObject.put("chatId",chatId);
        if(!TextUtils.isEmpty(transactionType))
            jsonObject.put("transactionType",transactionType);
            jsonObject.put("uid", CurrentUserData.getInstance().getUid());
        return jsonObject;

    }

    public JSONObject getFinalJSONO() {
        return jsonObject;
    }
}
