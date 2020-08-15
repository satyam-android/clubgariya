package com.satyam.clubgariya.database.tables;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "AppChats")
public class AppChat {
    @PrimaryKey(autoGenerate = true)
    int chatId;
    String senderUid;
    String message;
    long timeStamp;
    List<String> mediaList;
    String chatDeliveryStatus;

    public AppChat(String senderUid, String message, long timeStamp, List<String> mediaList, String chatDeliveryStatus) {
        this.senderUid = senderUid;
        this.message = message;
        this.timeStamp = timeStamp;
        this.mediaList = mediaList;
        this.chatDeliveryStatus = chatDeliveryStatus;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public List<String> getMediaList() {
        return mediaList;
    }

    public void setMediaList(List<String> mediaList) {
        this.mediaList = mediaList;
    }

    public String getChatDeliveryStatus() {
        return chatDeliveryStatus;
    }

    public void setChatDeliveryStatus(String chatDeliveryStatus) {
        this.chatDeliveryStatus = chatDeliveryStatus;
    }
}
