package edu.drake.research.android.lipswithmaps.data;

/**
 * Created by Mahesh Gaya on 1/15/17.
 */

public class WifiItem {
    private String ssid;
    private int level;

    public WifiItem(String ssid, int level){
        this.ssid = ssid;
        this.level = level;
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
}
