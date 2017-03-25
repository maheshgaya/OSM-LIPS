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

package edu.drake.research.android.lipswithmaps.helper;

import android.os.Build;
import android.util.Log;

import java.text.SimpleDateFormat;

import edu.drake.research.lipswithmaps.PhoneInfo;

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
