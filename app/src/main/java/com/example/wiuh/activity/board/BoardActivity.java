package com.example.wiuh.activity.board;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.wiuh.R;
import com.example.wiuh.WifiInfo;
import com.example.wiuh.activity.board.memo.AddMemoActivity;
import com.example.wiuh.activity.board.post.AddPostActivity;
import com.example.wiuh.activity.setup.PersonalSetupActivity;
import com.example.wiuh.activity.setup.SetupActivity;
import com.example.wiuh.model.Memo;
import com.example.wiuh.model.Post;
import com.example.wiuh.ui.community.CommunityFragment;
import com.example.wiuh.ui.community.PostAdapter;
import com.example.wiuh.ui.memo.MemoAdapter;
import com.example.wiuh.ui.memo.MemoFragment;
import com.example.wiuh.ui.setup.SetupFragment;
import com.example.wiuh.util.FirebaseUtil;
import com.example.wiuh.util.ToastUtil;
import com.example.wiuh.util.WifiWorker;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.tiper.MaterialSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/*
 * MainActivity
 * <p>
 * LoginActivity 에서 시작됨
 * <p>
 * 닉네임 설정 안되있는 경우
 * SetupActivity 호출
 * Main 에서 하는 이유: 구글 로그인 등 외부 로그인 사용 시 SignUp 을 안 거침
 */
public class BoardActivity extends AppCompatActivity {

