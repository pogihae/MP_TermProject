package com.example.wiuh;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.wiuh.model.WifiState;
import com.example.wiuh.nav.community.AddPostActivity;
import com.example.wiuh.nav.community.CommunityFragment;
import com.example.wiuh.nav.memo.AddMemoActivity;
import com.example.wiuh.nav.memo.MemoFragment;
import com.example.wiuh.util.FirebaseUtil;
import com.example.wiuh.util.ToastUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

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
public class BoardActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpBot();

        //wifi 정보 action bar 표시
        Objects.requireNonNull(getSupportActionBar()).setTitle(WifiState.getSSID());

        //nickname 설정 및 표시
        String nickname = FirebaseUtil.getCurUser().getDisplayName();
        if(nickname == null || nickname.matches("")) startSetUp();
        else ToastUtil.showText(this, nickname + " 환영");

        findViewById(R.id.action_a).setOnClickListener(v -> startAddPost());
        findViewById(R.id.action_b).setOnClickListener(v -> startAddMemo());
    }

    private void startAddPost() {
        Intent intent = new Intent(this, AddPostActivity.class);
        startActivity(intent);
    }

    private void startAddMemo() {
        Intent intent = new Intent(this, AddMemoActivity.class);
        startActivity(intent);
    }


    private void setUpBot() {
        TabLayout mTabLayout = findViewById(R.id.sliding_tabs);
        ViewPager2 viewPager2 = findViewById(R.id.viewpager);
        viewPager2.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                if(position == 0) return new MemoFragment();
                else return new CommunityFragment();
            }

            @Override
            public int getItemCount() {
                return 2;
            }
        });

        final String[] tabNames = new String[] {"Memo", "Community"};
        final int[] tabIcons = new int[] {R.drawable.ic_baseline_list_alt_24, R.drawable.ic_baseline_textsms_24};

        new TabLayoutMediator(mTabLayout, viewPager2, (tab, position) -> {
            tab.setText(tabNames[position]);
            tab.setIcon(tabIcons[position]);
        }).attach();
    }


    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemId = item.getItemId();

        if(itemId == R.id.logout) {
            Toast.makeText(this,"로그아웃 선택",Toast.LENGTH_SHORT).show();
            FirebaseUtil.logout(this);
        }
        else if(itemId == R.id.goNotification) {
            Intent intent = new Intent(getApplicationContext(), SetupListActivity.class);
            startActivity(intent);
        }
        else if(itemId == R.id.personalSettings) {
            startActivity(new Intent(this, SetupActivity.class));
        }

        else if(itemId == R.id.WiFiRegister) {
            Toast.makeText(this,"와이파이 등록 선택",Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void startSetUp() {
        ToastUtil.showText(this, "닉네임을 설정하세요");
        Intent intent = new Intent(this, SetupActivity.class);
        startActivity(intent);
    }

}