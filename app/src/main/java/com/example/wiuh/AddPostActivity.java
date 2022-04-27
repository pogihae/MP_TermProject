package com.example.wiuh;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wiuh.model.Post;
import com.example.wiuh.model.User;
import com.example.wiuh.util.FirebaseUtil;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class AddPostActivity extends AppCompatActivity {
    private EditText etPostTitle;
    private EditText etPostBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        Objects.requireNonNull(getSupportActionBar()).hide();

        etPostTitle = findViewById(R.id.et_postTitle);
        etPostBody  = findViewById(R.id.et_postBody);

        findViewById(R.id.btn_submitPost).setOnClickListener(v -> addPost());
    }

    private void addPost() {
        String title = etPostTitle.getText().toString();
        String body = etPostBody.getText().toString();

        Post post = new Post(FirebaseAuth.getInstance().getUid(),
                title,
                User.getUserSingleTon().nickname,
                body);

        FirebaseUtil.getPostRef()
                    .push()
                    .setValue(post);

        finish();
    }
}