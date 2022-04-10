package com.example.wiuh;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void clickSignin(View view){
        //SigninActivity 실행..
        Intent intent =new Intent(this,SigninActivity.class);
        startActivity(intent);
    }
}