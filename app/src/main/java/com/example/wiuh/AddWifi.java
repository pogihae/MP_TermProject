package com.example.wiuh;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wiuh.util.FirebaseUtil;

public class AddWifi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wifi);

        TextView ssid_info = findViewById(R.id.SSID_Info);
        TextView mac_info = findViewById(R.id.MAC_Info);
        Button register = findViewById(R.id.btn_register);

        ssid_info.setText(WifiInfo.getSSID());
        mac_info.setText((WifiInfo.getMAC()));

        register.setOnClickListener(v -> addWifi());
    }

    private void addWifi() {
        String SSID = WifiInfo.getSSID();
        String MAC = WifiInfo.getMAC();

        FirebaseUtil.getWifiRef().child(MAC).setValue(SSID);
        finish();
    }
}