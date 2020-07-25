package com.satyam.clubgariya.helper;

import android.text.TextUtils;

import com.satyam.clubgariya.modals.UserRegister;
import com.satyam.clubgariya.ui.RegisterFragment;

public class CurrentUserData {

    private static CurrentUserData instance;
    private UserRegister userRegister;

    private CurrentUserData() {
        userRegister = new UserRegister();
    }

    public static CurrentUserData getInstance() {
        if (instance == null) instance = new CurrentUserData();
        return instance;
    }

    public void setUserRegister(UserRegister userRegister) {
        this.userRegister = userRegister;
    }

    public String getUserImageUrl() {
        return userRegister.getImageUrl();
    }

    public String getUserName(){
        return userRegister.getName();
    }

    public long getTimeCreated(){
        if(TextUtils.isEmpty(userRegister.getCreationTime()))
            return 0;
           else return Long.parseLong(userRegister.getCreationTime());
    }

    public String getUserEmail(){
        return userRegister.getEmail();
    }

    public String getUserMobile(){
        return userRegister.getMobile();
    }

    public String getUid(){
        return userRegister.getUid();
    }
}
