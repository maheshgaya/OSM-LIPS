package edu.drake.research.web.lipswithmaps.backend;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.drake.research.lipswithmaps.Accelerometer;
import edu.drake.research.lipswithmaps.Constants;
import edu.drake.research.lipswithmaps.LocationLngLat;
import edu.drake.research.lipswithmaps.Magnetometer;
import edu.drake.research.lipswithmaps.PhoneInfo;
import edu.drake.research.lipswithmaps.Reading;
import edu.drake.research.lipswithmaps.RotationMeter;
import edu.drake.research.lipswithmaps.WifiItem;
import edu.drake.research.web.lipswithmaps.backend.constants.ServerConfig;

/**
 * Created by Mahesh Gaya on 3/25/17.
 */

public class AllReadingServlet extends HttpServlet {
    static Logger Log = Logger.getLogger("AllReadingServlet");
    @Override
    protected void doGet(HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/plain");
        //region Firebase Initialization
        InputStream serviceAccount = this.getClass().getResourceAsStream("/service-account.json");
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
                .setDatabaseUrl(ServerConfig.FIREBASE_DB_URL)
                .build();
        try {
            FirebaseApp.initializeApp(options);
        }
        catch(Exception error){
            Log.info("InitializeApp does not exists");
            error.printStackTrace();
        }
        //endregion

        //region Firebase Query
        // Read only access
        DatabaseReference ref = FirebaseDatabase
                .getInstance()
                .getReference(Reading.TABLE_NAME);
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Was always getting null value when referencing the data directly
                //So decided to do it one by one with hardcoded strings
                //TODO: Clean this up if can
                Log.info("Count " + String.valueOf(dataSnapshot.getChildrenCount()));
                int count = 1;
                for (DataSnapshot readingSnapshot: dataSnapshot.getChildren()) {
                    Log.info("COUNT" + count);
                    count++;
                    //region Location
                    Log.info("LOCATION");
                    LocationLngLat location = new LocationLngLat();
                    if (readingSnapshot.child("location").getChildrenCount() > 0) {
                        location.setAccuracy(Double.parseDouble(readingSnapshot.child("location").child("accuracy").getValue().toString()));
                        location.setLatitude(Double.parseDouble(readingSnapshot.child("location").child("latitude").getValue().toString()));
                        location.setLongitude(Double.parseDouble(readingSnapshot.child("location").child("latitude").getValue().toString()));
                        Log.info(readingSnapshot.child("location").child("accuracy").getValue().toString());
                        Log.info(readingSnapshot.child("location").child("latitude").getValue().toString());
                        Log.info(readingSnapshot.child("location").child("latitude").getValue().toString());
                    }

                    //endregion

                    //region Magnetometer
                    Log.info("MAGNETOMETER");
                    Magnetometer magnetometer = new Magnetometer();
                    if (readingSnapshot.child("magnetometer").getChildrenCount() > 0) {
                        magnetometer.setX(Double.parseDouble(readingSnapshot.child("magnetometer").child("x").getValue().toString()));
                        magnetometer.setY(Double.parseDouble(readingSnapshot.child("magnetometer").child("y").getValue().toString()));
                        magnetometer.setZ(Double.parseDouble(readingSnapshot.child("magnetometer").child("z").getValue().toString()));
                        Log.info(readingSnapshot.child("magnetometer").child("x").getValue().toString());
                        Log.info(readingSnapshot.child("magnetometer").child("y").getValue().toString());
                        Log.info(readingSnapshot.child("magnetometer").child("z").getValue().toString());
                    }
                    //endregion

                    //region RotationMeter
                    Log.info("RotationMeter");
                    RotationMeter rotationMeter = new RotationMeter();
                    if (readingSnapshot.child("rotationmeter").getChildrenCount() > 0) {
                        rotationMeter.setX(Double.parseDouble(readingSnapshot.child("rotationmeter").child("x").getValue().toString()));
                        rotationMeter.setY(Double.parseDouble(readingSnapshot.child("rotationmeter").child("y").getValue().toString()));
                        rotationMeter.setZ(Double.parseDouble(readingSnapshot.child("rotationmeter").child("z").getValue().toString()));
                        Log.info(readingSnapshot.child("rotationmeter").child("x").getValue().toString());
                        Log.info(readingSnapshot.child("rotationmeter").child("y").getValue().toString());
                        Log.info(readingSnapshot.child("rotationmeter").child("z").getValue().toString());
                    }
                    //endregion

                    //region Accelerometer
                    Log.info("ACCELEROMETER");
                    Accelerometer accelerometer = new Accelerometer();
                    if (readingSnapshot.child("accelerometer").getChildrenCount() > 0){
                        accelerometer.setX(Double.parseDouble(readingSnapshot.child("accelerometer").child("x").getValue().toString()));
                        accelerometer.setY(Double.parseDouble(readingSnapshot.child("accelerometer").child("y").getValue().toString()));
                        accelerometer.setZ(Double.parseDouble(readingSnapshot.child("accelerometer").child("z").getValue().toString()));
                        Log.info(readingSnapshot.child("accelerometer").child("x").getValue().toString());
                        Log.info(readingSnapshot.child("accelerometer").child("y").getValue().toString());
                        Log.info(readingSnapshot.child("accelerometer").child("z").getValue().toString());
                    }
                    //endregion

                    //region PhoneInfo
                    Log.info("PHONE_INFO");
                    PhoneInfo phoneInfo = new PhoneInfo();
                    if (readingSnapshot.child("user").getChildrenCount() > 0){
                        phoneInfo.setModel(readingSnapshot.child("user").child("model").getValue().toString());
                        phoneInfo.setProduct(readingSnapshot.child("user").child("product").getValue().toString());
                        phoneInfo.setDevice(readingSnapshot.child("user").child("device").getValue().toString());
                        phoneInfo.setSdklevel(readingSnapshot.child("user").child("sdklevel").getValue().toString());
                    }
                    //endregion

                    //region Wi-Fi List
                    Log.info("WI-FI LIST");
                    List<WifiItem> wifiList = new ArrayList<WifiItem>();
                    if (readingSnapshot.child("wifilist").getChildrenCount() > 0){
                        for (DataSnapshot wifi: readingSnapshot.child("wifilist").getChildren()) {
                            wifiList.add(new WifiItem(
                                    wifi.child("ssid").getValue().toString(),
                                    wifi.child("bssid").getValue().toString(),
                                    Integer.parseInt(wifi.child("level").getValue().toString())
                            ));
                        }
                    }
                    //endregion

                    //region Reading
                    long timestamp = Long.parseLong(readingSnapshot.child("timestamp").getValue().toString());

                    Reading reading = new Reading(timestamp, wifiList, location,
                            accelerometer, magnetometer, rotationMeter, phoneInfo);
                    //endregion

                    convertToJson(reading);

                    //region Write to Web Page
                    try {
                        resp.getWriter().println(reading.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //endregion

                }

                countDownLatch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        try {
            //wait for firebase to query record.
            countDownLatch.await();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        //endregion

    }

    public JSONObject convertToJson(Reading reading){
        return null;
    }
}
