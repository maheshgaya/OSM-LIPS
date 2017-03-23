package edu.drake.research.android.lipswithmaps.data;

import java.util.List;

/**
 * Created by Mahesh Gaya on 3/22/17.
 */

/**
 * This class is the way will we store data into the database
 */
public class Post{
    private int timeStamp;
    private LocationLngLat location;
    private PhoneInfo user;
    private List<WifiItem> wifiItemList;
    private Magnetometer magnetometer;
    private OrientationMeter orientationMeter;
    private Accelerometer accelerometer;

    public Post(int timeStamp, List<WifiItem> wifiItemList, LocationLngLat location,
                Magnetometer magnetometer, OrientationMeter orientationMeter, PhoneInfo phoneInfo){
        this.timeStamp = timeStamp;
        this.wifiItemList = wifiItemList;
        this.location = location;
        this.magnetometer = magnetometer;
        this.orientationMeter = orientationMeter;
        this.user = phoneInfo;

    }

    public int getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

    public LocationLngLat getLocation() {
        return location;
    }

    public void setLocation(LocationLngLat location) {
        this.location = location;
    }

    public PhoneInfo getUser() {
        return user;
    }

    public void setUser(PhoneInfo user) {
        this.user = user;
    }

    public List<WifiItem> getWifiItemList() {
        return wifiItemList;
    }

    public void setWifiItemList(List<WifiItem> wifiItemList) {
        this.wifiItemList = wifiItemList;
    }

    public Magnetometer getMagnetometer() {
        return magnetometer;
    }

    public void setMagnetometer(Magnetometer magnetometer) {
        this.magnetometer = magnetometer;
    }

    public OrientationMeter getOrientationMeter() {
        return orientationMeter;
    }

    public void setOrientationMeter(OrientationMeter orientationMeter) {
        this.orientationMeter = orientationMeter;
    }
}
