package com.example.wiuh.util;

import android.content.Context;
import android.content.Intent;

import com.example.wiuh.WifiInfo;
import com.example.wiuh.activity.sign.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * FirebaseUtil
 * <p>
 * Real time DB 접근
 * FirebaseUser 접근
 */
public class FirebaseUtil {
    private static final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

    private static ValueEventListener postListener;
    private static ValueEventListener memoListener;

    public static void setListener(ValueEventListener postListener, ValueEventListener memoListener) {
        if (FirebaseUtil.postListener != null)
            FirebaseUtil.getPostRef()
                    .removeEventListener(FirebaseUtil.postListener);
        if (FirebaseUtil.memoListener != null)
            FirebaseUtil.getMemoRef()
                    .removeEventListener(FirebaseUtil.memoListener);

        FirebaseUtil.postListener = postListener;
        FirebaseUtil.memoListener = memoListener;

        getPostRef().addValueEventListener(postListener);
        getMemoRef().addValueEventListener(memoListener);
    }

    public static DatabaseReference getPostRef() {
        return rootRef.child("POST")
                .child(WifiInfo.getMAC());
    }

    public static DatabaseReference getUserRef() {
        return rootRef.child("POST");
    }

    public static DatabaseReference getMemoRef() {
        return rootRef.child("MEMO")
                .child(getCurUser().getUid())
                .child(WifiInfo.getMAC());
    }

    public static DatabaseReference getWifiRef() {
        return rootRef.child("WIFI")
                .child(getCurUser().getUid());
    }

    public static FirebaseUser getCurUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static void logout(Context context) {
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(context, LoginActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);
    }
}
