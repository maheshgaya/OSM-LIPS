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
                Log.info("Count " + String.valueOf(dataSnapshot.getChildrenCount()));
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

                for (DataSnapshot readingSnapshot: dataSnapshot.getChildren()) {
                    Log.info("COUNT" + count);
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

                    String output = "";
                    if (formatParameter.equals(CSV_TYPE)){
                        output = convertToCSV(reading);
                        try {
                            resp.setHeader("Accept", "text/csv");
                            resp.setHeader("Content-type", "text/csv");
                            if (count == 1){
                                resp.getWriter().print(csvHeader());
                            }
                            resp.getWriter().print(output);
                        } catch (IOException e){
                            e.printStackTrace();
                        }
                    } else {
                        output = convertToJson(reading);
                        resp.setHeader("Accept", "application/json");
                        resp.setHeader("Content-type", "application/json");

                        //region Write to Web Page
                        try {
                            if (count == 1){
                                resp.getWriter().println("{\n \"reading\" : [");
                            }

                            if (count == childrenCount) {
                                resp.getWriter().println(output + "\n]\n}");
                            } else if (count != 1){
                                resp.getWriter().println(output + ",");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //endregion
                    }
                    count++;
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

    public String convertToJson(Reading reading){
        StringBuilder output = new StringBuilder();
        List<WifiItem> wifiList = reading.getWifilist();

        String newline = "\n";
        String tab = "\t";

        int count = 0;
        for (WifiItem wifi: wifiList) {
            output.append("{\n");

            //User
            if (reading.getUser() != null){
                String device = "\"user_device\":\"" + reading.getUser().getDevice()+ "\",\n";
                String model = "\"user_model\":\"" + reading.getUser().getModel()+ "\",\n";
                String product = "\"user_product\":\"" + reading.getUser().getProduct()+ "\",\n";
                String sdklevel = "\"user_sdklevel\":\"" + reading.getUser().getSdklevel()+ "\",\n";
                String out = device + model + product + sdklevel;
                output.append(out);
            }

            //magnetic
            if (reading.getAccelerometer() != null) {
                String x = "\"acceleration_x\":\"" + reading.getAccelerometer().getX() + "\",\n";
                String y = "\"acceleration_y\":\"" + reading.getAccelerometer().getX() + "\",\n";
                String z = "\"acceleration_z\":\"" + reading.getAccelerometer().getX() + "\",\n";
                String out = x + y + z;
                output.append(out);
            }

            //magnetic
            if (reading.getMagnetometer() != null) {
                String x = "\"magnetic_x\":\"" + reading.getMagnetometer().getX() + "\",\n";
                String y = "\"magnetic_y\":\"" + reading.getMagnetometer().getX() + "\",\n";
                String z = "\"magnetic_z\":\"" + reading.getMagnetometer().getX() + "\",\n";
                String out = x + y + z;
                output.append(out);

            }

            //rotation
            if (reading.getRotationmeter() != null) {
                String x = "\"rotation_x\":\"" + reading.getRotationmeter().getX() + "\",\n";
                String y = "\"rotation_y\":\"" + reading.getRotationmeter().getX() + "\",\n";
                String z = "\"rotation_z\":\"" + reading.getRotationmeter().getX() + "\",\n";
                String out = x + y + z;
                output.append(out);
            }

            //wifi
            if (wifi != null){
                String bssid = "\"wifi_bssid\":\"" + wifi.getBssid() + "\",\n";
                String ssid = "\"wifi_ssid\":\"" + wifi.getSsid() + "\",\n";
                String level = "\"wifi_level\":\"" + wifi.getLevel() + "\",\n";
                String out = bssid + ssid + level;
                output.append(out);
            }

            //location
            if (reading.getLocation() != null) {
                String longitude = "\"location_longitude\":\"" + reading.getLocation().getLongitude() + "\",\n";
                String latitude = "\"location_latitude\":\"" + reading.getLocation().getLatitude() + "\",\n";
                String accuracy = "\"location_accuracy\":\"" + reading.getLocation().getAccuracy() + "\"\n";
                String out = longitude + latitude + accuracy;
                output.append(out);
            }

            if (count == wifiList.size() - 1) {
                output.append("}\n");
            } else {
                output.append("},\n");
            }
            count++;

        }
        return output.toString();
    }

    public String convertToCSV(Reading reading){
        StringBuilder output = new StringBuilder();
        List<WifiItem> wifiList = reading.getWifilist();
        for (WifiItem wifi: wifiList) {
            output.append(reading.getUser().getDevice() + ", " + reading.getUser().getModel() + ", " +
                    reading.getUser().getProduct() + ", " + reading.getUser().getSdklevel() + ", " +
                    reading.getAccelerometer().getX() + ", " + reading.getAccelerometer().getY() + ", " +
                    reading.getAccelerometer().getZ() + ", " + reading.getMagnetometer().getX() + ", " +
                    reading.getMagnetometer().getY() + ", " + reading.getMagnetometer().getZ() + ", " +
                    reading.getRotationmeter().getX() + ", " + reading.getRotationmeter().getZ() + ", " +
                    wifi.getBssid() + ", " + wifi.getSsid() + ", " + wifi.getLevel() + ", " +
                    reading.getLocation().getLongitude() + ", " + reading.getLocation().getLatitude() + ", " +
                    reading.getLocation().getAccuracy() + "\r\n"
            );
        }
        return output.toString();
    }

    public String csvHeader(){
        return "user_device, user_model, user_product, user_sdklevel, " +
                "acceleration_x, acceleration_y, acceleration_z, " +
                "magnetic_x, magnetic_y, magnetic_z, " +
                "rotation_x, rotation_y, rotation_z, " +
                "wifi_bssid, wifi_ssid, wifi_level, " +
                "location_longitude, location_latitude, location_accuracy\r\n";

    }
}
