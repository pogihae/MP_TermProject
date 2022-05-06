package com.example.wiuh.nav.memo;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wiuh.R;
import com.example.wiuh.model.Memo;
import com.example.wiuh.util.FirebaseUtil;
import com.google.firebase.auth.FirebaseUser;

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
        FirebaseUser curUser = FirebaseUtil.getCurUser();

        String title = ((EditText)findViewById(R.id.add_title_memo)).getText().toString();
        String body = ((EditText)findViewById(R.id.add_content_memo)).getText().toString();

        Memo memo = new Memo(curUser.getUid(), title, curUser.getDisplayName(), body);
        FirebaseUtil.getMemoRef().push().setValue(memo);

        finish();
    }
}
