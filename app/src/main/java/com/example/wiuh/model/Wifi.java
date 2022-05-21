package com.example.wiuh.model;


import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;


@IgnoreExtraProperties
public class Wifi {
    @Exclude
    public String MAC;

    public String SSID;

    public Wifi() { }

    public Wifi(String SSID, String MAC) {
        this.SSID = SSID;
        this.MAC = MAC;
    }
}
