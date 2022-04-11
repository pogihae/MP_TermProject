package com.example.wiuh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;         //파이어베이스 인증
    private DatabaseReference mDatabaseRef;     //실시간 파이어베이스


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Wi U here");


        EditText mEtEmail = findViewById(R.id.userID);
        EditText mEtPwd = findViewById(R.id.password);


        TextView btn_login = findViewById(R.id.login_request);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 로그인 요청
                String strEmail = mEtEmail.getText().toString();
                String strPwd = mEtPwd.getText().toString();

                mFirebaseAuth.signInWithEmailAndPassword(strEmail,strPwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //로그인 성공
                            Intent intent = new Intent(LoginActivity.this,InitialSetupActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(LoginActivity.this,"로그인 실패",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        TextView btn_register = findViewById(R.id.signin);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 회원가입으로 이동

                Intent intent = new Intent(LoginActivity.this,SigninActivity.class);
                startActivity(intent);
            }
        });
    }
}