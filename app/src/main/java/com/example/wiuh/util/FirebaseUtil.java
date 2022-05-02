package com.example.wiuh.util;

import com.example.wiuh.model.WifiInformation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/** FirebaseUtil
 *
 *
 * */
public class FirebaseUtil {
    private static final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private static FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();

    public static DatabaseReference getPostRef() {
        return rootRef.child("POST")
                .child(WifiInformation.getMAC());
    }
    public static DatabaseReference getMemoRef() {
        return rootRef.child("MEMO")
                .child(curUser.getUid())
                .child(WifiInformation.getMAC());
    }

    public static String getCurUserUid() {
        return curUser.getUid();
    }
    public static String getCurUserNickname() {
        return curUser.getDisplayName();
    }
    public static void updateCurUserNickname(String nickname) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(nickname)
                .build();

        curUser.updateProfile(profileUpdates);
    }
    public static void logout() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();
        curUser = auth.getCurrentUser();
    }
}
