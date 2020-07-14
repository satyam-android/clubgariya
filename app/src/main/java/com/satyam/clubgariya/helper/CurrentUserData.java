package com.satyam.clubgariya.helper;

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

    public String getUserFirstName(){
        return userRegister.getfName();
    }

    public String getUserFullName(){
     return userRegister.getfName()+" "+userRegister.getlName();
    }

    public String getUserLastName(){
        return userRegister.getlName();
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
