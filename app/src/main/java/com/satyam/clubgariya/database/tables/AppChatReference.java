package com.satyam.clubgariya.database.tables;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "ChatReferences", indices = @Index(value = {"chatReferenceId"}, unique = true))
public class AppChatReference {
    @PrimaryKey(autoGenerate = true)
    public int id;
    String chatReferenceId;
    String lastMessage;
    long timeStamp;
    String chatType;
    String senderNotificationSource;
    String senderUid;
    public AppChatReference(String chatReferenceId, String lastMessage, long timeStamp, String chatType, String senderNotificationSource, String senderUid) {
        this.chatReferenceId = chatReferenceId;
        this.lastMessage = lastMessage;
        this.timeStamp = timeStamp;
        this.chatType = chatType;
        this.senderNotificationSource = senderNotificationSource;
        this.senderUid = senderUid;
    }

    public String getChatReferenceId() {
        return chatReferenceId;
    }

    public void setChatReferenceId(String chatReferenceId) {
        this.chatReferenceId = chatReferenceId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getChatType() {
        return chatType;
    }

    public void setChatType(String chatType) {
        this.chatType = chatType;
    }

    public String getSenderNotificationSource() {
        return senderNotificationSource;
    }

    public void setSenderNotificationSource(String senderNotificationSource) {
        this.senderNotificationSource = senderNotificationSource;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }
}
