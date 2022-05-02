package com.example.wiuh;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wiuh.util.ToastUtil;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Objects.requireNonNull(getSupportActionBar()).hide();

        findViewById(R.id.signup_register).setOnClickListener(v->register());

        //confirm password
        ((EditText)findViewById(R.id.signup_pw)).addTextChangedListener(passwordWatcher());
        ((EditText)findViewById(R.id.signup_pw2)).addTextChangedListener(passwordWatcher());
    }

    private void register() {
        String email    = ((EditText)findViewById(R.id.signup_id)).getText().toString();
        String pw       = ((EditText)findViewById(R.id.signup_pw)).getText().toString();
        String secondPw = ((EditText)findViewById(R.id.signup_pw2)).getText().toString();

        if (!pw.equals(secondPw)) {
            ToastUtil.showText(this, "비밀번호가 일치하지 않습니다.");
            return;
        }

        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAuth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener(this, task -> {
            if (!task.isSuccessful())
                ToastUtil.showText(this, task.getException().getMessage());
        });

        finish();
    }

    private TextWatcher passwordWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                String pw       = ((EditText)findViewById(R.id.signup_pw)).getText().toString();
                String secondPw = ((EditText)findViewById(R.id.signup_pw2)).getText().toString();
                int imageId = (pw.equals(secondPw))? R.drawable.ic_true : R.drawable.ic_false;

                ((ImageView)findViewById(R.id.setImage)).setImageResource(imageId);
            }
        };
    }
}