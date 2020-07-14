package com.satyam.clubgariya.modals;

public class Like {
    String uid;
    String userName;

    public Like(){

    }

    public Like(String uid, String userName) {
        this.uid = uid;
        this.userName = userName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
