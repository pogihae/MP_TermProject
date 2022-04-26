package com.example.wiuh;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

/** 사용자 정보
 *
 * */
@IgnoreExtraProperties
public class User {
    @Exclude
    public static final String INITIAL_NICKNAME = "SAMPLE";

    private String email;
    private String nickname;

    public User() {}

    public User(String email) {
        this.email = email;
        this.nickname = INITIAL_NICKNAME;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    @Exclude
    public boolean hasInitialNickName() {
        return nickname.equals(INITIAL_NICKNAME);
    }
}