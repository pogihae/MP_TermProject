package com.example.wiuh.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.wiuh.AddWifi;
import com.example.wiuh.R;
import com.example.wiuh.WifiState;
import com.example.wiuh.activity.memo.AddMemoActivity;
import com.example.wiuh.activity.post.AddPostActivity;
import com.example.wiuh.model.Memo;
import com.example.wiuh.model.Post;
import com.example.wiuh.ui.community.CommunityFragment;
import com.example.wiuh.ui.community.PostAdapter;
import com.example.wiuh.ui.memo.MemoAdapter;
import com.example.wiuh.ui.memo.MemoFragment;
import com.example.wiuh.util.FirebaseUtil;
import com.example.wiuh.util.ToastUtil;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.github.pwittchen.reactivewifi.ReactiveWifi;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * MainActivity
 * <p>
 * LoginActivity 에서 시작됨
 * <p>
 * 닉네임 설정 안되있는 경우
 * SetupActivity 호출
 * Main 에서 하는 이유: 구글 로그인 등 외부 로그인 사용 시 SignUp 을 안 거침
 */
public class BoardActivity extends AppCompatActivity {

    private Map<String, String> ssidToMac = new HashMap<>();
    private String[] dropDownItemArr; //spinner에 표시될 array

    private MemoAdapter memoAdapter;
    private PostAdapter postAdapter;
    private ValueEventListener memoListener;
    private ValueEventListener postListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        init();
        startWifiInfoSubscription();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setUpActionBar();
        setUpBot();

        //nickname 설정 및 표시
        String nickname = FirebaseUtil.getCurUser().getDisplayName();
        if (nickname == null || nickname.matches("")) startSetUp();
        else ToastUtil.showText(this, nickname + " 환영");

        FloatingActionButton btnAddPost = findViewById(R.id.action_a);
        FloatingActionButton btnAddMemo = findViewById(R.id.action_b);

        btnAddPost.setIcon(R.drawable.ic_baseline_list_alt_24);
        btnAddMemo.setIcon(R.drawable.ic_baseline_textsms_24);

