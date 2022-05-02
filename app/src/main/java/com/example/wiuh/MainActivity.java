package com.example.wiuh;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.wiuh.databinding.ActivityMainBinding;
import com.example.wiuh.model.WifiInformation;
import com.example.wiuh.util.FirebaseUtil;
import com.example.wiuh.util.ToastUtil;

import java.util.Objects;

/**
 *
 * */
public class MainActivity extends AppCompatActivity {
    static final int RQ_NICKNAME = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBotNav();

        //wifi 정보 action bar 표시
        Objects.requireNonNull(getSupportActionBar())
                .setTitle(WifiInformation.getSSID());

        //nickname 설정 및 표시
        String nickname = FirebaseUtil.getCurUserNickname();
        if(nickname == null || nickname.matches("")) startSetUp();
        else ToastUtil.showText(this, nickname + " 환영");
    }

    private void setBotNav() {
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_memo, R.id.navigation_community)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    private void startSetUp() {
        ToastUtil.showText(this, "닉네임을 설정하세요");
        Intent intent = new Intent(this, SetupActivity.class);
        startActivityForResult(intent, RQ_NICKNAME);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RQ_NICKNAME && resultCode == SetupActivity.RS_SUCCESS) {
            String nickname = data.getStringExtra(SetupActivity.NICKNAME);
            FirebaseUtil.updateCurUserNickname(nickname);
        }
    }

}