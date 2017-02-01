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
        final String SQL_CREATE_WIFI_TABLE =
                "CREATE TABLE " + LocationContract.WifiEntry.TABLE_NAME ; //TODO
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
