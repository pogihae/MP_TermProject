package com.example.wiuh;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.wiuh.util.FirebaseUtil;
import com.example.wiuh.util.ToastUtil;
import com.github.pwittchen.reactivewifi.ReactiveWifi;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import kotlin.Unit;
import se.warting.permissionsui.backgroundlocation.PermissionsUiContracts;

public class LoginActivity extends AppCompatActivity {
    static final int RC_GOOGLE_SIGN_IN = 123;
    static final String CHANNEL_ID = "WIFI_INFO";

    private FirebaseAuth auth;
    private GoogleSignInClient client;

    private EditText mEtEmail;
    private EditText mEtPwd;

    @SuppressLint("SetTextI18n")
    private final ActivityResultLauncher<Unit> mGetPermission = registerForActivityResult(
            new PermissionsUiContracts.RequestBackgroundLocation(),
            success -> {
                //createNotificationChannel();
                startWifiInfoSubscription();
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        client = GoogleSignIn.getClient(this, gso);

        mEtEmail    = findViewById(R.id.et_userID);
        mEtPwd      = findViewById(R.id.et_password);

        findViewById(R.id.btn_login).setOnClickListener(v -> emailLogin());
        findViewById(R.id.btn_google_login).setOnClickListener(
                v -> startActivityForResult(client.getSignInIntent(), RC_GOOGLE_SIGN_IN)
        );
        findViewById(R.id.btn_signin).setOnClickListener(
                v -> startActivity(new Intent(this, SignUpActivity.class))
        );

        mGetPermission.launch(null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!FirebaseUtil.isAvailable() || !isNetworkAvailable())
            ToastUtil.showText(this, "connection error");
        else if(auth.getCurrentUser() != null)
            startMain();
    }

    private boolean isNetworkAvailable() {
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @SuppressLint("CheckResult")
    private void startWifiInfoSubscription() {
        ReactiveWifi.observeWifiAccessPointChanges(this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(res -> {
                    String ssid = res.getSSID();
                    String mac = res.getBSSID();
                    //notifyContent(ssid + " " + mac);
                });
    }

    /*
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void notifyContent(String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("WIFI INFO")
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, builder.build());
    }
    */

    private void startMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            firebaseAuthWithGoogle(account.getIdToken());
        } catch (ApiException e) {
            ToastUtil.showText(this, "Login failed");
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) startMain();
                    else ToastUtil.showText(this, "Login failed");
                });
    }

    private void emailLogin() {
        String strEmail = mEtEmail.getText().toString();
        String strPwd   = mEtPwd.getText().toString();

        auth.signInWithEmailAndPassword(strEmail,strPwd)
                     .addOnCompleteListener(LoginActivity.this, task -> {
                        if(task.isSuccessful()) startMain();
                        else ToastUtil.showText(this, "Login Failed");
                        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
}