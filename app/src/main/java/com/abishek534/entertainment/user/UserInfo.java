package com.abishek534.entertainment.user;

public class UserInfo {
    private String userName;
    private String userEmail;
    private String userImage;
    private String userMob;

    public UserInfo(){}

    public UserInfo(String userName, String userEmail, String userImage, String userMob) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userImage = userImage;
        this.userMob = userMob;
    }


    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserImage() {
        return userImage;
    }

    public String getUserMob() {
        return userMob;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public void setUserMob(String userMob) {
        this.userMob = userMob;
    }
}
