package com.example.wiuh;

//singleton class
public class WifiInfo {
    private static final WifiInfo instance = new WifiInfo();
    private String SSID;
    private String MAC;
    private WifiInfo() {
    }

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
