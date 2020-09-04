package com.satyam.clubgariya.modals;

public class ChatReferenceUser {
    private String userUid;
    private String userRole;
//    private String userMobile;
//    private String userStatus;
//    private String userProfileImage;

    public ChatReferenceUser(){

    }

    public ChatReferenceUser(String userUid,String userRole) {
        this.userUid = userUid;
        this.userRole = userRole;
//        this.userMobile = userMobile;
//        this.userStatus = userStatus;
//        this.userProfileImage = userProfileImage;
    }



    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    /*
    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }*/

  /*  public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserProfileImage() {
        return userProfileImage;
    }

    public void setUserProfileImage(String userProfileImage) {
        this.userProfileImage = userProfileImage;
    }*/
}
