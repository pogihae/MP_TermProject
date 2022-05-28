package com.example.wiuh.ui.setup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.wiuh.R;
import com.example.wiuh.util.FirebaseUtil;
import com.example.wiuh.util.ToastUtil;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SetupFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_personal_setup, container, false);
        FirebaseUser curUser = FirebaseUtil.getCurUser();
        String nick = curUser.getDisplayName();

        EditText nickname = root.findViewById(R.id.nickname);
        nickname.setText(nick);

        Button editNickname = root.findViewById(R.id.editNickname);
        TextView logoutTextView = root.findViewById(R.id.logoutTextview);

        editNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nick = nickname.getText().toString();
                if (nick.matches("")) {
                    ToastUtil.showText(getContext(), "닉네임을 입력하세요");
                    return;
                }
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(nick)
                        .build();
                FirebaseUtil.getCurUser().updateProfile(profileUpdates);
                ToastUtil.showText(getContext(),"닉네임 변경 완료");
            }
        });
        logoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUtil.logout(getContext());
                ToastUtil.showText(getContext(),"로그아웃");
            }
        });
        return root;
    }
}