        btnAddPost.setOnClickListener(v -> startAddPost());
        btnAddMemo.setOnClickListener(v -> startAddMemo());
    }

    private void setUpActionBar() {
        List<String> starredL = new ArrayList<>();
        FirebaseUtil.getWifiRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    String ssid = ds.getValue(String.class);
                    String mac = ds.getKey();

                    starredL.add(ssid);
                    ssidToMac.put(ssid, mac);
                }
                if(!starredL.contains(WifiState.getSSID()))
                    starredL.add(WifiState.getSSID());

                dropDownItemArr = starredL.toArray(new String[0]);
                setUpListActionBar();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void startAddPost() {
        Intent intent = new Intent(this, AddPostActivity.class);
        startActivity(intent);
    }

    private void startAddMemo() {
        Intent intent = new Intent(this, AddMemoActivity.class);
        startActivity(intent);
    }

    private void setUpListActionBar() {
        ActionBar actionBar = getSupportActionBar();
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter() {
            @Override
            //spinner의 list를 보여주는 view
            public View getDropDownView(int itemIndex, View view, ViewGroup viewGroup) {
                LinearLayout linearLayout = new LinearLayout(BoardActivity.this);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
                linearLayout.setLayoutParams((layoutParams));
                TextView itemTextView = new TextView(BoardActivity.this);
                String itemText = dropDownItemArr[itemIndex];
                itemTextView.setText(itemText);
                itemTextView.setTextSize(20);
                itemTextView.setTextColor(Color.WHITE);
                // Add TextView in return view
                linearLayout.addView(itemTextView, itemIndex);
                return linearLayout;
            }

            @Override
            public void registerDataSetObserver(DataSetObserver dataSetObserver) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

            }

            @Override
            public int getCount() {
                return dropDownItemArr.length;
            }

            @Override
            public Object getItem(int itemIndex) {
                return dropDownItemArr[itemIndex];
            }

            @Override
            public long getItemId(int itemIndex) {
                return itemIndex;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            @Override
            public View getView(int itemIndex, View view, ViewGroup viewGroup) {
                TextView itemTextView = new TextView(BoardActivity.this);
                String itemText = dropDownItemArr[itemIndex];
                itemTextView.setText(itemText);
                itemTextView.setTextSize(20);
                itemTextView.setTextColor(Color.WHITE);
                return itemTextView;
            }

            @Override
            public int getItemViewType(int i) {
                return 0;
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }
        };
        // Set action bar navigation mode to list mode.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        // Set action bar list navigation data and item click listener.
        actionBar.setListNavigationCallbacks(spinnerAdapter, (itemPosition, itemId) -> {
            String SSID = dropDownItemArr[itemPosition];
            //String MAC = ssidToMac.get(SSID);

            //System.out.println(SSID + " " + MAC);

            //WifiState.setInfo(SSID, MAC);
            //FirebaseUtil.setListener(postListener, memoListener);

            String message = "You clicked " + SSID;
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            return true;

        });
    }

    private void startWifiInfoSubscription() {
        ReactiveWifi.observeWifiAccessPointChanges(this)
                .subscribeOn(AndroidSchedulers.from(Looper.myLooper()))
                .subscribe(wifiInfo -> {
                    WifiState.setInfo(wifiInfo.getSSID(), wifiInfo.getBSSID());
                    //wifi 정보 action bar 표시
                    Objects.requireNonNull(getSupportActionBar()).setTitle(WifiState.getSSID());
                    //getSupportActionBar().setDisplayShowTitleEnabled(false);

                    FirebaseUtil.getMemoRef().addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                int memoCnt = 0;
                                for (Object ignored : snapshot.getChildren())
                                    memoCnt++;
                                notifyContent(wifiInfo.getSSID() + "에 " + memoCnt + "개의 메모가 존재해요");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    FirebaseUtil.setListener(postListener, memoListener);
                }).isDisposed();
    }

    private void notifyContent(String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, LoginActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("WIUH")
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, builder.build());
    }

    private void init() {
        memoAdapter = new MemoAdapter(new ArrayList<>(), this);
        postAdapter = new PostAdapter(new ArrayList<>(), this);

        postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Post> list = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Post p = ds.getValue(Post.class);
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
                return new Fragment();
            }

            @Override
            public int getItemCount() {
                return 3;
            }
        });

        //final String[] tabNames = new String[]{"Memo", "Community"};
        final int[] tabIcons = new int[]{
                R.drawable.ic_baseline_textsms_24,
                R.drawable.ic_baseline_list_alt_24
                , R.drawable.ic_home_black_24dp
        };

        new TabLayoutMediator(mTabLayout, viewPager2, (tab, position) -> {
            //tab.setText(tabNames[position]);
            tab.setId(position);
            tab.setIcon(tabIcons[position]);
        }).attach();

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
        } else if (itemId == R.id.goNotification) {
            Intent intent = new Intent(getApplicationContext(), NotificationHistoryActivity.class);
            startActivity(intent);
        } else if (itemId == R.id.personalSettings) {
            startActivity(new Intent(this, SetupActivity.class));
        } else if (itemId == R.id.WiFiRegister) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("WIFI 등록?")
                    .setTitle("WIUH")
                    .setPositiveButton("OK", (dialogInterface, i) -> {
                        String SSID = WifiState.getSSID();
                        String MAC = WifiState.getMAC();

                        FirebaseUtil.getWifiRef().child(MAC).setValue(SSID);
                        ToastUtil.showText(this, "등록 성공");
                    })
                    .setNegativeButton("NO", (dialogInterface, i) -> {
                        ToastUtil.showText(this, "등록 안함");
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
            //startActivity(new Intent(this, AddWifi.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void startSetUp() {
        ToastUtil.showText(this, "닉네임을 설정하세요");
        Intent intent = new Intent(this, SetupActivity.class);
        startActivity(intent);
    }

}