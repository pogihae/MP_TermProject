package com.example.wiuh;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class SigninActivity extends AppCompatActivity {

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
    }
    public void clickSignin_confirm(View view) {
            //MainActivity class 실행..

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        //SigninActivity 종료 (메모리에서 제거)
        finish();

    }
}
