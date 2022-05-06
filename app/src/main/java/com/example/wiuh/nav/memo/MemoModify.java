package com.example.wiuh.nav.memo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wiuh.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

        EditText bulletinTitle = findViewById(R.id.mod_memoTitle);
        EditText bulletinBody = findViewById(R.id.mod_memoBody);

        bulletinTitle.setText(title);
        bulletinBody.setText(body);

        Button cancel = findViewById(R.id.cancel_mod_button);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}