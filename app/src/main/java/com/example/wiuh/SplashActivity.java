package com.example.wiuh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.wiuh.activity.board.BoardActivity;
import com.example.wiuh.activity.sign.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //어플리케이션 본격적인 구동 전 하려는 작업을 작성(DB 등 리소스 로드라던지)
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finish();
    }

}