package com.example.wiuh;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wiuh.util.FirebaseUtil;
import com.example.wiuh.util.ToastUtil;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

/**
 * SetUpActivity
 *
 * MainActivity 에서 시작됨
 * Nickname 및 세부 설정
 *
 * */
public class SetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        Objects.requireNonNull(getSupportActionBar()).hide();

        Button btnNicknameSubmit    = findViewById(R.id.GoToMain);
        EditText etNickname         = findViewById(R.id.nickname_edit);

        btnNicknameSubmit.setOnClickListener(v -> {
            String nick = etNickname.getText().toString();
            if(nick.matches("")){
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
    }

    //뒤로가기 금지
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}