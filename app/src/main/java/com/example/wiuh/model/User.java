package com.example.wiuh.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

/** 사용자 정보
 *
 * */
@IgnoreExtraProperties
public class User {
    @Exclude
    public static final String INITIAL_NICKNAME = "SAMPLE";
    @Exclude
    private static User CUR_USER_INSTANCE;

    public String email;
    public String nickname;

    public User() {}

    public User(String email) {
        this.email = email;
        this.nickname = INITIAL_NICKNAME;
    }

    @Exclude
    public boolean hasInitialNickName() {
        return nickname.equals(INITIAL_NICKNAME);
    }
    @Exclude
    public static User getCurUserInstance() { return CUR_USER_INSTANCE; }
    @Exclude
    public static void setCurUserInstance(User user) { CUR_USER_INSTANCE = user; }
}