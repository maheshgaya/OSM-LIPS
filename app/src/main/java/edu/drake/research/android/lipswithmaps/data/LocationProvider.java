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

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Mahesh Gaya on 12/4/16.
 */

public class LocationProvider extends ContentProvider{
    private static final String TAG = LocationProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private LocationDbHelper mLocationDbHelper;

    //For URI
    private static final int READING = 100;
    private static final int READING_WITH_ID = 101;

    private static final int WIFI = 200;
    private static final int WIFI_WITH_ID = 201;

    private static final int PHONE = 300;
    private static final int PHONE_WITH_ID = 301;

    private static final int PHONE_READING = 400;
    private static final int PHONE_READING_WITH_ID = 401;

    private static final int WIFI_READING = 500;
    private static final int WIFI_READING_WITH_ID = 501;

    public static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = LocationContract.CONTENT_AUTHORITY;

        //content://authority/reading
        matcher.addURI(authority, LocationContract.ReadingEntry.TABLE_NAME, READING);
        //content://authority/reading/#
        matcher.addURI(authority, LocationContract.ReadingEntry.TABLE_NAME + "/#", READING_WITH_ID);

        //content://authority/wifi
        matcher.addURI(authority, LocationContract.WifiEntry.TABLE_NAME, WIFI);
        //content://authority/reading/#
        matcher.addURI(authority, LocationContract.WifiEntry.TABLE_NAME + "/#", WIFI_WITH_ID);

        //content://authority/phone
        matcher.addURI(authority, LocationContract.PhoneEntry.TABLE_NAME, PHONE);
        //content://authority/phone/#
        matcher.addURI(authority, LocationContract.PhoneEntry.TABLE_NAME + "/#", PHONE_WITH_ID);

        //content://authority/phone_reading
        matcher.addURI(authority, LocationContract.PhoneReadingEntry.TABLE_NAME, PHONE_READING);
        //content://authority/phone_reading/#
        matcher.addURI(authority, LocationContract.PhoneReadingEntry.TABLE_NAME + "/#", PHONE_READING_WITH_ID);

        //content://authority/wifi_reading
        matcher.addURI(authority, LocationContract.WifiReadingEntry.TABLE_NAME, WIFI_READING);
        //content://authority/wifi_reading/#
        matcher.addURI(authority, LocationContract.WifiReadingEntry.TABLE_NAME + "/#", WIFI_READING_WITH_ID);

        return matcher;

    }

    @Override
    public boolean onCreate() {
        mLocationDbHelper = new LocationDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        //todo
        return null;
    }

    /**
     * get the type of content that the uri should return
     * CONTENT_TYPE is a list of records
     * CONTENT_ITEM_TYPE is a single record
     * @param uri
     * @return
     */
    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case READING:
                return LocationContract.ReadingEntry.CONTENT_TYPE;
            case READING_WITH_ID:
                return LocationContract.ReadingEntry.CONTENT_ITEM_TYPE;

            case WIFI:
                return LocationContract.WifiEntry.CONTENT_TYPE;
            case WIFI_WITH_ID:
                return LocationContract.WifiEntry.CONTENT_ITEM_TYPE;

            case PHONE:
                return LocationContract.PhoneEntry.CONTENT_TYPE;
            case PHONE_WITH_ID:
                return LocationContract.PhoneEntry.CONTENT_ITEM_TYPE;

            case PHONE_READING:
                return LocationContract.PhoneReadingEntry.CONTENT_TYPE;
            case PHONE_READING_WITH_ID:
                return LocationContract.PhoneReadingEntry.CONTENT_ITEM_TYPE;

            case WIFI_READING:
                return LocationContract.WifiReadingEntry.CONTENT_TYPE;
            case WIFI_READING_WITH_ID:
                return LocationContract.WifiReadingEntry.CONTENT_ITEM_TYPE;

            default: {
                //Bad URI
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        //todo
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        //todo
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        //todo
        return 0;
    }
}
