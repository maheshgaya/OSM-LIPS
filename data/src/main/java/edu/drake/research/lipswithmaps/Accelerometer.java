package edu.drake.research.lipswithmaps;

/**
 * Created by Mahesh Gaya on 3/22/17.
 */

public class Accelerometer {
    public static final String TABLE_NAME = "accelerometer";
    private double x;
    private double y;
    private double z;

    public Accelerometer(){
        this.x = this.y = this.z = 0;
    }

    public Accelerometer(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString(){
        return "Accelerometer [x=" + x + ", y=" + y + ", z=" + z + "];";
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
