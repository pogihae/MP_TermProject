package com.example.wiuh;

//singleton class
public class WifiState {
    private static final WifiState instance = new WifiState();

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
