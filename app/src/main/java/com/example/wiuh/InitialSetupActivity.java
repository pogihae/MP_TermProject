package com.example.wiuh;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InitialSetupActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;         //파이어베이스 인증
    private DatabaseReference mDatabaseRef;     //실시간 파이어베이스

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_setup);


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Button button = findViewById(R.id.GoToMain);
        EditText nickname = findViewById(R.id.nickname_edit);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Wi U here");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nick = nickname.getText().toString();
                if(nick.equals("닉네임을 입력하세요")){
                    Toast.makeText(getApplicationContext(),"닉네임을 입력하세요", Toast.LENGTH_SHORT);
                }
                else{

                }

                Intent intent = new Intent(getApplicationContext() ,MainActivity.class);
                startActivity(intent);
            }
        });

    }
}