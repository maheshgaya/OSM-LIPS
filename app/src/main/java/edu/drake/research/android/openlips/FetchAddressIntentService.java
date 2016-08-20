package edu.drake.research.android.openlips;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Mahesh Gaya on 8/7/16.
 */
public class FetchAddressIntentService extends IntentService {
    public static final String TAG = "FetchLocationAddressIS";

    protected ResultReceiver mReceiver;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public FetchAddressIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle parameters = intent.getExtras();
        mReceiver = parameters.getParcelable(Constants.RECEIVER);
        String errorMessage = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        //Get location passed to this service through an extra
        Location location = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);
        List<Address> addresses = null;
        try {
        addresses = geocoder.getFromLocation(
                location.getLatitude(),
                location.getLongitude(),
                1);
            } catch (IOException ioException) {
                // Catch network or other I/O problems.
                errorMessage = getString(R.string.service_not_available);
                Log.d(TAG, "onHandleIntent: " + errorMessage, ioException);
            } catch (IllegalArgumentException illegalArgumentException){
                // Catch invalid latitude or longitude values.
                errorMessage = getString(R.string.invalid_lat_long_used);
                Log.d(TAG, "onHandleIntent: " + errorMessage + ". " +
                        "Latitude = " + location.getLatitude() +
                        ", Longitude = " +
                        location.getLongitude(), illegalArgumentException);
            }
        // Handle case where no address was found.
        if (addresses == null || addresses.size() == 0){
            if (errorMessage.isEmpty()){
                errorMessage = getString(R.string.no_address_found);
                Log.d(TAG, "onHandleIntent: " + errorMessage);

            }
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
        } else {
            Address address = addresses.get(0);
            ArrayList<String> mAddressFragments = new ArrayList<String>();

            //Fetch Address using getAddressLine
            for (int i = 0; i < address.getMaxAddressLineIndex(); i++){
                mAddressFragments.add(address.getAddressLine(i));
            }
            Log.d(TAG, "onHandleIntent: " + getString(R.string.address_found));
            deliverResultToReceiver(Constants.SUCCESS_RESULT, TextUtils.join(System.getProperty("line.separator"), mAddressFragments));
        }



    }

    private void deliverResultToReceiver(int resultCode, String message) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.RESULT_DATA_KEY, message);
            Log.d(TAG, "deliverResultToReceiver: " + bundle);
            Log.d(TAG, "deliverResultToReceiver: " + resultCode);
            mReceiver.send(resultCode, bundle);
        }catch (Exception e){
            Log.d(TAG, "deliverResultToReceiver: ", e);
        }

    }


}
