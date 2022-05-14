package com.example.wiuh.ui.community;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wiuh.R;
import com.example.wiuh.model.Post;
import com.example.wiuh.util.FirebaseUtil;
import com.google.firebase.auth.FirebaseUser;

public class PostModify extends AppCompatActivity {
    static final int RS_SUC = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_bulletin);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String title = bundle.getString("title");
        String body = bundle.getString("body");
        String key = bundle.getString("key");

        EditText bulletinTitle = findViewById(R.id.mod_bulletinTitle);
        EditText bulletinBody = findViewById(R.id.mod_bulletinBody);

        bulletinTitle.setText(title);
        bulletinBody.setText(body);

        Button ok = findViewById(R.id.btn_ok_mod);
        ok.setOnClickListener(v -> {
            FirebaseUser curUser = FirebaseUtil.getCurUser();

            String title1 = bulletinTitle.getText().toString();
            String body1 = bulletinBody.getText().toString();

            Post post = new Post(curUser.getUid(), title1, curUser.getDisplayName(), body1);
            FirebaseUtil.getPostRef().child(key).setValue(post);

            Intent resultIntent = new Intent();
            resultIntent.putExtra("resultTitle", title1);
            resultIntent.putExtra("resultBody", body1);
            setResult(RS_SUC, resultIntent);
            finish();
        });

        Button cancel = findViewById(R.id.btn_cancel_mod);

        cancel.setOnClickListener(v -> finish());
    }


}