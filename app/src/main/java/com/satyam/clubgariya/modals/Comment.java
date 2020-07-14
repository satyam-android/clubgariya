package com.satyam.clubgariya.modals;

import com.google.firebase.firestore.Exclude;

public class Comment {

    String blogId;
    String uid;
    String profileImageUrl;
    String userName;
    String comment;
    long time;

    public Comment(){

    }

    public Comment(String blogId,String uid, String userName, String comment,String profileImageUrl,long time) {
        this.blogId=blogId;
        this.uid = uid;
        this.userName = userName;
        this.comment = comment;
        this.profileImageUrl=profileImageUrl;
        this.time=time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
    @Exclude
    public String getBlogId() {
        return blogId;
    }

    public void setBlogId(String blogId) {
        this.blogId = blogId;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
