package com.example.wiuh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InitialSetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_setup);

        Button button = findViewById(R.id.GoToMain);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InitialSetupActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}