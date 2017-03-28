package edu.drake.research.lipswithmaps;

/**
 * Created by Mahesh Gaya on 3/22/17.
 */

public class RotationMeter {
    public static final String TABLE_NAME = "orientation";
    private double x;
    private double y;
    private double z;

    public RotationMeter(){
        this.x = this.y = this.z = 0;
    }

    public RotationMeter(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString(){
        return x + " :: " + y + " :: " + z;
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
