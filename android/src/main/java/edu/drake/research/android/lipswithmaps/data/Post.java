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
    private long timestamp;
    private LocationLngLat location;
    private PhoneInfo user;
    private List<WifiItem> wifilist;
    private Magnetometer magnetometer;
    private RotationMeter rotationmeter;
    private Accelerometer accelerometer;

    public Post(long timestamp, List<WifiItem> wifilist, LocationLngLat location,
                Accelerometer accelerometer, Magnetometer magnetometer,
                RotationMeter rotationmeter, PhoneInfo phoneInfo){
        this.timestamp = timestamp;
        this.wifilist = wifilist;
        this.location = location;
        this.accelerometer = accelerometer;
        this.magnetometer = magnetometer;
        this.rotationmeter = rotationmeter;
        this.user = phoneInfo;

    }

    @Override
    public String toString(){
        StringBuilder wifiStringBuilder = new StringBuilder();
        for (WifiItem wifiItem: wifilist){
            wifiStringBuilder.append(wifiItem.toString() + ", ");
        }
        return timestamp + ", " + wifiStringBuilder.toString() + ", " + accelerometer.toString() +
                magnetometer.toString() + ", " + rotationmeter.toString() + ", " + location.toString();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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

    public List<WifiItem> getWifilist() {
        return wifilist;
    }

    public void setWifilist(List<WifiItem> wifilist) {
        this.wifilist = wifilist;
    }

    public Magnetometer getMagnetometer() {
        return magnetometer;
    }

    public void setMagnetometer(Magnetometer magnetometer) {
        this.magnetometer = magnetometer;
    }

    public RotationMeter getRotationmeter() {
        return rotationmeter;
    }

    public void setRotationmeter(RotationMeter rotationmeter) {
        this.rotationmeter = rotationmeter;
    }
}
