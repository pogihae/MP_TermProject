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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class SigninActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;         //파이어베이스 인증
    private DatabaseReference mDatabaseRef;     //실시간 파이어베이스

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Wi U here");

        EditText mEtEmail = findViewById(R.id.signin_id);
        EditText mEtPwd = findViewById(R.id.signin_password);
        TextView submit = (TextView) findViewById(R.id.sighin_submit);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 회원가입 처리
                String strEmail = mEtEmail.getText().toString();
                String strPwd = mEtPwd.getText().toString();

                // FirebaseAuth 진행
                mFirebaseAuth.createUserWithEmailAndPassword(strEmail,strPwd).addOnCompleteListener(SigninActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // 회원가입 성공했을 때
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                            UserAccount account = new UserAccount();
                            account.setIdToken(firebaseUser.getUid());
                            account.setEmailId(firebaseUser.getEmail());
                            account.setPassword(strPwd);

                            // 데베에 삽입
                            mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);

                            Toast.makeText(SigninActivity.this, "회원가입 성공",Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(SigninActivity.this,LoginActivity.class);
                            startActivity(intent);

                        }else{
                            Toast.makeText(SigninActivity.this, "회원가입 실패",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}