package com.example.wiuh.nav.memo;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wiuh.R;
import com.example.wiuh.model.Memo;
import com.example.wiuh.model.Post;
import com.example.wiuh.util.FirebaseUtil;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class AddMemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memo);
        Objects.requireNonNull(getSupportActionBar()).hide();

        findViewById(R.id.submit_memo_btn).setOnClickListener(v -> addMemo());
    }

    private void addMemo() {
        String title = ((EditText)findViewById(R.id.add_title_memo)).getText().toString();
        String body = ((EditText)findViewById(R.id.add_content_memo)).getText().toString();

        Memo memo = new Memo(FirebaseUtil.getCurUserUid(), title, FirebaseUtil.getCurUserNickname(), body,null);
        FirebaseUtil.getMemoRef().push().setValue(memo);

        finish();
    }
}
