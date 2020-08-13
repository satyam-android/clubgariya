package com.satyam.clubgariya.modals;

public class TransactionReference {
    private String transReferenceId;
    private String lastMessageTime;
    private String lastMessage;
    private String partnerName;
    private double userCredit;
    private double partnerCredit;
    private String partnerUid;
    private String userName;
    private String userUid;
    private String userProfile_image;

    public TransactionReference(){

    }

    public TransactionReference(String transReferenceId, String lastMessageTime, String lastMessage, String partnerName,String partnerUid,String userName,String userUid,double userCredit,double partnerCredit,String userProfile_image) {
        this.transReferenceId = transReferenceId;
        this.lastMessageTime = lastMessageTime;
        this.lastMessage = lastMessage;
        this.partnerName=partnerName;
        this.partnerUid=partnerUid;
        this.userCredit=userCredit;
        this.partnerCredit=partnerCredit;
        this.userName=userName;
        this.userUid=userUid;
        this.userProfile_image=userProfile_image;
    }

    public String getUserProfile_image() {
        return userProfile_image;
    }

    public void setUserProfile_image(String userProfile_image) {
        this.userProfile_image = userProfile_image;
    }

    public String getPartnerUid() {
        return partnerUid;
    }

    public void setPartnerUid(String partnerUid) {
        this.partnerUid = partnerUid;
    }

    public double getUserCredit() {
        return userCredit;
    }

    public void setUserCredit(double userCredit) {
        this.userCredit = userCredit;
    }

    public double getPartnerCredit() {
        return partnerCredit;
    }

    public void setPartnerCredit(double partnerCredit) {
        this.partnerCredit = partnerCredit;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getTransactionReferenceId() {
        return transReferenceId;
    }

    public void setTransactionReferenceId(String chatReferenceId) {
        this.transReferenceId = chatReferenceId;
    }

    public String getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(String lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
