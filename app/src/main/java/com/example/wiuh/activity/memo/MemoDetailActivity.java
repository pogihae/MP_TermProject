package com.example.wiuh.activity.memo;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wiuh.R;
import com.example.wiuh.util.FirebaseUtil;

public class MemoDetailActivity extends AppCompatActivity {
    static final int RQ_MOD = 10;

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

        modButton.setOnClickListener(v -> {
            Intent intent1 = new Intent(getBaseContext(), ModifyMemoActivity.class);
            Bundle bundle1 = new Bundle();

            bundle1.putString("title", title);
            bundle1.putString("body", body);
            bundle1.putString("key", key);

            intent1.putExtras(bundle1);
            startActivityForResult(intent1, RQ_MOD);
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ModifyMemoActivity.RS_SUC) {
            if (resultCode == RQ_MOD) {

                Bundle resultBundle = data.getExtras();
                String resultTitle = resultBundle.getString("resultTitle");
                String resultBody = resultBundle.getString("resultBody");

                TextView title = findViewById(R.id.memoTitle);
                TextView body = findViewById(R.id.memoBody);

                title.setText(resultTitle);
                body.setText(resultBody);

                Toast.makeText(getApplicationContext(), resultTitle + resultBody, Toast.LENGTH_SHORT).show();
            } else {   // RESULT_CANCEL
                Toast.makeText(getApplicationContext(), "취소", Toast.LENGTH_SHORT).show();
            }
        }
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