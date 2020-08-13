package com.satyam.clubgariya.modals;



import com.google.firebase.firestore.Exclude;

import java.util.List;

public class Chat {
    @Exclude
    String chatId;
    private String uid;
    private String msg;
    private String time;
    private List<String> mediaList;
    private String chatStatus;
    public Chat(){

    }
    public Chat(String uid, String msg, String time,List<String> mediaList,String chatStatus) {
        this.uid = uid;
        this.msg = msg;
        this.time = time;
        this.mediaList=mediaList;
        this.chatStatus=chatStatus;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getChatStatus() {
        return chatStatus;
    }

    public void setChatStatus(String chatStatus) {
        this.chatStatus = chatStatus;
    }

    public String getUid() {
        return uid;
    }

    public List<String> getMediaList() {
        return mediaList;
    }

    public void setMediaList(List<String> mediaList) {
        this.mediaList = mediaList;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
