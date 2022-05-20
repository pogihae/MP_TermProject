package com.example.wiuh.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.dd.processbutton.iml.ActionProcessButton;
import com.example.wiuh.R;
import com.example.wiuh.util.ToastUtil;
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

import se.warting.permissionsui.backgroundlocation.PermissionsUiContracts;

/**
 * LoginActivity
 * <p>
 * 시작
 * 권한, 로그인 확인
 * <p>
 * todo: Splash Activity 추가 후 권한, 네트워크 확인 등을 넘기기
 */
public class LoginActivity extends AppCompatActivity {
    static final String TAG = LoginActivity.class.getSimpleName();
    static final String CHANNEL_ID = "WIFI_INFO";

    private FirebaseAuth auth;
    private ActionProcessButton btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.app_blue));
        getSupportActionBar().hide();

        //permission & wifi info observation
        registerForActivityResult(
                new PermissionsUiContracts.RequestBackgroundLocation(),
                success -> createNotificationChannel()
        ).launch(null);

        //already login
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) startMain();

        btnSignIn = findViewById(R.id.btn_login);
        btnSignIn.setMode(ActionProcessButton.Mode.ENDLESS);
        btnSignIn.setProgress(0);

        btnSignIn.setOnClickListener(v -> emailLogin());
        findViewById(R.id.btn_google_login).setOnClickListener(v -> googleLogin());
        findViewById(R.id.btn_signup).setOnClickListener(v -> startSignUp());
    }

    private void startMain() {
        if(!hasWifiConnection()) {
            ToastUtil.showText(this, "NEED WIFI CONNECTION");
            return;
        }

        Intent intent = new Intent(this, BoardActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean hasWifiConnection() {
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null &&
                networkInfo.isConnectedOrConnecting() &&
                networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    private void startSignUp() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    private void emailLogin() {
        btnSignIn.setEnabled(false);
        btnSignIn.setProgress(1);

        String strEmail = ((EditText) findViewById(R.id.et_userID)).getText().toString();
        String strPwd = ((EditText) findViewById(R.id.et_password)).getText().toString();

        auth.signInWithEmailAndPassword(strEmail, strPwd)
                .addOnCompleteListener(LoginActivity.this, task -> {
                    if (task.isSuccessful()) startMain();
                    else {
                        ToastUtil.showText(this, Objects.requireNonNull(task.getException()).getMessage());
                        btnSignIn.setEnabled(true);
                    }
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

        if (requestCode == 123) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInAccount account;
            try {
                account = task.getResult(ApiException.class);
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                auth.signInWithCredential(credential)
                        .addOnCompleteListener(this, t -> {
                            if (t.isSuccessful()) startMain();
                            else
                                ToastUtil.showText(this, Objects.requireNonNull(t.getException()).getMessage());
                        });
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    /* notification code */
    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "name", importance);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}