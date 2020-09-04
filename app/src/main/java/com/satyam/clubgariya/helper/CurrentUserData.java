package com.satyam.clubgariya.helper;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.Observer;

import com.satyam.clubgariya.database.AppDatabase;
import com.satyam.clubgariya.database.tables.User;

import java.util.List;

public class CurrentUserData {
    private static final String TAG = "CurrentUserData";

    private static CurrentUserData instance;
    private User user;

    private CurrentUserData() {
        user = new User();
    }

    public static CurrentUserData getInstance() {
        if (instance == null) instance = new CurrentUserData();
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        Log.e(TAG, "setUser ------------" );
        this.user = user;
    }

    public List<String> getChatGroups(){
        return this.user.getChatGroups();
    }

    public List<String> getTransactionGroup(){
        return this.user.getTransactionGroups();
    }

    public String getFcm_token(){
        return user.getFcm_token();
    }

    public double getProfileCredit(){
        return user.getTotalCredit();
    }

    public double getProfileDebit(){
        return user.getTotalDebit();
    }

    public String getUserStatus(){
        return user.getUserStatus();
    }

    public double getNet_Worth(){
        return user.getTotalCredit()- user.getTotalDebit();
    }

    public String getUserImageUrl() {
        return user.getImageUrl();
    }

    public String getUserName(){
        return user.getName();
    }

    public long getTimeCreated(){
//        if(TextUtils.isEmpty(user.getCreationTime()))
//            return 0;
//           else
               return user.getCreationTime();
    }

    public String getUserEmail(){
        return user.getEmail();
    }

    public String getUserMobile(){
        return user.getMobile();
    }

    public String getUid(){
        return user.getUid();
    }
}
