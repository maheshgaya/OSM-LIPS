package edu.drake.research.android.lipswithmaps.helper;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.drake.research.android.lipswithmaps.data.Post;

/**
 * Created by Mahesh Gaya on 3/23/17.
 */

public class DatabaseUtils {
    public static void uploadContent(Post post){
        //TODO: upload content to Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Post.TABLE_NAME);
        databaseReference.push().setValue(post);

    }
}
