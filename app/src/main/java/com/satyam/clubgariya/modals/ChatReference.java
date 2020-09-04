package com.satyam.clubgariya.modals;


import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.List;

public class ChatReference {
    @Exclude
    String chatReferenceId;
    long lastMessageTime;
    String lastMessage;
    List<ChatReferenceUser> users;
    List<String> allowedUsers;
    String referenceType;
    String createdBy;
    String referenceName;
    String profileImage;
    String referenceId;

    public ChatReference() {

    }

    public ChatReference(String chatReferenceId,long lastMessageTime, String lastMessage,String referenceName,String createdBy,String referenceType,String profileImage,List<ChatReferenceUser> users, List<String> allowedUsers,String referenceId) {
        this.lastMessageTime = lastMessageTime;
        this.lastMessage = lastMessage;
        this.users = users;
        this.chatReferenceId=chatReferenceId;
        this.referenceName=referenceName;
        this.profileImage=profileImage;
        this.createdBy=createdBy;
        this.allowedUsers = allowedUsers;
        this.referenceType=referenceType;
        this.referenceId=referenceId;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public void setAllowedUsers(List<String> allowedUsers) {
        this.allowedUsers = allowedUsers;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getReferenceName() {
        return referenceName;
    }

    public void setReferenceName(String referenceName) {
        this.referenceName = referenceName;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public long getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(long lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public List<ChatReferenceUser> getUsers() {
        return users;
    }

    public void setUsers(List<ChatReferenceUser> users) {
        this.users = users;
    }

    public List<String> getAllowedUsers() {
        return allowedUsers;
    }

    public void setAllowedUserMobiles(List<String> allowedUsers) {
        this.allowedUsers = allowedUsers;
    }

    @Exclude
    public String getChatReferenceId() {
        return chatReferenceId;
    }

    public void setChatReferenceId(String chatReferenceId) {
        this.chatReferenceId = chatReferenceId;
    }
}
