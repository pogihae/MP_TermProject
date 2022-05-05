package com.example.wiuh.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Memo {
    public String uid;
    public String title;
    public String author;
    public String body;
    public String key;

    public Memo() {}

    public Memo(String uid, String title, String author, String body, String key) {
        this.uid = uid;
        this.title = title;
        this.author = author;
        this.body = body;
        this.key = key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
