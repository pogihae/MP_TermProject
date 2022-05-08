package com.example.wiuh.nav.memo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wiuh.R;
import com.example.wiuh.model.Memo;
import com.example.wiuh.util.FirebaseUtil;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MemoModify extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_memo);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

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
        ok.setOnClickListener(v -> {
            String title1 = bulletinTitle.getText().toString();
            String body1 = bulletinBody.getText().toString();

            Map<String, Object> map = new HashMap<>(2);
            map.put("title", title1);
            map.put("body", body1);

            FirebaseUtil.getMemoRef().child(key).updateChildren(map);
            finish();
        });

        Button cancel = findViewById(R.id.cancel_mod_button);

        cancel.setOnClickListener(v -> finish());
    }


}