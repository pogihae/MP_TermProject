package com.example.wiuh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.wiuh.R;

public class BulletinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulletin);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String title = bundle.getString("title");
        String body = bundle.getString("body");
        String author = bundle.getString("author");
        String uid = bundle.getString("uid");

        TextView bulletinTitle = findViewById(R.id.bulletinTitle);
        TextView bulletinBody = findViewById(R.id.bulletinBody);
        TextView bulletinAuth = findViewById(R.id.bulletinAuth);
        TextView bulletinUid = findViewById(R.id.bulletinUid);

        bulletinTitle.setText(title);
        bulletinBody.setText(body);
        bulletinAuth.setText(author);
        bulletinUid.setText(uid);

    }
}