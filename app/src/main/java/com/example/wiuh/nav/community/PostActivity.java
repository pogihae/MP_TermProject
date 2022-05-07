package com.example.wiuh.nav.community;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wiuh.R;
import com.example.wiuh.nav.community.PostModify;
import com.example.wiuh.util.FirebaseUtil;

public class PostActivity extends AppCompatActivity {

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
        String key = bundle.getString("key");

        TextView bulletinTitle = findViewById(R.id.bulletinTitle);
        TextView bulletinBody = findViewById(R.id.bulletinBody);
        TextView bulletinAuth = findViewById(R.id.bulletinAuth);
        TextView bulletinUid = findViewById(R.id.bulletinUid);

        bulletinTitle.setText(title);
        bulletinBody.setText(body);
        bulletinAuth.setText(author);
        bulletinUid.setText(uid);

        Button delButton = findViewById(R.id.btn_delpost);
        Button modButton = findViewById(R.id.btn_modpost);

        if(!isAuthor(uid)) {
            delButton.setVisibility(View.INVISIBLE);
            modButton.setVisibility(View.INVISIBLE);
        }

        delButton.setOnClickListener(view -> {
            FirebaseUtil.getPostRef()
                    .child(key)
                    .removeValue();
            finish();
        });

        modButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), PostModify.class);
                Bundle bundle = new Bundle();

                bundle.putString("title", title);
                bundle.putString("body", body);
                bundle.putString("key", key);

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private boolean isAuthor(String uid) {
        String curUserUid = FirebaseUtil.getCurUser().getUid();
        return uid.equals(curUserUid);
    }
}