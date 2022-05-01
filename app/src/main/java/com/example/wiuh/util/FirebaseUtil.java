package com.example.wiuh.util;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUtil {
    private enum FRef {
        USER, POST, MEMO
    }

    private static final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

    public static DatabaseReference getUserRef() {
        return rootRef.child(FRef.USER.name());
    }
    public static DatabaseReference getPostRef() {return rootRef.child(FRef.POST.name());}
    public static DatabaseReference getMemoRef() {return rootRef.child(FRef.MEMO.name());}

    public static boolean isAvailable() {
        try {
            FirebaseApp.getInstance();
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }
}
