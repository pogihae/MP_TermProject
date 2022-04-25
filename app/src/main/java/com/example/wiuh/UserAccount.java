package com.example.wiuh;

/*
    사용자 계정 정보 클래스
 */

public class UserAccount {

    private String emailId;
    private String password;
    private String idToken;     // ID 고유정보
    private String nickname;

    public UserAccount() { }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}