package com.example.wiuh.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wiuh.R;
import com.example.wiuh.util.FirebaseUtil;
import com.example.wiuh.util.ToastUtil;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class PersonalSetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_setup);


        FirebaseUser curUser = FirebaseUtil.getCurUser();
        String nick = curUser.getDisplayName();

        EditText nickname = findViewById(R.id.nickname);
        nickname.setText(nick);

        Button editNickname = findViewById(R.id.editNickname);
        TextView textView=findViewById(R.id.logoutTextview);

        editNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nick = nickname.getText().toString();
                if (nick.matches("")) {
                    ToastUtil.showText(getApplicationContext(), "닉네임을 입력하세요");
                    return;
                }
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(nick)
                        .build();
                FirebaseUtil.getCurUser().updateProfile(profileUpdates);
                ToastUtil.showText(getApplicationContext(),"닉네임 변경 완료");
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUtil.logout(getApplicationContext());
                ToastUtil.showText(getApplicationContext(),"로그아웃");
            }
        });

    }
}