package com.example.wiuh.model;


import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;


@IgnoreExtraProperties
public class Wifi {
    @Exclude
    public String uid;
    @Exclude
    public String MAC;

    public String SSID;

    public Wifi() { }

    public Wifi(String uid, String SSID, String MAC) {
        this.uid = uid;
        this.SSID = SSID;
        this.MAC = MAC;
    }
}
