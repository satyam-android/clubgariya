package com.satyam.clubgariya.helper;

import android.text.TextUtils;

import com.satyam.clubgariya.database.tables.User;

public class CurrentUserData {

    private static CurrentUserData instance;
    private User user;

    private CurrentUserData() {
        user = new User();
    }

    public static CurrentUserData getInstance() {
        if (instance == null) instance = new CurrentUserData();
        return instance;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getProfileCredit(){
        return user.getTotalCredit();
    }

    public double getProfileDebit(){
        return user.getTotalDebit();
    }

    public String getUserStatus(){
        return user.getStatus();
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
        if(TextUtils.isEmpty(user.getCreationTime()))
            return 0;
           else return Long.parseLong(user.getCreationTime());
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