    MaterialSpinner spinner;
    private Map<String, String> ssidToMac;          //ssid로 mac 주소 획득
    private List<String> spinnerList;                  //spinner에 표시될 ssid array
    private ArrayAdapter<String> spinnerAdapter;
    private MemoAdapter memoAdapter;
    private PostAdapter postAdapter;
    private ValueEventListener memoListener;
    private ValueEventListener postListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        memoAdapter = new MemoAdapter(new ArrayList<>(), this);
        postAdapter = new PostAdapter(new ArrayList<>(), this);
        spinnerList = new ArrayList<>();
        ssidToMac = new HashMap<>();
        spinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, spinnerList
        );

        initListener();                 //firebase listener init
        startWifiInfoSubscription();    //observe wifi information change
        setUpSSIDList();                //get starred SSID list from DB
        setUpBot();                     //set up navigation bar
        setUpSpinner();

        //nickname 설정 및 표시
        String nickname = FirebaseUtil.getCurUser().getDisplayName();
        if (nickname == null || nickname.matches("")) startSetUpActivity();
        else ToastUtil.showText(this, nickname + " 님 환영합니다");

        FloatingActionButton btnAddPost = findViewById(R.id.action_a);
        FloatingActionButton btnAddMemo = findViewById(R.id.action_b);

        btnAddPost.setIcon(R.drawable.ic_baseline_list_alt_24);
        btnAddMemo.setIcon(R.drawable.ic_baseline_textsms_24);

        btnAddPost.setOnClickListener(v -> startAddPost());
        btnAddMemo.setOnClickListener(v -> startAddMemo());
    }

    private void initListener() {
        postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Post> list = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Post p = ds.getValue(Post.class);
                    assert p != null;
                    p.setKey(ds.getKey());
                    list.add(p);
                }
                postAdapter.updateList(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };

        memoListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Memo> list = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Memo m = ds.getValue(Memo.class);
                    assert m != null;
                    m.setKey(ds.getKey());
                    list.add(m);
                }
                memoAdapter.updateList(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
    }

    private void startWifiInfoSubscription() {
        WorkManager workManager = WorkManager.getInstance(getApplicationContext());

        Data.Builder userInfo = new Data.Builder();
        userInfo.putString("USER", FirebaseUtil.getCurUser().getUid());

        PeriodicWorkRequest pr = new PeriodicWorkRequest
                .Builder(WifiWorker.class, 15, TimeUnit.MINUTES)
                .setInputData(userInfo.build())
                .setConstraints(
                        new Constraints.Builder()
                                .setRequiredNetworkType(NetworkType.CONNECTED)
                                .setRequiredNetworkType(NetworkType.UNMETERED)
                                .build())
                .build();
        workManager.enqueueUniquePeriodicWork("WifiWorker",
                ExistingPeriodicWorkPolicy.REPLACE, pr);
    }

    /*set up*/

    private void setUpSSIDList() {
        List<String> tmpList = new ArrayList<>();
        FirebaseUtil.getWifiRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    tmpList.add(ds.getValue(String.class));
                    ssidToMac.put(ds.getValue(String.class), ds.getKey());
                }
                spinnerList.addAll(
                        tmpList.stream()
                                .filter(ssid -> !spinnerList.contains(ssid))
                                .collect(Collectors.toList())
                );

                spinnerAdapter.notifyDataSetChanged();
                spinner.setSelection(0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void setUpBot() {
        TabLayout mTabLayout = findViewById(R.id.sliding_tabs);
        ViewPager2 viewPager2 = findViewById(R.id.viewpager);

        //스와이프 탭 전환
        viewPager2.setUserInputEnabled(false);

        viewPager2.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                if (position == 0) return new MemoFragment(memoAdapter);
                else if (position == 1) return new CommunityFragment(postAdapter);
                else return new SetupFragment();
            }

            @Override
            public int getItemCount() {
                return 3;
            }
        });

        //final String[] tabNames = new String[]{"Memo", "Community"};
        final int[] tabIcons = new int[]{
                R.drawable.ic_baseline_textsms_24,
                R.drawable.ic_baseline_list_alt_24,
                R.drawable.ic_home_black_24dp
        };

        new TabLayoutMediator(mTabLayout, viewPager2, (tab, position) -> {
            //tab.setText(tabNames[position]);
            tab.setId(position);
            tab.setIcon(tabIcons[position]);
        }).attach();

    }

    private void setUpSpinner() {
        Toolbar toolbar = findViewById(R.id.toolbar);  //toolbar(커스텀)를 actionbar로 만듦
        setSupportActionBar(toolbar);                 //actionbar에 toolbar대입

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowCustomEnabled(true);  //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false);  //원래 title 숨기기

        spinner = findViewById(R.id.spinner);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        //스피너 객체에다가 어댑터 넣어줌
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NonNull MaterialSpinner materialSpinner, @Nullable View view, int i, long l) {
                String ssid = spinnerList.get(i);
                String mac = ssidToMac.get(ssid);

                WifiInfo.setInfo(ssid, mac);
                FirebaseUtil.setListener(postListener, memoListener);
                Snackbar.make(materialSpinner, "Select " + ssid, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(@NonNull MaterialSpinner materialSpinner) {
                Snackbar.make(materialSpinner, "No SSID selected", Snackbar.LENGTH_LONG).show();
            }
        });
    }


    /*start activity*/

    private void startAddPost() {
        Intent intent = new Intent(this, AddPostActivity.class);
        startActivity(intent);
    }

    private void startAddMemo() {
        Intent intent = new Intent(this, AddMemoActivity.class);
        startActivity(intent);
    }

    private void startSetUpActivity() {
        ToastUtil.showText(this, "닉네임을 설정하세요");
        Intent intent = new Intent(this, SetupActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.logout) {
            FirebaseUtil.logout(this);
        } else if (itemId == R.id.personalSettings) {
            Intent intent = new Intent(this, PersonalSetupActivity.class);
            startActivity(intent);
        } else if (itemId == R.id.WiFiRegister) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("WIFI 등록?")
                    .setTitle("WIUH")
                    .setPositiveButton("OK", (dialogInterface, i) -> {
                        String SSID = WifiInfo.getSSID();
                        String MAC = WifiInfo.getMAC();

                        FirebaseUtil.getWifiRef().child(MAC).setValue(SSID);
                        ToastUtil.showText(this, "등록 성공");
                    })
                    .setNegativeButton("NO", (dialogInterface, i) ->
                            ToastUtil.showText(this, "등록 안함"));
            AlertDialog dialog = builder.create();
            dialog.show();
            //startActivity(new Intent(this, AddWifi.class));
        }

        return super.onOptionsItemSelected(item);
    }

}