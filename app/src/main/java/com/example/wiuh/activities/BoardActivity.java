package com.example.wiuh.activities;

import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.wiuh.R;
import com.example.wiuh.model.WifiState;
import com.example.wiuh.ui.community.AddPostActivity;
import com.example.wiuh.ui.community.CommunityFragment;
import com.example.wiuh.ui.memo.AddMemoActivity;
import com.example.wiuh.ui.memo.MemoFragment;
import com.example.wiuh.util.FirebaseUtil;
import com.example.wiuh.util.ToastUtil;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

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
    //spinner에 표시될 array
    private String dropDownItemArr[]={"123","456","789"};
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpBot();
        actionBar=getSupportActionBar();
        //wifi 정보 action bar 표시
        Objects.requireNonNull(getSupportActionBar()).setTitle(WifiState.getSSID());
        SpinnerAdapter spinnerAdapter=new SpinnerAdapter() {
            @Override
            //spinner의 list를 보여주는 view
            public View getDropDownView(int itemIndex, View view, ViewGroup viewGroup) {
                LinearLayout linearLayout=new LinearLayout(BoardActivity.this);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
                linearLayout.setLayoutParams((layoutParams));
                TextView itemTextView = new TextView(BoardActivity.this);
                String itemText = dropDownItemArr[itemIndex];
                itemTextView.setText(itemText);
                itemTextView.setTextSize(20);
                itemTextView.setTextColor(Color.WHITE);
                // Add TextView in return view
                linearLayout.addView(itemTextView, 1);
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
        actionBar.setListNavigationCallbacks(spinnerAdapter, new ActionBar.OnNavigationListener() {
                    @Override
                    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                        String menuItemText = dropDownItemArr[itemPosition];
                        String message = "You clicked " + menuItemText;
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        return true;

                    }
                });



        //nickname 설정 및 표시
        String nickname = FirebaseUtil.getCurUser().getDisplayName();
        if (nickname == null || nickname.matches("")) startSetUp();
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
        //스와이프 탭 전환
        viewPager2.setUserInputEnabled(false);
        viewPager2.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                if (position == 0) return new MemoFragment();
                else if (position == 1) return new CommunityFragment();
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
            startActivity(new Intent(this, PersonalSetupActivity.class));
        } else if (itemId == R.id.WiFiRegister) {
            startActivity(new Intent(this, PersonalSetupActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void startSetUp() {
        ToastUtil.showText(this, "닉네임을 설정하세요");
        Intent intent = new Intent(this, SetupActivity.class);
        startActivity(intent);
    }

}