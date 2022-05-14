package com.example.wiuh.util;

import android.content.Context;
import android.content.Intent;

import com.example.wiuh.activities.LoginActivity;
import com.example.wiuh.model.WifiState;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * FirebaseUtil
 * <p>
 * Real time DB 접근
 * FirebaseUser 접근
 */
public class FirebaseUtil {
    private static final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

    public static DatabaseReference getPostRef() {
        return rootRef.child("POST")
                .child(WifiState.getMAC());
    }

    public static DatabaseReference getMemoRef() {
        return rootRef.child("MEMO")
                .child(getCurUser().getUid())
                .child(WifiState.getMAC());
    }

    public static FirebaseUser getCurUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static void logout(Context context) {
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);
    }
}
