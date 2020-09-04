package com.satyam.clubgariya.database.tables;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "Users", indices = @Index(value = {"mobile"}, unique = true))
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String imageUrl;
    public String name;
    public String email;
    public String mobile;
    public String uid;
    public String userType;
    public List<String> chatGroups;
    public List<String> transactionGroups;
    public String userBusiness;
    public long creationTime;
    public String fcm_token;
    public String userStatus;
    public String profileStatus;
    public double totalCredit;
    public double totalDebit;
    public boolean clubMember;

    public User() {
    }

    public User(String name, String userStatus, String profileStatus, String email,String userType, String userBusiness,String mobile, String uid, String imageUrl, long creationTime, double totalCredit, double totalDebit, String fcm_token, boolean isClubMember,List<String> chatGroups,List<String> transactionGroups) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.userStatus = userStatus;
        this.chatGroups=chatGroups;
        this.transactionGroups=transactionGroups;
        this.userType=userType;
        this.profileStatus = profileStatus;
        this.clubMember = isClubMember;
        this.email = email;
        this.userBusiness=userBusiness;
        this.mobile = mobile;
        this.uid = uid;
        this.creationTime = creationTime;
        this.fcm_token = fcm_token;
        this.totalCredit = totalCredit;
        this.totalDebit = totalDebit;
    }

    public List<String> getTransactionGroups() {
        return transactionGroups;
    }

    public void setTransactionGroups(List<String> transactionGroups) {
        this.transactionGroups = transactionGroups;
    }

    public List<String> getChatGroups() {
        return chatGroups;
    }

    public void setChatGroups(List<String> chatGroups) {
        this.chatGroups = chatGroups;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserBusiness() {
        return userBusiness;
    }

    public void setUserBusiness(String userBusiness) {
        this.userBusiness = userBusiness;
    }

    public String getProfileStatus() {
        return profileStatus;
    }

    public void setProfileStatus(String profileStatus) {
        this.profileStatus = profileStatus;
    }

    public boolean isClubMember() {
        return clubMember;
    }

    public void setClubMember(boolean clubMember) {
        this.clubMember = clubMember;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public double getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(double totalCredit) {
        this.totalCredit = totalCredit;
    }

    public double getTotalDebit() {
        return totalDebit;
    }

    public void setTotalDebit(double totalDebit) {
        this.totalDebit = totalDebit;
    }

    public String getFcm_token() {
        return fcm_token;
    }

    public void setFcm_token(String fcm_token) {
        this.fcm_token = fcm_token;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
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
