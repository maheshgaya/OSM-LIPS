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
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
        Cursor retCursor;
        switch (sUriMatcher.match(uri)){
            case READING:
                retCursor = mLocationDbHelper.getReadableDatabase().query(
                        LocationContract.ReadingEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case READING_WITH_ID:
                retCursor = mLocationDbHelper.getReadableDatabase().query(
                        LocationContract.ReadingEntry.TABLE_NAME,
                        projection,
                        LocationContract.ReadingEntry._ID + " = ? ",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder
                );
                break;
            case WIFI:
                retCursor = mLocationDbHelper.getReadableDatabase().query(
                        LocationContract.WifiEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case WIFI_WITH_ID:
                retCursor = mLocationDbHelper.getReadableDatabase().query(
                        LocationContract.WifiEntry.TABLE_NAME,
                        projection,
                        LocationContract.WifiEntry._ID + " = ? ",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder
                );
                break;
            case PHONE:
                retCursor = mLocationDbHelper.getReadableDatabase().query(
                        LocationContract.PhoneEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case PHONE_WITH_ID:
                retCursor = mLocationDbHelper.getReadableDatabase().query(
                        LocationContract.PhoneEntry.TABLE_NAME,
                        projection,
                        LocationContract.PhoneEntry._ID + " = ? ",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder
                );
                break;
            case PHONE_READING:
                retCursor = mLocationDbHelper.getReadableDatabase().query(
                        LocationContract.PhoneReadingEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case PHONE_READING_WITH_ID:
                retCursor = mLocationDbHelper.getReadableDatabase().query(
                        LocationContract.PhoneReadingEntry.TABLE_NAME,
                        projection,
                        LocationContract.PhoneReadingEntry._ID + " = ? ",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder
                );
                break;
            case WIFI_READING:
                retCursor = mLocationDbHelper.getReadableDatabase().query(
                        LocationContract.WifiReadingEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case WIFI_READING_WITH_ID:
                retCursor = mLocationDbHelper.getReadableDatabase().query(
                        LocationContract.WifiReadingEntry.TABLE_NAME,
                        projection,
                        LocationContract.WifiReadingEntry._ID + " = ? ",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder
                );
                break;
            default:{
                // Bad URI
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;

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
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = mLocationDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match){
            case READING: {
                long _id = db.insert(LocationContract.ReadingEntry.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = LocationContract.ReadingEntry.buildReadingUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case WIFI: {
                long _id = db.insert(LocationContract.WifiEntry.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = LocationContract.WifiEntry.buildWifiUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case PHONE: {
                long _id = db.insert(LocationContract.PhoneEntry.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = LocationContract.PhoneEntry.buildPhoneUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case PHONE_READING: {
                long _id = db.insert(LocationContract.PhoneReadingEntry.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = LocationContract.PhoneReadingEntry.buildPhoneReadingUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case WIFI_READING: {
                long _id = db.insert(LocationContract.WifiReadingEntry.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = LocationContract.WifiReadingEntry.buildWifiReadingUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mLocationDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";

        switch (match){
            case READING: {
                rowsDeleted = db.delete(
                        LocationContract.ReadingEntry.TABLE_NAME, selection, selectionArgs
                );
                break;
            }
            case WIFI: {
                rowsDeleted = db.delete(
                        LocationContract.WifiEntry.TABLE_NAME, selection, selectionArgs
                );
                break;
            }
            case PHONE: {
                rowsDeleted = db.delete(
                        LocationContract.PhoneEntry.TABLE_NAME, selection, selectionArgs
                );
                break;
            }
            case PHONE_READING: {
                rowsDeleted = db.delete(
                        LocationContract.PhoneReadingEntry.TABLE_NAME, selection, selectionArgs
                );
                break;
            }
            case WIFI_READING: {
                rowsDeleted = db.delete(
                        LocationContract.WifiReadingEntry.TABLE_NAME, selection, selectionArgs
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mLocationDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";

        switch (match){
            case READING: {
                rowsUpdated = db.update(
                        LocationContract.ReadingEntry.TABLE_NAME, contentValues, selection, selectionArgs
                );
                break;
            }
            case WIFI: {
                rowsUpdated = db.update(
                        LocationContract.WifiEntry.TABLE_NAME, contentValues, selection, selectionArgs
                );
                break;
            }
            case PHONE: {
                rowsUpdated = db.update(
                        LocationContract.PhoneEntry.TABLE_NAME, contentValues, selection, selectionArgs
                );
                break;
            }
            case PHONE_READING: {
                rowsUpdated = db.update(
                        LocationContract.PhoneReadingEntry.TABLE_NAME, contentValues, selection, selectionArgs
                );
                break;
            }
            case WIFI_READING: {
                rowsUpdated = db.update(
                        LocationContract.WifiReadingEntry.TABLE_NAME, contentValues, selection, selectionArgs
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
