package com.satyam.clubgariya.modals;

import com.google.firebase.firestore.Exclude;

import java.util.List;

public class Blog {
    public String status;
    @Exclude
    public String documentID;
    public String uid;
    public String source;
    public long time;
    public String medialLink;
    public String description;
    public int likeCount;
    public int commentCount;
    public String lastComment;
    public String lastCommentUserImage;
    public String lastCommentUserName;
    public String lastLikeBy;

    public Blog() {

    }

    public Blog(String uid, long time, String medialLink, String description, String source, String status) {
        this.uid = uid;
        this.time = time;
        this.medialLink = medialLink;
        this.description = description;
        this.source = source;
        this.status = status;
    }

    public String getLastCommentUserName() {
        return lastCommentUserName;
    }

    public void setLastCommentUserName(String lastCommentUserName) {
        this.lastCommentUserName = lastCommentUserName;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getLastComment() {
        return lastComment;
    }

    public void setLastComment(String lastComment) {
        this.lastComment = lastComment;
    }

    public String getLastCommentUserImage() {
        return lastCommentUserImage;
    }

    public void setLastCommentUserImage(String lastCommentUserImage) {
        this.lastCommentUserImage = lastCommentUserImage;
    }

    public String getLastLikeBy() {
        return lastLikeBy;
    }

    public void setLastLikeBy(String lastLikeBy) {
        this.lastLikeBy = lastLikeBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Exclude
    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getMedialLink() {
        return medialLink;
    }

    public void setMedialLink(String medialLink) {
        this.medialLink = medialLink;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
