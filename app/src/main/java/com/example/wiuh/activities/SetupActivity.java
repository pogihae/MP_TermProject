package com.example.wiuh.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wiuh.R;
import com.example.wiuh.util.FirebaseUtil;
import com.example.wiuh.util.ToastUtil;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

/**
 * SetUpActivity
 * <p>
 * MainActivity 에서 시작됨
 * Nickname 및 세부 설정
 */
public class SetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        Objects.requireNonNull(getSupportActionBar()).hide();

        Button btnNicknameSubmit = findViewById(R.id.GoToMain);
        Button NotificationList = findViewById(R.id.notification_list);
        EditText etNickname = findViewById(R.id.nickname_edit);

        btnNicknameSubmit.setOnClickListener(v -> {
            String nick = etNickname.getText().toString();
            if (nick.matches("")) {
                ToastUtil.showText(getApplicationContext(), "닉네임을 입력하세요");
                return;
            }

            // firebase authentication update
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nick)
                    .build();
            FirebaseUtil.getCurUser().updateProfile(profileUpdates);

            finish();
        });
        NotificationList.setOnClickListener(v -> {
            Intent intent = new Intent(this, SetupListActivity.class);
            startActivity(intent);
        });
    }

    //뒤로가기 금지
    @Override
    public void onBackPressed() {
        ToastUtil.showText(this, "Set nickname");
        //super.onBackPressed();
    }
}