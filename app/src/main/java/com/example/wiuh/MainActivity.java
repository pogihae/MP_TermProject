package com.example.wiuh;

import android.content.Intent;
import android.os.Bundle;

import com.example.wiuh.model.User;
import com.example.wiuh.util.FirebaseUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.wiuh.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * */
public class MainActivity extends AppCompatActivity {
    static final int RQ_NICKNAME = 111;

    private FirebaseUser curUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        curUser = FirebaseAuth.getInstance().getCurrentUser();
        setUpUser();

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(R.id.navigation_memo, R.id.navigation_community)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    private void setUpUser() {
        FirebaseUtil.getUserRef()
                .child(curUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User.userSingleTon = snapshot.getValue(User.class);
                        if(User.userSingleTon.hasInitialNickName()) {
                            Intent intent = new Intent(getApplicationContext(), SetupActivity.class);
                            startActivityForResult(intent, RQ_NICKNAME);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RQ_NICKNAME && resultCode == SetupActivity.RS_SUCCESS) {
            String nickname = data.getStringExtra(SetupActivity.NICKNAME);
            Map<String, Object> map = new HashMap<>();
            map.put("nickname", nickname);
            FirebaseUtil.getUserRef().child(curUser.getUid()).updateChildren(map);
            User.userSingleTon.nickname = nickname;
        }
    }

}