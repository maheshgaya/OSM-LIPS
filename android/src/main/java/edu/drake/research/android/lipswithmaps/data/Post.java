package edu.drake.research.android.lipswithmaps.data;

import java.util.List;

/**
 * Created by Mahesh Gaya on 3/22/17.
 */

/**
 * This class is the way will we store data into the database
 */
public class Post{
    public static final String TABLE_NAME = "Post";
    private long timeStamp;
    private LocationLngLat location;
    private PhoneInfo user;
    private List<WifiItem> wifiItemList;
    private Magnetometer magnetometer;
    private RotationMeter rotationMeter;
    private Accelerometer accelerometer;

    public Post(long timeStamp, List<WifiItem> wifiItemList, LocationLngLat location,
                Accelerometer accelerometer, Magnetometer magnetometer,
                RotationMeter rotationMeter, PhoneInfo phoneInfo){
        this.timeStamp = timeStamp;
        this.wifiItemList = wifiItemList;
        this.location = location;
        this.accelerometer = accelerometer;
        this.magnetometer = magnetometer;
        this.rotationMeter = rotationMeter;
        this.user = phoneInfo;

    }

    @Override
    public String toString(){
        StringBuilder wifiStringBuilder = new StringBuilder();
        for (WifiItem wifiItem: wifiItemList){
            wifiStringBuilder.append(wifiItem.toString() + ", ");
        }
        return timeStamp + ", " + wifiStringBuilder.toString() + ", " + accelerometer.toString() +
                magnetometer.toString() + ", " + rotationMeter.toString() + ", " + location.toString();
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
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

    public RotationMeter getRotationMeter() {
        return rotationMeter;
    }

    public void setRotationMeter(RotationMeter rotationMeter) {
        this.rotationMeter = rotationMeter;
    }
}
