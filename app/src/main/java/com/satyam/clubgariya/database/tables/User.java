package com.satyam.clubgariya.database.tables;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "Users", indices = @Index(value = {"mobile"}, unique = true))
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String imageUrl;
    public String name;
    public String email;
    public String mobile;
    public String uid;
    public String creationTime;
    public String fcm_token;
    public String status;
    public String profileStatus;
    public double totalCredit;
    public double totalDebit;
    public boolean clubMember;

    public User() {
    }

    public User(String name, String status, String profileStatus, String email, String mobile, String uid, String imageUrl, String creationTime, double totalCredit, double totalDebit, String fcm_token, boolean isClubMember) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.status = status;
        this.profileStatus = profileStatus;
        this.clubMember = isClubMember;
        this.email = email;
        this.mobile = mobile;
        this.uid = uid;
        this.creationTime = creationTime;
        this.fcm_token = fcm_token;
        this.totalCredit = totalCredit;
        this.totalDebit = totalDebit;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
