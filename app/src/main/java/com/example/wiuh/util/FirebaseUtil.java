package com.example.wiuh.util;

import com.example.wiuh.model.WifiInformation;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/** FirebaseUtil
 *
 *
 * */
public class FirebaseUtil {
    private static final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

    public static DatabaseReference getPostRef() {
        return rootRef.child("POST")
                .child(WifiInformation.getMAC());
    }
    public static DatabaseReference getMemoRef() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return rootRef.child("MEMO")
                .child(user.getUid())
                .child(WifiInformation.getMAC());
    }

    public static boolean isAvailable() {
        try {
            FirebaseApp.getInstance();
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }
}
