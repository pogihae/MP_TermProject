package com.example.wiuh;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wiuh.model.User;
import com.example.wiuh.util.FirebaseUtil;
import com.example.wiuh.util.ToastUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

/**
 *
 */
public class SignUpActivity extends AppCompatActivity {
    static final String TAG = SignUpActivity.class.getSimpleName();

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mUserRef;

    private EditText mEtEmail;
    private EditText mEtPwd;
    private EditText mEtSecondPwd;
    private ImageView setImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFirebaseAuth   = FirebaseAuth.getInstance();
        mUserRef        = FirebaseUtil.getUserRef();

        mEtEmail        = findViewById(R.id.signin_id);
        mEtPwd          = findViewById(R.id.signin_password);
        mEtSecondPwd    = findViewById(R.id.signin_check_password);
        setImage        = findViewById(R.id.setImage);

        findViewById(R.id.sighin_submit).setOnClickListener(v -> register());

        mEtSecondPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mEtPwd.getText().toString().equals(mEtSecondPwd.getText().toString())) {
                    setImage.setImageResource(R.drawable.ic_true);
                } else {
                    setImage.setImageResource(R.drawable.ic_false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    private void register() {
        if (!mEtPwd.getText().toString().equals(mEtSecondPwd.getText().toString())) {
            ToastUtil.showText(this, "비밀번호가 일치하지 않습니다.");
            return;
        }

        String email    = mEtEmail.getText().toString();
        String pw       = mEtPwd.getText().toString();

        mFirebaseAuth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener(this, task -> {
            if (!task.isSuccessful()) {
                ToastUtil.showText(this, task.getException().getMessage());
                return;
            }

            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
            User user = new User();

            assert firebaseUser != null;
            mUserRef.child(firebaseUser.getUid()).setValue(user);

            Log.d(TAG, "Sign up success");
            finish();
        });
    }
}