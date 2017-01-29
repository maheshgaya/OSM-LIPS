package edu.drake.research.android.lipswithmaps.data;

import java.io.Serializable;

/**
 * Created by Mahesh Gaya on 1/29/17.
 */

public class PhoneInfo {
    private String product;
    private String sdkLevel;
    private String device;
    private String model;

    public PhoneInfo(String product, String sdkLevel, String device, String model){
        this.product = product;
        this.sdkLevel = sdkLevel;
        this.device = device;
        this.model = model;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getSdkLevel() {
        return sdkLevel;
    }

    public void setSdkLevel(String sdkLevel) {
        this.sdkLevel = sdkLevel;
    }

    public String toString(){
        return this.product + " -- " + this.sdkLevel + " -- " + this.device + " -- " + this.model;
    }
}
