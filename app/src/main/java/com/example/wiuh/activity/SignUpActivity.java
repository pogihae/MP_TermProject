package com.example.wiuh.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.wiuh.R;
import com.example.wiuh.util.ToastUtil;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

/**
 * SignUpActivity
 * <p>
 * LoginActivity 에서 시작됨
 * Firebase 에 user 등록까지만 책임
 */
public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.app_blue));
        findViewById(R.id.signup_register).setOnClickListener(v -> register());

        //confirm password
        ((EditText) findViewById(R.id.signup_pw)).addTextChangedListener(passwordWatcher());
        ((EditText) findViewById(R.id.signup_pw2)).addTextChangedListener(passwordWatcher());
    }

    private void register() {
        String email = ((EditText) findViewById(R.id.signup_id)).getText().toString();
        String pw = ((EditText) findViewById(R.id.signup_pw)).getText().toString();
        String secondPw = ((EditText) findViewById(R.id.signup_pw2)).getText().toString();

        if (!pw.equals(secondPw)) {
            ToastUtil.showText(this, "비밀번호가 일치하지 않습니다.");
            return;
        }

        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAuth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener(this, task -> {
            if (task.isSuccessful())
                ToastUtil.showText(getApplicationContext(), "회원가입 성공");
            else
                ToastUtil.showText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage());

            finish();
        });
    }

    private TextWatcher passwordWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String pw = ((EditText) findViewById(R.id.signup_pw)).getText().toString();
                String secondPw = ((EditText) findViewById(R.id.signup_pw2)).getText().toString();
                int imageId = (pw.equals(secondPw)) ? R.drawable.ic_true : R.drawable.ic_false;

                ((ImageView) findViewById(R.id.setImage)).setImageResource(imageId);
            }
        };
    }
}