package edu.drake.research.android.lipswithmaps.helper;

import android.os.Build;
import android.util.Log;

import java.text.SimpleDateFormat;

import edu.drake.research.android.lipswithmaps.data.PhoneInfo;

/**
 * Created by Mahesh Gaya on 1/29/17.
 */

public class Utils {
    private static final String TAG = Utils.class.getSimpleName();
    public static String getFriendlyDateTime(long time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss:SSS");
        return simpleDateFormat.format(time);
    }

    public static PhoneInfo getPhoneInformation(){
        PhoneInfo phoneInfo = new PhoneInfo(
                Build.PRODUCT,
                String.valueOf(Build.VERSION.SDK_INT),
                Build.DEVICE,
                Build.MODEL
        );
        Log.d(TAG, "getPhoneInformation: " + phoneInfo.toString());
        return phoneInfo;

    }
}
