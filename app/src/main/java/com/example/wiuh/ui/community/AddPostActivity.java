package com.example.wiuh.ui.community;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wiuh.R;
import com.example.wiuh.model.Post;
import com.example.wiuh.util.FirebaseUtil;
import com.google.firebase.auth.FirebaseUser;

public class AddPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        getSupportActionBar().setTitle("게시글 작성");

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}