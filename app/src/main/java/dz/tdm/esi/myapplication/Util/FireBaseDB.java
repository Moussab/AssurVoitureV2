package dz.tdm.esi.myapplication.Util;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Amine on 26/05/2017.
 */

public class FireBaseDB {

    private static FirebaseDatabase mDatabase;
    private static DatabaseReference myRef;
    private static StorageReference storageRef;

    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
            myRef = mDatabase.getReference();
        }
        return mDatabase;
    }

    public static DatabaseReference getMyRef() {
        if (myRef == null) {
            getDatabase();
        }
        return myRef;
    }

    public static StorageReference getMyStorageRef() {
        if (storageRef == null) {
            storageRef = FirebaseStorage.getInstance().getReference();
        }
        return storageRef;
    }

}
