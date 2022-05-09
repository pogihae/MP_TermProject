package com.example.wiuh.nav.memo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wiuh.MainActivity;
import com.example.wiuh.R;
import com.example.wiuh.util.FirebaseUtil;

public class MemoActivity extends AppCompatActivity {

    private int REQUEST_CODE = 10;
    private int RESULT_OK = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String title = bundle.getString("title");
        String body = bundle.getString("body");
        String key = bundle.getString("key");

        TextView bulletinTitle = findViewById(R.id.memoTitle);
        TextView bulletinBody = findViewById(R.id.memoBody);

        bulletinTitle.setText(title);
        bulletinBody.setText(body);

        Button delButton = findViewById(R.id.del_Button);
        delButton.setOnClickListener(view -> {
            FirebaseUtil.getMemoRef()
                    .child(key)
                    .removeValue();
            finish();
        });

        Button modButton = findViewById(R.id.mod_button);

        modButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MemoModify.class);
                Bundle bundle = new Bundle();

                bundle.putString("title", title);
                bundle.putString("body", body);
                bundle.putString("key", key);

                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                Bundle resultBundle = data.getExtras();
                String resultTitle = resultBundle.getString("resultTitle");
                String resultBody = resultBundle.getString("resultBody");

                TextView title = findViewById(R.id.memoTitle);
                TextView body = findViewById(R.id.memoBody);

                title.setText(resultTitle);
                body.setText(resultBody);

                Toast.makeText(getApplicationContext(), resultTitle + resultBody, Toast.LENGTH_SHORT).show();
            }
            else {   // RESULT_CANCEL
                Toast.makeText(getApplicationContext(), "취소", Toast.LENGTH_SHORT).show();
            }
        }
    }
}