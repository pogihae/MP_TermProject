package com.example.wiuh.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wiuh.R;
import com.example.wiuh.util.FirebaseUtil;
import com.example.wiuh.util.ToastUtil;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);


        FirebaseUser curUser = FirebaseUtil.getCurUser();
        String nick = curUser.getDisplayName();

        EditText nickname = findViewById(R.id.nickname_edit);
        nickname.setText(nick);

        Button editNickname = findViewById(R.id.GoToMain);

        editNickname.setOnClickListener(v -> {
            String nick1 = nickname.getText().toString();
            if (nick1.matches("")) {
                ToastUtil.showText(getApplicationContext(), "닉네임을 입력하세요");
                return;
            }
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nick1)
                    .build();
            FirebaseUtil.getCurUser().updateProfile(profileUpdates);
            ToastUtil.showText(getApplicationContext(), "닉네임이 설정되었습니다");
            finish();
        });
    }

    //뒤로가기 금지
    @Override
    public void onBackPressed() {
        ToastUtil.showText(this, "닉네임을 입력하세요");
        //super.onBackPressed();
    }
}