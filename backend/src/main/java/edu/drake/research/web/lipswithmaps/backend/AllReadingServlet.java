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

    /**
     * According to format requested output JSON or CSV
     * Default is JSON
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        String parameter = JSON_TYPE;
        if (req.getParameterMap().containsKey(FORMAT)) {
            //get the output format (CSV or JSON)
            parameter = req.getParameter(FORMAT);
        }
        //region Firebase Initialization
        //Service account is found in the main/resources folder
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
        //Firebase queries from a background thread,
        // thus we need to await for the background thread to complete
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        // Read only access service account
        final DatabaseReference ref = FirebaseDatabase
                .getInstance()
                .getReference(Reading.TABLE_NAME);

        final String formatParameter = parameter;

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Output was always getting null value when referencing the data directly
                //So decided to do it one by one with hardcoded key strings
                //TODO: change the key string if the names of the variables are updated for the data

                int childrenCount = (int) dataSnapshot.getChildrenCount();

                //return error if there is no content
                if (childrenCount == 0) {
                    try {
                        String message = "{ \"error\": \"No data in the database\"}";
                        resp.getWriter().println(message);
                        Log.info(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                //keep a chronologically ordered Hash Map for the unique wifi bssid
                //will be used for CSV Output, however, keep the structure for JSON
                Map<String, String> uniqueWifisMap =  new LinkedHashMap<>();
                //create a list of the readings from the Firebase Query
                List<Reading> readingList = new ArrayList<>();
                //To be used for the uniqueWifiMaps count, the format is "BSSID<count>", i.e. BSSID243
                int wifiCount = 1;

                Log.info("Count: " + dataSnapshot.getChildrenCount()); //debug

                //Loop through the datasnapshot list and get all the data
                for (DataSnapshot readingSnapshot: dataSnapshot.getChildren()) {
                    //region Location
                    //get the location data
                    LocationLngLat location = new LocationLngLat();
                    if (readingSnapshot.child("location").getChildrenCount() > 0) {
                        location.setAccuracy(Double.parseDouble(readingSnapshot.child("location").child("accuracy").getValue().toString()));
                        location.setLatitude(Double.parseDouble(readingSnapshot.child("location").child("latitude").getValue().toString()));
                        location.setLongitude(Double.parseDouble(readingSnapshot.child("location").child("longitude").getValue().toString()));
                    }

                    //endregion

                    //region Magnetometer
                    //get the magnetometer data
                    Magnetometer magnetometer = new Magnetometer();
                    if (readingSnapshot.child("magnetometer").getChildrenCount() > 0) {
                        magnetometer.setX(Double.parseDouble(readingSnapshot.child("magnetometer").child("x").getValue().toString()));
                        magnetometer.setY(Double.parseDouble(readingSnapshot.child("magnetometer").child("y").getValue().toString()));
                        magnetometer.setZ(Double.parseDouble(readingSnapshot.child("magnetometer").child("z").getValue().toString()));
                    }
                    //endregion

                    //region RotationMeter
                    //get the rotation meter data
                    RotationMeter rotationMeter = new RotationMeter();
                    if (readingSnapshot.child("rotationmeter").getChildrenCount() > 0) {
                        rotationMeter.setX(Double.parseDouble(readingSnapshot.child("rotationmeter").child("x").getValue().toString()));
                        rotationMeter.setY(Double.parseDouble(readingSnapshot.child("rotationmeter").child("y").getValue().toString()));
                        rotationMeter.setZ(Double.parseDouble(readingSnapshot.child("rotationmeter").child("z").getValue().toString()));
                    }
                    //endregion

                    //region Accelerometer
                    //get the accelerometer data
                    Accelerometer accelerometer = new Accelerometer();
                    if (readingSnapshot.child("accelerometer").getChildrenCount() > 0){
                        accelerometer.setX(Double.parseDouble(readingSnapshot.child("accelerometer").child("x").getValue().toString()));
                        accelerometer.setY(Double.parseDouble(readingSnapshot.child("accelerometer").child("y").getValue().toString()));
                        accelerometer.setZ(Double.parseDouble(readingSnapshot.child("accelerometer").child("z").getValue().toString()));
                    }
                    //endregion

                    //region PhoneInfo
                    //Get the user's device information
                    PhoneInfo phoneInfo = new PhoneInfo();
                    if (readingSnapshot.child("user").getChildrenCount() > 0){
                        phoneInfo.setModel(readingSnapshot.child("user").child("model").getValue().toString());
                        phoneInfo.setProduct(readingSnapshot.child("user").child("product").getValue().toString());
                        phoneInfo.setDevice(readingSnapshot.child("user").child("device").getValue().toString());
                        phoneInfo.setSdklevel(readingSnapshot.child("user").child("sdklevel").getValue().toString());
                    }
                    //endregion

                    //region Wi-Fi List
                    //map of unique bssid with "BSSID<count>"
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
                                //add the Bssid as key and an abstract BSSID<count> as value
                                String uniqueBssid = "BSSID" + wifiCount;
                                uniqueWifisMap.put(wifiItem.getBssid(), uniqueBssid);
                                //increment count for the next unique hash
                                wifiCount++;
                            }
                        }
                    }
                    //endregion

                    //region Timestamp
                    //get the timestamp
                    long timestamp = Long.parseLong(readingSnapshot.child("timestamp").getValue().toString());

                    Reading reading = new Reading(timestamp, wifiList, location,
                            accelerometer, magnetometer, rotationMeter, phoneInfo);
                    //endregion

                    //add to the list of the Reading class
                    readingList.add(reading);
                }


                //region Write to Web Page
                if (formatParameter.equals(CSV_TYPE)){
                    //if format is CSV
                    try {
                        //set web page header
                        resp.setHeader("Accept", "text/csv");
                        resp.setHeader("Content-type", "text/csv");
                        resp.setHeader("Content-Disposition","inline; filename=all_readings.csv");
                        //output csv header
                        resp.getWriter().print(csvHeader(uniqueWifisMap));
                        //output all the rows in csv
                        resp.getWriter().print(convertToCSV(readingList, uniqueWifisMap));
                    } catch (IOException e){
                        e.printStackTrace();
                    }

                } else{
                    //default format is JSON
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    try {
                        //set web page header
                        resp.setHeader("Accept", "application/json");
                        resp.setHeader("Content-type", "application/json");
                        resp.setHeader("Content-Disposition","inline; filename=all_readings.json");
                        //out the data in JSON
                        resp.getWriter().print("{ \"readings\" :" + gson.toJson(readingList) + "}");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //endregion

                //background thread job is down so release the latch
                countDownLatch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        try {
            //waits for firebase to query record.
            countDownLatch.await();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        //endregion

    }

    /**
     * Loop through the reading list
     * Output the reading values accordingly
     * Check the position of the wifi bssid, else set to 0.0
     * @param readingList
     * @return
     */
    public String convertToCSV(List<Reading> readingList, Map<String, String> uniqueReadingMap){
        //if error occurred return no data error string
        if (uniqueReadingMap == null || readingList == null) return "error: no data";

        //build the output
        StringBuilder output = new StringBuilder();
        //loop through each reading, build the row and return the whole thing as string
        for (Reading reading: readingList) {
            //compare it with the unique wifi hash map
            //check if the wifi is there, if not set to 0
            Integer[] wifiLevelArray = new Integer[uniqueReadingMap.size()];
            //set everything to zero first
            for (int i = 0; i < wifiLevelArray.length; i++){
                wifiLevelArray[i] = 0;
            }

            //we need to set the level values of the wifis
            for (WifiItem wifi : reading.getWifilist()) {
                String bssid = uniqueReadingMap.get(wifi.getBssid());
                int count = Integer.parseInt(bssid.substring(5, bssid.length()));
                //Since we start with 1 for the BSSID<count>, we need to remove 1
                wifiLevelArray[count - 1] = wifi.getLevel();
            }
            //build the wifi level row first
            StringBuilder levelBuilder = new StringBuilder();
            for (Integer level: wifiLevelArray){
                String levelStr = level + ", ";
                levelBuilder.append(levelStr);
            }

            //build each row completely
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
            //append the row to the output StringBuilder
            output.append(out);
        }

        return output.toString();
    }

    /**
     * Returns the csv header with all the unique BSSIDs
     * @param uniqueWifisMap
     * @return
     */
    public String csvHeader(Map<String, String> uniqueWifisMap){
        StringBuilder uniqueWifis = new StringBuilder();
        for (String wifi: uniqueWifisMap.keySet()){
            String bssid = uniqueWifisMap.get(wifi) + ", ";
            uniqueWifis.append(bssid);
        }
        return "user_device, user_model, user_product, user_sdklevel, " +
                "acceleration_x, acceleration_y, acceleration_z, " +
                "magnetic_x, magnetic_y, magnetic_z, " +
                "rotation_x, rotation_y, rotation_z, " +
                uniqueWifis.toString() +
                "location_longitude, location_latitude, location_accuracy\r\n";


    }
}
