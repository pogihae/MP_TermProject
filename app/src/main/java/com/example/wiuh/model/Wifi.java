package com.example.wiuh.model;


import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;


@IgnoreExtraProperties
public class Wifi {
    public String SSID;
    public String MAC;

    public Wifi() { }

    public Wifi(String SSID, String MAC) {
        this.SSID = SSID;
        this.MAC = MAC;
    }
}
