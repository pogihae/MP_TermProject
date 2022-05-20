package com.example.wiuh.activity.memo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wiuh.R;
import com.example.wiuh.model.Memo;
import com.example.wiuh.util.FirebaseUtil;
import com.google.firebase.auth.FirebaseUser;

public class ModifyMemoActivity extends AppCompatActivity {
    static final int RS_SUC = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_memo);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String title = bundle.getString("title");
        String body = bundle.getString("body");
        String key = bundle.getString("key");

        EditText bulletinTitle = findViewById(R.id.mod_memoTitle);
        EditText bulletinBody = findViewById(R.id.mod_memoBody);

        bulletinTitle.setText(title);
        bulletinBody.setText(body);

        Button ok = findViewById(R.id.ok_mod_button);
        ok.setOnClickListener(view -> {
            FirebaseUser curUser = FirebaseUtil.getCurUser();

            String modTitle = bulletinTitle.getText().toString();
            String modBody = bulletinBody.getText().toString();

            Memo memo = new Memo(curUser.getUid(), modTitle, curUser.getDisplayName(), modBody);
            FirebaseUtil.getMemoRef().child(key).setValue(memo);

            Intent resultIntent = new Intent();
            resultIntent.putExtra("resultTitle", modTitle);
            resultIntent.putExtra("resultBody", modBody);
            setResult(RS_SUC, resultIntent);
            finish();
        });

        Button cancel = findViewById(R.id.cancel_mod_button);

        cancel.setOnClickListener(v -> finish());
    }

}