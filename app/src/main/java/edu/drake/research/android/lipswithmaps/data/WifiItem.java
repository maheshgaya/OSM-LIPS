package edu.drake.research.android.lipswithmaps.data;

/**
 * Created by Mahesh Gaya on 1/15/17.
 */

public class WifiItem {
    private String ssid;
    private String bssid;
    private int level;

    //Constructor
    public WifiItem(String ssid, String bssid, int level){
        this.ssid = ssid;
        this.bssid = bssid;
        this.level = level;
    }

    //Getter and Setter methods
    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String toString(){
        return this.bssid + " -- " + this.ssid + " -- " + this.level;
    }
}
