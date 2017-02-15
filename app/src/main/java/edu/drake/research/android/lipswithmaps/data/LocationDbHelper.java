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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mahesh Gaya on 12/4/16.
 */


public class LocationDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "location";
    public static final int DATABASE_VERSION = 1;

    /**
     * Required Constructor
     * @param context
     */
    public LocationDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        /**
         * CREATE TABLE wifi (
         * _id INTEGER PRIMARY KEY AUTOINCREMENT,
         * bssid TEXT UNIQUE,
         * ssid TEXT
         * );
         */
        final String SQL_CREATE_WIFI_TABLE =
                "CREATE TABLE " + LocationContract.WifiEntry.TABLE_NAME + "( " +
                        LocationContract.WifiEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        LocationContract.WifiEntry.COLUMN_BSSID + " TEXT UNIQUE, " +
                        LocationContract.WifiEntry.COLUMN_SSID + " TEXT " +
                        ");";

        /**
         * CREATE TABLE phone (
         * _id INTEGER PRIMARY KEY AUTOINCREMENT ,
         * model TEXT UNIQUE,
         * product TEXT,
         * device TEXT,
         * sdk_level TEXT
         * );
         */
        final String SQL_CREATE_PHONE_TABLE =
                "CREATE TABLE " + LocationContract.PhoneEntry.TABLE_NAME + "( " +
                        LocationContract.PhoneEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        LocationContract.PhoneEntry.COLUMN_MODEL + " TEXT UNIQUE, " +
                        LocationContract.PhoneEntry.COLUMN_PRODUCT + " TEXT, " +
                        LocationContract.PhoneEntry.COLUMN_DEVICE + " TEXT, " +
                        LocationContract.PhoneEntry.COLUMN_SDK_LEVEL + " TEXT " +
                        ");";

        /**
         * CREATE TABLE reading (
         * _id INTEGER PRIMARY KEY AUTOINCREMENT,
         * timestamp TEXT NOT NULL,
         * longitude REAL NOT NULL,
         * latitude REAL NOT NULL,
         * altitude REAL NOT NULL,
         * accuracy REAL NOT NULL,
         * location_provider TEXT NOT NULL,
         * accelerometer_x REAL NOT NULL,
         * accelerometer_y REAL NOT NULL,
         * accelerometer_z REAL NOT NULL,
         * rotation_x REAL NOT NULL,
         * rotation_y REAL NOT NULL,
         * rotation_z REAL NOT NULL,
         * magnetic_x REAL NOT NULL,
         * magnetic_y REAL NOT NULL,
         * magnetic_z REAL NOT NULL,
         * orientation_x REAL NOT NULL,
         * orientation_y REAL NOT NULL,
         * orientation_z REAL NOT NULL
         * );
         */
        final String SQL_CREATE_READING_TABLE =
                "CREATE TABLE " + LocationContract.ReadingEntry.TABLE_NAME + "( " +
                        LocationContract.ReadingEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        LocationContract.ReadingEntry.COLUMN_TIME_STAMP + " TEXT NOT NULL, " +
                        //location
                        LocationContract.ReadingEntry.COLUMN_LONGITUDE + " REAL NOT NULL, " +
                        LocationContract.ReadingEntry.COLUMN_LATITUDE + " REAL NOT NULL, " +
                        LocationContract.ReadingEntry.COLUMN_ALTITUDE + " REAL NOT NULL, " +
                        LocationContract.ReadingEntry.COLUMN_ACCURACY + " REAL NOT NULL, " +
                        LocationContract.ReadingEntry.COLUMN_LOCATION_PROVIDER + " TEXT NOT NULL, " +
                        //accelerometer
                        LocationContract.ReadingEntry.COLUMN_ACCELEROMETER_X + " REAL NOT NULL, " +
                        LocationContract.ReadingEntry.COLUMN_ACCELEROMETER_Y + " REAL NOT NULL, " +
                        LocationContract.ReadingEntry.COLUMN_ACCELEROMETER_Z + " REAL NOT NULL, " +
                        //rotation
                        LocationContract.ReadingEntry.COLUMN_ROTATION_X + " REAL NOT NULL, " +
                        LocationContract.ReadingEntry.COLUMN_ROTATION_Y + " REAL NOT NULL, " +
                        LocationContract.ReadingEntry.COLUMN_ROTATION_Z + " REAL NOT NULL, " +
                        //magnetic
                        LocationContract.ReadingEntry.COLUMN_MAGNETIC_X + " REAL NOT NULL, " +
                        LocationContract.ReadingEntry.COLUMN_MAGNETIC_Y + " REAL NOT NULL, " +
                        LocationContract.ReadingEntry.COLUMN_MAGNETIC_Z + " REAL NOT NULL, " +
                        //orientation
                        LocationContract.ReadingEntry.COLUMN_ORIENTATION_X + " REAL NOT NULL, " +
                        LocationContract.ReadingEntry.COLUMN_ORIENTATION_Y + " REAL NOT NULL, " +
                        LocationContract.ReadingEntry.COLUMN_ORIENTATION_Z + " REAL NOT NULL " +
                        ");";

        /**
         * CREATE TABLE phone_reading (
         * _id INTEGER PRIMARY KEY AUTOINCREMENT,
         * reading_id INTEGER NOT NULL,
         * phone_id INTEGER NOT NULL,
         * FOREIGN KEY (reading_id) REFERENCES reading(_id),
         * FOREIGN KEY (phone_id) REFERENCES phone(_id)
         * );
         */
        final String SQL_CREATE_PHONE_READING_TABLE =
                "CREATE TABLE " + LocationContract.PhoneReadingEntry.TABLE_NAME + "( " +
                        LocationContract.PhoneReadingEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        LocationContract.PhoneReadingEntry.COLUMN_READING_ID + " INTEGER NOT NULL, " +
                        LocationContract.PhoneReadingEntry.COLUMN_PHONE_ID + " INTEGER NOT NULL, " +
                        "FOREIGN KEY (" + LocationContract.PhoneReadingEntry.COLUMN_READING_ID + ") " +
                        "REFERENCES " + LocationContract.ReadingEntry.TABLE_NAME + "(" + LocationContract.ReadingEntry._ID + "), " +
                        "FOREIGN KEY (" + LocationContract.PhoneReadingEntry.COLUMN_PHONE_ID + ") " +
                        "REFERENCES " + LocationContract.PhoneEntry.TABLE_NAME + "(" + LocationContract.PhoneEntry._ID + ") " +
                        ");";

        final String SQL_CREATE_WIFI_READING_TABLE =
                "CREATE TABLE" + LocationContract.WifiReadingEntry.TABLE_NAME + "( " +
                        LocationContract.WifiReadingEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        LocationContract.WifiReadingEntry.COLUMN_READING_ID + " INTEGER NOT NULL, " +
                        LocationContract.WifiReadingEntry.COLUMN_WIFI_ID + " INTEGER NOT NULL, " +
                        LocationContract.WifiReadingEntry.COLUMN_WIFI_READING + " REAL NOT NULL, " +
                        "FOREIGN KEY (" + LocationContract.WifiReadingEntry.COLUMN_READING_ID + ") " +
                        "REFERENCES " + LocationContract.ReadingEntry.TABLE_NAME + "(" + LocationContract.ReadingEntry._ID + "), " +
                        "FOREIGN KEY (" + LocationContract.WifiReadingEntry.COLUMN_WIFI_ID + ") " +
                        "REFERENCES " + LocationContract.WifiEntry.TABLE_NAME + "(" + LocationContract.WifiEntry._ID + ") " +
                        ");";

        //Create the tables
        sqLiteDatabase.execSQL(SQL_CREATE_WIFI_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PHONE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_READING_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PHONE_READING_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_WIFI_READING_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + LocationContract.WifiEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + LocationContract.PhoneEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + LocationContract.ReadingEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + LocationContract.PhoneReadingEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + LocationContract.WifiReadingEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
