package com.example.wiuh;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wiuh.util.FirebaseUtil;
import com.example.wiuh.util.ToastUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

/**
 * todo: submit 시 모든 칸 채워져 있는지, 중복 검사, 비밀번호 동일
 */
public class SignUpActivity extends AppCompatActivity {
    static final String TAG = SignUpActivity.class.getSimpleName();

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mUserRef;

    private EditText mEtEmail;
    private EditText mEtPwd;
    private EditText mEtSecondPwd;
    private TextView idCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        mFirebaseAuth   = FirebaseAuth.getInstance();
        mUserRef        = FirebaseUtil.getUserRef();

        mEtEmail        = findViewById(R.id.signin_id);
        mEtPwd          = findViewById(R.id.signin_password);
        mEtSecondPwd    = findViewById(R.id.signin_check_password);
        idCheck         = findViewById(R.id.signin_id_check);

        findViewById(R.id.sighin_submit).setOnClickListener(v -> register());
    }

    private boolean isCompleteForm() {
        return true;
    }

    private void register() {
        if (!isCompleteForm()) {
            Toast.makeText(this, "모든 칸을 채워주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        String email    = mEtEmail.getText().toString();
        String pw       = mEtPwd.getText().toString();

        mFirebaseAuth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener(SignUpActivity.this, task -> {
            if (!task.isSuccessful()) {
                ToastUtil.showText(this, "Sign up fail");
                Log.d(TAG, "Sign up fail");
                return;
            }

            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
            User user = new User(email);

            assert firebaseUser != null;
            mUserRef.child(firebaseUser.getUid()).setValue(user);

            Log.d(TAG, "Sign up success");
            finish();
        });
    }
}