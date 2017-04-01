package edu.drake.research.web.lipswithmaps.backend;

import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.appengine.repackaged.com.google.gson.GsonBuilder;
import com.google.appengine.repackaged.com.google.gson.JsonObject;
import com.google.common.reflect.TypeToken;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.drake.research.lipswithmaps.Accelerometer;
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
    private static Logger Log = Logger.getLogger("AllReadingServlet");
    public static final String CSV_TYPE = "csv";
    public static final String JSON_TYPE = "json";
    public static final String FORMAT = "format";
    @Override
    protected void doGet(HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        String parameter = JSON_TYPE;
        if (req.getParameterMap().containsKey(FORMAT)) {
            parameter = req.getParameter(FORMAT);
        }
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
        final DatabaseReference ref = FirebaseDatabase
                .getInstance()
                .getReference(Reading.TABLE_NAME);
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final String formatParameter = parameter;
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Was always getting null value when referencing the data directly
                //So decided to do it one by one with hardcoded strings
                //TODO: Clean this up if can
                int childrenCount = (int) dataSnapshot.getChildrenCount();
                int count = 1;
                if (childrenCount == 0) {
                    try {
                        String message = "{ \"error\": \"No data in the database\"}";
                        resp.getWriter().println(message);
                        Log.info(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Map<String, String> uniqueWifisMap =  new LinkedHashMap<>();
                List<Reading> readingList = new ArrayList<>();
                int wifiCount = 1;

                Log.info("Count: " + dataSnapshot.getChildrenCount());

                for (DataSnapshot readingSnapshot: dataSnapshot.getChildren()) {
                    //region Location
                    LocationLngLat location = new LocationLngLat();
                    if (readingSnapshot.child("location").getChildrenCount() > 0) {
                        location.setAccuracy(Double.parseDouble(readingSnapshot.child("location").child("accuracy").getValue().toString()));
                        location.setLatitude(Double.parseDouble(readingSnapshot.child("location").child("latitude").getValue().toString()));
                        location.setLongitude(Double.parseDouble(readingSnapshot.child("location").child("latitude").getValue().toString()));
                    }

                    //endregion

                    //region Magnetometer
                    Magnetometer magnetometer = new Magnetometer();
                    if (readingSnapshot.child("magnetometer").getChildrenCount() > 0) {
                        magnetometer.setX(Double.parseDouble(readingSnapshot.child("magnetometer").child("x").getValue().toString()));
                        magnetometer.setY(Double.parseDouble(readingSnapshot.child("magnetometer").child("y").getValue().toString()));
                        magnetometer.setZ(Double.parseDouble(readingSnapshot.child("magnetometer").child("z").getValue().toString()));
                    }
                    //endregion

                    //region RotationMeter
                    RotationMeter rotationMeter = new RotationMeter();
                    if (readingSnapshot.child("rotationmeter").getChildrenCount() > 0) {
                        rotationMeter.setX(Double.parseDouble(readingSnapshot.child("rotationmeter").child("x").getValue().toString()));
                        rotationMeter.setY(Double.parseDouble(readingSnapshot.child("rotationmeter").child("y").getValue().toString()));
                        rotationMeter.setZ(Double.parseDouble(readingSnapshot.child("rotationmeter").child("z").getValue().toString()));
                    }
                    //endregion

                    //region Accelerometer
                    Accelerometer accelerometer = new Accelerometer();
                    if (readingSnapshot.child("accelerometer").getChildrenCount() > 0){
                        accelerometer.setX(Double.parseDouble(readingSnapshot.child("accelerometer").child("x").getValue().toString()));
                        accelerometer.setY(Double.parseDouble(readingSnapshot.child("accelerometer").child("y").getValue().toString()));
                        accelerometer.setZ(Double.parseDouble(readingSnapshot.child("accelerometer").child("z").getValue().toString()));
                    }
                    //endregion

                    //region PhoneInfo
                    PhoneInfo phoneInfo = new PhoneInfo();
                    if (readingSnapshot.child("user").getChildrenCount() > 0){
                        phoneInfo.setModel(readingSnapshot.child("user").child("model").getValue().toString());
                        phoneInfo.setProduct(readingSnapshot.child("user").child("product").getValue().toString());
                        phoneInfo.setDevice(readingSnapshot.child("user").child("device").getValue().toString());
                        phoneInfo.setSdklevel(readingSnapshot.child("user").child("sdklevel").getValue().toString());
                    }
                    //endregion

                    //map of unique bssid with BSSID<count>
                    //region Wi-Fi List
                    List<WifiItem> wifiList = new ArrayList<>();
                    if (readingSnapshot.child("wifilist").getChildrenCount() > 0){
                        for (DataSnapshot wifi: readingSnapshot.child("wifilist").getChildren()) {
                            WifiItem wifiItem = new WifiItem(wifi.child("ssid").getValue().toString(),
                                    wifi.child("bssid").getValue().toString(),
                                    Integer.parseInt(wifi.child("level").getValue().toString()));
                            wifiList.add(wifiItem);

                            //execute this only if format is CSV type and is not already contained in the hashmap
                            if (formatParameter.equals(CSV_TYPE) &&
                                    !uniqueWifisMap.containsKey(wifiItem.getBssid())) {
                                String uniqueBssid = "BSSID" + wifiCount;
                                uniqueWifisMap.put(wifiItem.getBssid(), uniqueBssid);
                                Log.info(uniqueWifisMap.get(wifiItem.getBssid()) + " -- " + wifiItem.getBssid());
                                wifiCount++;
                            }
                        }
                    }
                    //endregion

                    //region Reading
                    long timestamp = Long.parseLong(readingSnapshot.child("timestamp").getValue().toString());

                    Reading reading = new Reading(timestamp, wifiList, location,
                            accelerometer, magnetometer, rotationMeter, phoneInfo);
                    //endregion

                    readingList.add(reading);

                    count++;
                }


                if (formatParameter.equals(CSV_TYPE)){
                    try {
                        resp.setHeader("Accept", "text/csv");
                        resp.setHeader("Content-type", "text/csv");
                        resp.setHeader("Content-Disposition","inline; filename=all_readings.csv");
                        resp.getWriter().print(csvHeader(uniqueWifisMap));
                        resp.getWriter().print(convertToCSV(readingList, uniqueWifisMap));
                    } catch (IOException e){
                        e.printStackTrace();
                    }

                } else{
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    //region Write to Web Page
                    try {
                        resp.setHeader("Accept", "application/json");
                        resp.setHeader("Content-type", "application/json");
                        resp.setHeader("Content-Disposition","inline; filename=all_readings.json");
                        String json = "{ \"readings\" :" + gson.toJson(readingList) + "}";
                        resp.getWriter().print(json);
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

    /**
     * Loop through the reading list
     * Output the reading values accordingly
     * TODO: check the position of the wifi bssid, else set to 0.0
     * @param readingList
     * @return
     */
    public String convertToCSV(List<Reading> readingList, Map<String, String> uniqueReadingMap){
        //if error occurred return empty string
        if (uniqueReadingMap == null) return "";
        if (readingList == null) return "";

        //build the output
        StringBuilder output = new StringBuilder();
        for (Reading reading: readingList) {
            List<WifiItem> wifiList = reading.getWifilist();
            //compare it with the unique wifi hash map
            //check if the wifi is there, if not set to 0.0
            Integer[] wifiLevelArray = new Integer[uniqueReadingMap.size()];

            //set everything to zero
            for (int i = 0; i < wifiLevelArray.length; i++){
                wifiLevelArray[i] = 0;
            }

            for (WifiItem wifi : wifiList) {
                String bssid = uniqueReadingMap.get(wifi.getBssid());
                int count = Integer.parseInt(bssid.substring(5, bssid.length()));
                //Since we start with 1 for the BSSID<count>, we need to remove 1
                wifiLevelArray[count - 1] = wifi.getLevel();
                Log.info("bssid [" + count + "] " + wifi.getLevel());

            }
            //build each row
            StringBuilder levelBuilder = new StringBuilder();
            for (Integer level: wifiLevelArray){
                String levelStr = level + ", ";
                levelBuilder.append(levelStr);
            }
            String out =
                    //user's device information
                    reading.getUser().getDevice() + ", " + reading.getUser().getModel() + ", " +
                        reading.getUser().getProduct() + ", " + reading.getUser().getSdklevel() + ", " +

                    //accelerometer
                    reading.getAccelerometer().getX() + ", " + reading.getAccelerometer().getY() + ", " +
                        reading.getAccelerometer().getZ() + ", " +

                    //magnetometer
                    reading.getMagnetometer().getX() + ", " +
                            reading.getMagnetometer().getY() + ", " + reading.getMagnetometer().getZ() + ", " +
                    reading.getRotationmeter().getX() + ", " +
                            reading.getRotationmeter().getY() + ", " + reading.getRotationmeter().getZ() + ", " +

                    //wifis
                    levelBuilder.toString() +
                            
                    //location
                    reading.getLocation().getLongitude() + ", " + reading.getLocation().getLatitude() + ", " +
                        reading.getLocation().getAccuracy() + "\r\n";
            output.append(out);

        }
        return output.toString();
    }

    public String csvHeader(Map<String, String> uniqueWifisMap){
        StringBuilder uniqueWifis = new StringBuilder();
        for (String wifi: uniqueWifisMap.keySet()){
            String bssid = uniqueWifisMap.get(wifi) + ", ";
            uniqueWifis.append(bssid);
        }
        String retStr =  "user_device, user_model, user_product, user_sdklevel, " +
                "acceleration_x, acceleration_y, acceleration_z, " +
                "magnetic_x, magnetic_y, magnetic_z, " +
                "rotation_x, rotation_y, rotation_z, " +
                uniqueWifis.toString() +
                "location_longitude, location_latitude, location_accuracy\r\n";
        Log.info(retStr);
        return  retStr;

    }
}
