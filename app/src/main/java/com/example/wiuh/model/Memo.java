package com.example.wiuh.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Memo {
    public String uid;
    public String title;
    public String author;
    public String body;

    public Memo() {}

    public Memo(String uid, String title, String author, String body) {
        this.uid = uid;
        this.title = title;
        this.author = author;
        this.body = body;
    }

    @Exclude
    public String key;
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }
    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    @Override
    public boolean equals(Object other) {
        if(!(other instanceof Memo)) return false;
        return key.equals(((Memo) other).getKey());
    }
}
