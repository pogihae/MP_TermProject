package com.example.wiuh.model;

public class WifiInformation {
    private WifiInformation() {}
    private static final WifiInformation instance = new WifiInformation();

    private String SSID;
    private String MAC;

    public static void setInfo(String SSID, String MAC) {
        instance.SSID = SSID;
        instance.MAC = MAC;
    }

    public static String getSSID() {
        return instance.SSID;
    }
    public static String getMAC() {
        return instance.MAC;
    }
}
