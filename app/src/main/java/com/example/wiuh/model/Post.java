package com.example.wiuh.model;


import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;


@IgnoreExtraProperties
public class Post {
    public String uid;
    public String title;
    public String author;
    public String body;
    public Integer like;

    @Exclude
    public String key;

    public Post() {
    }

    public Post(String uid, String title, String author, String body, Integer like) {
        this.uid = uid;
        this.title = title;
        this.author = author;
        this.body = body;
        this.like = like;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    @Exclude
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Post)) return false;
        return key.equals(((Post) other).getKey());
    }
}
