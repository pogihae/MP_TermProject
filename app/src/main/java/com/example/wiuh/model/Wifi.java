package com.example.wiuh.model;


import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;


@IgnoreExtraProperties
public class Wifi {
    public String uid;
    public String SSID;
    public String MAC;

    public Wifi() { }

    public Wifi(String uid, String SSID, String MAC) {
        this.uid = uid;
        this.SSID = SSID;
        this.MAC = MAC;
    }
}
