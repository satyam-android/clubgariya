package com.satyam.clubgariya.modals;

public class UserRegister {
    String imageUrl;
    String name;
    String email;
    String mobile;
    String uid;
    String creationTime;
    String fcm_token;

    public UserRegister() {
    }

    public UserRegister(String name, String email, String mobile, String uid,String imageUrl, String creationTime,String fcm_token) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.uid = uid;
        this.creationTime=creationTime;
        this.fcm_token=fcm_token;
    }

    public String getFcm_token() {
        return fcm_token;
    }

    public void setFcm_token(String fcm_token) {
        this.fcm_token = fcm_token;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
