package edu.drake.research.android.lipswithmaps.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Mahesh Gaya on 12/4/16.
 */

public class LocationContract {
    public static final String CONTENT_AUTHORITY = "edu.drake.research.android.lipswithmaps.app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    //Constants for paths
    public static final String PATH_WIFI = "wifi";
    public static final String PATH_WIFI_READING = "wifi_reading";
    public static final String PATH_USER = "user";
    public static final String PATH_PHONE = "phone";
    public static final String PATH_BUILDING = "building";
    public static final String PATH_READING = "reading";


    /**
     * Wifi Table
     * contains:
     */
    public static final class WifiEntry implements BaseColumns{
        /**
         * Table Definition
         */
        //table name
        public static final String TABLE_NAME = "wifi";
        //Columns
        public static final String COLUMN_BSSID = "bssid";
        public static final String COLUMN_SSID = "ssid";
        public static final String COLUMN_CAPABILITIES = "capabilities";
        public static final String COLUMN_CHANNEL_WIDTH = "channel_width";
        public static final String COLUMN_FREQUENCY = "frequency";

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
}
