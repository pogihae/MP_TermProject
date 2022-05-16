package com.example.wiuh.ui.community;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wiuh.R;
import com.example.wiuh.model.Post;
import com.example.wiuh.util.FirebaseUtil;
import com.example.wiuh.util.ToastUtil;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.atomic.AtomicReference;

public class PostDetailActivity extends AppCompatActivity {
    static final int RQ_MOD = 10;

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
        AtomicReference<Integer> like = new AtomicReference<>(bundle.getInt("like"));

        TextView bulletinTitle = findViewById(R.id.bulletinTitle);
        TextView bulletinBody = findViewById(R.id.bulletinBody);
        TextView bulletinAuth = findViewById(R.id.bulletinAuth);
        TextView bulletinLike = findViewById(R.id.bulletinLike);

        bulletinTitle.setText(title);
        bulletinBody.setText(body);
        bulletinAuth.setText(author);
        bulletinLike.setText(like.toString());

        Button likeButton = findViewById(R.id.btn_LikePost);
        likeButton.setVisibility(View.INVISIBLE);
        likeButton.setSelected(false);
        Button delButton = findViewById(R.id.btn_delpost);
        Button modButton = findViewById(R.id.btn_modpost);

        if (!isAuthor(uid)) {
            likeButton.setVisibility(View.VISIBLE);
            delButton.setVisibility(View.INVISIBLE);
            modButton.setVisibility(View.INVISIBLE);
        }

        likeButton.setOnClickListener(view -> {
            FirebaseUser curUser = FirebaseUtil.getCurUser();
            Post post;
            if(!likeButton.isSelected()){
                like.set(like.get() + 1);
                post = new Post(curUser.getUid(), title, curUser.getDisplayName(), body, like.get());
                FirebaseUtil.getPostRef().child(key).setValue(post);
                bulletinLike.setText(like.get().toString());
                ToastUtil.showText(this, "좋아요");
                likeButton.setSelected(true);
            }
            else {
                like.set(like.get() - 1);
                post = new Post(curUser.getUid(), title, curUser.getDisplayName(), body, like.get());
                FirebaseUtil.getPostRef().child(key).setValue(post);
                bulletinLike.setText(like.get().toString());
                ToastUtil.showText(this, "좋아요 취소");
                likeButton.setSelected(false);
            }
        });

        delButton.setOnClickListener(view -> {
            FirebaseUtil.getPostRef()
                    .child(key)
                    .removeValue();
            finish();
        });

        modButton.setOnClickListener(view -> {
            Intent intent1 = new Intent(getBaseContext(), PostModify.class);
            Bundle bundle1 = new Bundle();

            bundle1.putString("title", title);
            bundle1.putString("body", body);
            bundle1.putString("key", key);
            bundle1.putInt("like", like.get());

            intent1.putExtras(bundle1);
            startActivityForResult(intent1, RQ_MOD);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RQ_MOD) {
            if (resultCode == PostModify.RS_SUC) {

                Bundle resultBundle = data.getExtras();
                String resultTitle = resultBundle.getString("resultTitle");
                String resultBody = resultBundle.getString("resultBody");

                TextView title = findViewById(R.id.bulletinTitle);
                TextView body = findViewById(R.id.bulletinBody);

                title.setText(resultTitle);
                body.setText(resultBody);

            } else {   // RESULT_CANCEL
                Toast.makeText(getApplicationContext(), "취소", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isAuthor(String uid) {
        String curUserUid = FirebaseUtil.getCurUser().getUid();
        return uid.equals(curUserUid);
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