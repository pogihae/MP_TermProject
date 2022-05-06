package com.example.wiuh.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Post {
    public String uid;
    public String title;
    public String author;
    public String body;

    @Exclude
    public String key;

    public Post() {}

    public Post(String uid, String title, String author, String body) {
        this.uid = uid;
        this.title = title;
        this.author = author;
        this.body = body;
    }
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }
}
