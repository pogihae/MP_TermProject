package com.example.wiuh.model;


import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;


@IgnoreExtraProperties
public class Wifi {
    public String SSID;
    public String MAC;


    @Exclude
    public String key;

    public Wifi() {
    }

    public Wifi(String SSID, String MAC) {
        this.SSID = SSID;
        this.MAC = MAC;

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
