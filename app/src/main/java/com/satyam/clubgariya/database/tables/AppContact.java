package com.satyam.clubgariya.database.tables;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "AppContact")
public class AppContact {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String mobileNumber;
    public String displayName;
    public String timestamp;
    public String status;
    public boolean isClubUser;
    public String uid;

    public AppContact(){

    }

    public AppContact(String mobileNumber, String displayName, String timestamp, String status,boolean isClubUser) {
        this.mobileNumber = mobileNumber;
        this.displayName = displayName;
        this.timestamp = timestamp;
        this.status = status;
        this.isClubUser=isClubUser;
    }

    public boolean getisClubUser() {
        return isClubUser;
    }

    public void setisClubUser(boolean user) {
        this.isClubUser = user;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
