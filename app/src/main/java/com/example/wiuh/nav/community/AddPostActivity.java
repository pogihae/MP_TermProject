package com.example.wiuh.nav.community;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wiuh.R;
import com.example.wiuh.model.Post;
import com.example.wiuh.util.FirebaseUtil;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class AddPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        Objects.requireNonNull(getSupportActionBar()).hide();

        findViewById(R.id.btn_submitPost).setOnClickListener(v -> addPost());
    }

    private void addPost() {
        FirebaseUser curUser = FirebaseUtil.getCurUser();

        String title = ((EditText) findViewById(R.id.et_postTitle)).getText().toString();
        String body = ((EditText) findViewById(R.id.et_postBody)).getText().toString();

        Post post = new Post(curUser.getUid(), title, curUser.getDisplayName(), body);
        FirebaseUtil.getPostRef().push().setValue(post);

        finish();
    }
}