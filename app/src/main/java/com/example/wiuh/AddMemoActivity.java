package com.example.wiuh;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wiuh.model.Memo;
import com.example.wiuh.util.FirebaseUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class AddMemoActivity extends AppCompatActivity {
    EditText add_title_memo,add_content_memo;
    Button submit_memo_btn;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memo);
        Objects.requireNonNull(getSupportActionBar()).hide();
        user = FirebaseAuth.getInstance().getCurrentUser();

        add_title_memo = findViewById(R.id.add_title_memo);
        add_content_memo  = findViewById(R.id.add_content_memo);

        findViewById(R.id.submit_memo_btn).setOnClickListener(v -> addMemo());
    }
    private void addMemo() {
        String title = add_title_memo.getText().toString();
        String body = add_content_memo.getText().toString();

        Memo memo = new Memo(FirebaseAuth.getInstance().getUid(), title, user.getDisplayName(), body);
        FirebaseUtil.getMemoRef().push().setValue(memo);
        finish();
    }
}
