package com.example.wiuh;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wiuh.model.Wifi;
import com.example.wiuh.util.FirebaseUtil;
import com.google.firebase.auth.FirebaseUser;

public class AddWifi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wifi);

        TextView ssid_info = findViewById(R.id.SSID_Info);
        TextView mac_info = findViewById(R.id.MAC_Info);
        Button register = findViewById(R.id.btn_register);

        ssid_info.setText(WifiState.getSSID());
        mac_info.setText((WifiState.getMAC()));


        register.setOnClickListener(v -> addWifi());


    }

    private void addWifi() {
        FirebaseUser curUser = FirebaseUtil.getCurUser();
        String SSID = WifiState.getSSID();
        String MAC = WifiState.getMAC();

        Wifi wifi = new Wifi(curUser.getUid(), SSID, MAC);
        FirebaseUtil.getWifiRef().setValue(wifi);
    }
}