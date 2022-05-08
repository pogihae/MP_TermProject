package com.example.wiuh.nav.community;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wiuh.R;
import com.example.wiuh.model.Post;
import com.example.wiuh.util.FirebaseUtil;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostModify extends AppCompatActivity {

    private int RESULT_OK = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_bulletin);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

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
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser curUser = FirebaseUtil.getCurUser();

                String title = bulletinTitle.getText().toString();
                String body = bulletinBody.getText().toString();

                Post post = new Post(curUser.getUid(), title, curUser.getDisplayName(), body);
                FirebaseUtil.getPostRef().child(key).setValue(post);

                Intent resultIntent = new Intent();
                resultIntent.putExtra("resultTitle", title);
                resultIntent.putExtra("resultBody", body);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        Button cancel = findViewById(R.id.btn_cancel_mod);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}