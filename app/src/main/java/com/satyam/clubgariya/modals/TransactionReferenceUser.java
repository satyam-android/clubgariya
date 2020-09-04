package com.satyam.clubgariya.modals;

public class TransactionReferenceUser {
    private String userUid;
    private String userRole;
//    private String userName;
//    private String userStatus;
//    private String userProfileImage;
    private double userCredit;
    private double userShare;

    public TransactionReferenceUser() {
    }

    public TransactionReferenceUser(String userUid, String userRole,double userCredit, double userShare) {
        this.userUid = userUid;
        this.userRole = userRole;
//        this.userName=userName;
//        this.userProfileImage=userProfileImage;
//        this.userStatus=userStatus;
        this.userCredit = userCredit;
        this.userShare = userShare;
    }

  /*  public String getUserProfileImage() {
        return userProfileImage;
    }

    public void setUserProfileImage(String userProfileImage) {
        this.userProfileImage = userProfileImage;
    }*/

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

 /*   public String getUserStatus() {
        return userStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }*/

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public double getUserCredit() {
        return userCredit;
    }

    public void setUserCredit(double userCredit) {
        this.userCredit = userCredit;
    }

    public double getUserShare() {
        return userShare;
    }

    public void setUserShare(double userShare) {
        this.userShare = userShare;
    }

}
