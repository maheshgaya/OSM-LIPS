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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.drake.research.lipswithmaps.Reading;
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
                Log.info("Count" + String.valueOf(dataSnapshot.getChildrenCount()));
                for (DataSnapshot reading: dataSnapshot.getChildren()){

                    try {
                        resp.getWriter().print(reading.getValue().toString() + ",");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

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
}
