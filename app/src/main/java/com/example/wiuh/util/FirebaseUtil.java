package com.example.wiuh.util;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUtil {
    private enum FRef {
        USER
    }

    private static final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

    public static DatabaseReference getUserRef() {
        return rootRef.child(FRef.USER.name());
    }
}
