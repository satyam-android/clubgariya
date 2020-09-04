package com.satyam.clubgariya.modals;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.List;

public class TransactionReference {
    @Exclude
    private String transReferenceId;
    private long lastMessageTime;
    private String lastMessage;
    private double totalCredit;
    private List<String> allowedUids;
    private List<TransactionReferenceUser> users;
    private String createdBy;
    private String groupUserId;
    private String referenceType;

    public TransactionReference(){

    }

    public TransactionReference(String transReferenceId,String createdBy,String groupUserId,String referenceType,long lastMessageTime, String lastMessage,double totalCredit,List<String> allowedUids,List<TransactionReferenceUser> users) {
        this.lastMessageTime = lastMessageTime;
        this.lastMessage = lastMessage;
        this.transReferenceId=transReferenceId;
        this.allowedUids=allowedUids;
        this.users = users;
        this.referenceType=referenceType;
        this.totalCredit=totalCredit;
        this.createdBy=createdBy;
        this.groupUserId=groupUserId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getGroupUserId() {
        return groupUserId;
    }

    public void setGroupUserId(String groupUserId) {
        this.groupUserId = groupUserId;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public List<String> getAllowedUids() {
        return allowedUids;
    }

    public void setAllowedUids(List<String> userIds) {
        this.allowedUids = userIds;
    }

    public double getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(double totalCredit) {
        this.totalCredit = totalCredit;
    }

    @Exclude
    public String getTransReferenceId() {
        return transReferenceId;
    }


    public void setTransReferenceId(String transReferenceId) {
        this.transReferenceId = transReferenceId;
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

    public List<TransactionReferenceUser> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<TransactionReferenceUser> users) {
        this.users = users;
    }
}
