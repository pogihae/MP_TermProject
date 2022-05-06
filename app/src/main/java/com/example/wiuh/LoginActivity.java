package com.example.wiuh;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wiuh.model.WifiState;
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

import java.util.Objects;

import io.reactivex.schedulers.Schedulers;
import se.warting.permissionsui.backgroundlocation.PermissionsUiContracts;

/**
 * LoginActivity
 *
 * 시작
 * 권한, 로그인 확인
 *
 * todo: Splash Activity 추가 후 권한, 네트워크 확인 등을 넘기기
 *
 * */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG         = LoginActivity.class.getSimpleName();
    private static final String CHANNEL_ID  = "WIFI_INFO";

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //permission & wifi info observation
        registerForActivityResult(
                new PermissionsUiContracts.RequestBackgroundLocation(),
                success -> startWifiInfoSubscription()
        ).launch(null);

        //start observation wifi
        startWifiInfoSubscription();

        //already login
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null) startMain();

        findViewById(R.id.btn_login).setOnClickListener(v->emailLogin());
        findViewById(R.id.btn_google_login).setOnClickListener(v->googleLogin());
        findViewById(R.id.btn_signup).setOnClickListener(v->startSignUp());
    }

    private void startWifiInfoSubscription() {
        ReactiveWifi.observeWifiAccessPointChanges(getApplicationContext())
                .subscribeOn(Schedulers.io())
                .subscribe(res -> {
                    WifiState.setInfo(res.getSSID(), res.getBSSID());
                    //notifyContent(ssid + " " + mac);
                }).isDisposed();
    }

    private void startMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void startSignUp() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    private void emailLogin() {
        String strEmail = ((EditText)findViewById(R.id.et_userID)).getText().toString();
        String strPwd   = ((EditText)findViewById(R.id.et_password)).getText().toString();

        auth.signInWithEmailAndPassword(strEmail,strPwd)
                     .addOnCompleteListener(LoginActivity.this, task -> {
                        if(task.isSuccessful()) startMain();
                        else ToastUtil.showText(this, Objects.requireNonNull(task.getException()).getMessage());
                        });
    }
    private void googleLogin() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient client = GoogleSignIn.getClient(this, gso);
        startActivityForResult(client.getSignInIntent(), 123);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 123) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInAccount account;
            try {
                account = task.getResult(ApiException.class);
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                auth.signInWithCredential(credential)
                        .addOnCompleteListener(this, t -> {
                            if (t.isSuccessful()) startMain();
                            else ToastUtil.showText(this, Objects.requireNonNull(t.getException()).getMessage());
                        });
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    /* notification code */

    /* private void createNotificationChannel() {
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
    } */
}