package edu.drake.research.lipswithmaps;

import java.util.List;

/**
 * Created by Mahesh Gaya on 3/22/17.
 */

/**
 * This class is the way will we store data into the database
 */
public class Reading {
    public static final String TABLE_NAME = "reading";
    //The variable names will reflect how the data will be stored in the database
    private long timestamp;
    private LocationLngLat location;
    private PhoneInfo user;
    private List<WifiItem> wifilist;
    private Magnetometer magnetometer;
    private RotationMeter rotationmeter;
    private Accelerometer accelerometer;

    public Reading(){}

    public Reading(long timestamp, List<WifiItem> wifilist, LocationLngLat location,
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

    public Accelerometer getAccelerometer() {
        return accelerometer;
    }

    public void setAccelerometer(Accelerometer accelerometer) {
        this.accelerometer = accelerometer;
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
