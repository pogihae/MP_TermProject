package com.example.wiuh;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.wiuh.databinding.ActivityMainBinding;
import com.example.wiuh.model.WifiState;
import com.example.wiuh.util.FirebaseUtil;
import com.example.wiuh.util.ToastUtil;

import java.util.Objects;

/**
 * MainActivity
 *
 * LoginActivity 에서 시작됨
 *
 * 닉네임 설정 안되있는 경우
 * SetupActivity 호출
 * Main 에서 하는 이유: 구글 로그인 등 외부 로그인 사용 시 SignUp 을 안 거침
 *
 * */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBotNav();

        //wifi 정보 action bar 표시
        Objects.requireNonNull(getSupportActionBar()).setTitle(WifiState.getSSID());

        //nickname 설정 및 표시
        String nickname = FirebaseUtil.getCurUser().getDisplayName();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.logout:
                Toast.makeText(this,"로그아웃 선택",Toast.LENGTH_SHORT).show();
                break;
            case R.id.goNotification:
                Toast.makeText(this,"알림설정 선택",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), SetupListActivity.class);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void startSetUp() {
        ToastUtil.showText(this, "닉네임을 설정하세요");
        Intent intent = new Intent(this, SetupActivity.class);
        startActivity(intent);
    }

}