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

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Mahesh Gaya on 12/4/16.
 */

/**
 * This is the database schema for the location database
 * It stores information about the phone, the wifi, and the location readings
 */

public class LocationContract {
    public static final String CONTENT_AUTHORITY = "edu.drake.research.android.lipswithmaps.app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    //Constants for paths
    public static final String PATH_WIFI = "wifi";
    public static final String PATH_PHONE = "phone";
    public static final String PATH_READING = "reading";
    public static final String PATH_PHONE_READING = "phone_reading";
    public static final String PATH_WIFI_READING = "wifi_reading";

    /**
     * Wifi Table
     * This table contains only the name and BSSID of the wifi
     * For machine learning, we only need the unique BSSID and level readings
     * The readings will be stored in the reading table and will reference these
     * wifi when they are used
     */
    public static final class WifiEntry implements BaseColumns{
        /**
         * Table Definition
         */
        //table name
        public static final String TABLE_NAME = "wifi";
        //Columns
        public static final String COLUMN_BSSID = "bssid"; //string
        public static final String COLUMN_SSID = "ssid"; //string

        /**
         * Content provider
         */
        //defining the default paths
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_WIFI).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WIFI;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WIFI;

        //building the paths
        public static Uri buildWifiUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /**
     * Keeps track of the phone API level, Device, Model, Product
     * This will help us know if different phone will have impact
     * on the learning algorithm
     */
    public static final class PhoneEntry implements BaseColumns{
        /**
         * Table Definition
         */
        //Table name
        public static final String TABLE_NAME = "phone";
        //Columns
        public static final String COLUMN_MODEL = "model";
        public static final String COLUMN_PRODUCT = "product";
        public static final String COLUMN_DEVICE = "device";
        public static final String COLUMN_SDK_LEVEL = "sdk_level";

        /**
         * Content provider
         */
        //defining the default paths
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PHONE).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PHONE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PHONE;

        //building the paths
        public static Uri buildPhoneUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    /**
     * Merge Phone and reading together,
     * the unavailability of the phone information should not
     * hinder the storage of the reading
     */

    public static final class PhoneReadingEntry implements BaseColumns{
        /**
         * Table definition
         */
        //Table name
        public static final String TABLE_NAME = "phone_reading";
        //Columns
        public static final String COLUMN_READING_ID = "reading_id"; //foreign key
        public static final String COLUMN_PHONE_ID = "phone_id"; //foreign key

        /**
         * Content provider
         */
        //defining the default paths
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PHONE_READING).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PHONE_READING;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PHONE_READING;

        //building the paths
        public static Uri buildPhoneReadingUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /**
     * We can use Google Play Services (ActivityRecognitionApi) to get the behavior of the user
     * Is he running, walking or still?
     * ----
     * The reading entry table will store data about the readings
     * The readings we are concerned with are:
     * longitude, latitude, altitude (although this is not always accurate),
     * accelerometer
     */
    public static final class ReadingEntry implements BaseColumns{
        /**
         * Table Definition
         */
        //Table name
        public static final String TABLE_NAME = "reading";
        //Columns
        /** Keeps track of time */
        public static final String COLUMN_TIME_STAMP = "timestamp"; //string

        /** keeps track of location */
        public static final String COLUMN_LONGITUDE = "longitude"; //double
        public static final String COLUMN_LATITUDE = "latitude"; //double
        public static final String COLUMN_ALTITUDE = "altitude"; //double
        public static final String COLUMN_ACCURACY = "accuracy"; //double
        public static final String COLUMN_LOCATION_PROVIDER = "location_provider"; //String, either NETWORK or GPS or etc

        /** keeps track of the sensors */
        //accelerometer
        public static final String COLUMN_ACCELEROMETER_X = "accelerometer_x"; //double
        public static final String COLUMN_ACCELEROMETER_Y = "accelerometer_y"; //double
        public static final String COLUMN_ACCELEROMETER_Z = "accelerometer_z"; //double
        //rotation
        public static final String COLUMN_ROTATION_X = "rotation_x"; //double
        public static final String COLUMN_ROTATION_Y = "rotation_y"; //double
        public static final String COLUMN_ROTATION_Z = "rotation_z"; //double
        //magnetometer
        public static final String COLUMN_MAGNETIC_X = "magnetic_x"; //double
        public static final String COLUMN_MAGNETIC_Y = "magnetic_y"; //double
        public static final String COLUMN_MAGNETIC_Z = "magnetic_z"; //double
        //orientation
        public static final String COLUMN_ORIENTATION_X = "orientation_x"; //double
        public static final String COLUMN_ORIENTATION_Y = "orientation_y"; //double
        public static final String COLUMN_ORIENTATION_Z = "orientation_z"; //double

        /**
         * Content provider
         */
        //defining the default paths
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_READING).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_READING;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_READING;

        //building the paths
        public static Uri buildReadingUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /**
     * This table links wifi and reading together
     */
    public static final class WifiReadingEntry implements BaseColumns{
        //Table Name
        public static final String TABLE_NAME = "wifi_reading";
        //Columns
        public static final String COLUMN_READING_ID = "reading_id"; //foreign key
        public static final String COLUMN_WIFI_ID = "wifi_id"; //foreign key
        public static final String COLUMN_WIFI_READING = "wifi_reading"; //integer


        /**
         * Content provider
         */
        //defining the default paths
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_WIFI_READING).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WIFI_READING;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WIFI_READING;

        //building the paths
        public static Uri buildWifiReadingUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}
