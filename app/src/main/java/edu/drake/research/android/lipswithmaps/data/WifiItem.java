/*
 * Copyright 2017 Drake University
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
