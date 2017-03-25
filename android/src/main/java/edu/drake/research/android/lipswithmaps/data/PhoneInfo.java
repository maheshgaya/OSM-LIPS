/*
 * Copyright 2017 Mahesh Gaya
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
 * Created by Mahesh Gaya on 1/29/17.
 */

public class PhoneInfo {
    public static final String TABLE_NAME = "phone";
    private String product;
    private String sdklevel;
    private String device;
    private String model;

    public PhoneInfo(String product, String sdklevel, String device, String model){
        this.product = product;
        this.sdklevel = sdklevel;
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

    public String getSdklevel() {
        return sdklevel;
    }

    public void setSdklevel(String sdklevel) {
        this.sdklevel = sdklevel;
    }

    public String toString(){
        return this.product + " -- " + this.sdklevel + " -- " + this.device + " -- " + this.model;
    }
}
