package com.example.wiuh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wiuh.model.User;
import com.example.wiuh.util.FirebaseUtil;
import com.example.wiuh.util.ToastUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
        idCheck         = findViewById(R.id.signin_id_check);
        setImage        = findViewById(R.id.setImage);

        findViewById(R.id.sighin_submit).setOnClickListener(v -> register());

        idCheck.setOnClickListener(view -> ToastUtil.showText(getApplicationContext(), "아이디 중복 버튼 눌림"));

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


    //todo
    private boolean isCompleteForm() {
        return true;
    }

    private void register() {
        if (!isCompleteForm()) {
            ToastUtil.showText(this, "Please Complete form");
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

            FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();

            User user = new User(email);
            mUserRef.child(currentUser.getUid()).setValue(user);

            Log.d(TAG, "Sign up success");
            finish();
        });
    }
}