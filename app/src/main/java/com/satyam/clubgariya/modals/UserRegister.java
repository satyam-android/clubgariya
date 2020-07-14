package com.satyam.clubgariya.modals;

public class UserRegister {
    String imageUrl;
    String fName;
    String lName;
    String email;
    String password;
    String mobile;
    String uid;

    public UserRegister() {
    }

    public UserRegister(String fName, String lName, String email, String password, String mobile,String uid,String imageUrl) {
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.uid=uid;
        this.imageUrl=imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
