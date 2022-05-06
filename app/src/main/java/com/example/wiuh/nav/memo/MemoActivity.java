package com.example.wiuh.nav.memo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.wiuh.model.Post;
import com.example.wiuh.util.FirebaseUtil;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wiuh.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String title = bundle.getString("title");
        String body = bundle.getString("body");
        String key = bundle.getString("key");

        TextView bulletinTitle = findViewById(R.id.memoTitle);
        TextView bulletinBody = findViewById(R.id.memoBody);

        bulletinTitle.setText(title);
        bulletinBody.setText(body + key);

        Button delButton = findViewById(R.id.del_Button);

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUtil.getMemoRef().child(key).removeValue();
            }
        });

        Button modButton = findViewById(R.id.mod_button);

        modButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MemoModify.class);
                Bundle bundle = new Bundle();

                bundle.putString("title", title);
                bundle.putString("body", body);

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }



}