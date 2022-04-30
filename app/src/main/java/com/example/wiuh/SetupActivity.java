package com.example.wiuh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wiuh.util.ToastUtil;

public class SetupActivity extends AppCompatActivity {
    static final int RS_SUCCESS = 1;
    static final String NICKNAME = "NICKNAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        //getSupportActionBar().hide();

        Button btnNicknameSubmit    = findViewById(R.id.GoToMain);
        EditText etNickname         = findViewById(R.id.nickname_edit);

        btnNicknameSubmit.setOnClickListener(view -> {
            String nick = etNickname.getText().toString();
            if(nick.matches("")){
                ToastUtil.showText(getApplicationContext(), "닉네임을 입력하세요");
                return;
            }

            Intent result = new Intent();
            result.putExtra(NICKNAME, nick);
            setResult(RS_SUCCESS, result);
            finish();
        });

    }
}