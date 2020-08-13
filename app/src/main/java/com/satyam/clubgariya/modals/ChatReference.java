package com.satyam.clubgariya.modals;


import com.google.firebase.firestore.Exclude;

public class ChatReference {
     @Exclude
     String chatReferenceId;
     String lastMessageTime;
     String lastMessage;
     String partnerName;
     String partnerUid;
     String partner_profile_image;

    public ChatReference(){

    }

    public ChatReference(String chatReferenceId, String lastMessageTime, String lastMessage, String partnerName, String partnerUid, String partner_profile_image) {
        this.chatReferenceId = chatReferenceId;
        this.lastMessageTime = lastMessageTime;
        this.lastMessage = lastMessage;
        this.partnerName=partnerName;
        this.partnerUid=partnerUid;
        this.partner_profile_image=partner_profile_image;
    }

    public String getChatReferenceId() {
        return chatReferenceId;
    }

    public void setChatReferenceId(String chatReferenceId) {
        this.chatReferenceId = chatReferenceId;
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

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getPartnerUid() {
        return partnerUid;
    }

    public void setPartnerUid(String partnerUid) {
        this.partnerUid = partnerUid;
    }

    public String getPartner_profile_image() {
        return partner_profile_image;
    }

    public void setPartner_profile_image(String partner_profile_image) {
        this.partner_profile_image = partner_profile_image;
    }
}
