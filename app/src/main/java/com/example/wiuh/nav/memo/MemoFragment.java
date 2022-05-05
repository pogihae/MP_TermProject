package com.example.wiuh.nav.memo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wiuh.R;
import com.example.wiuh.model.Memo;
import com.example.wiuh.util.FirebaseUtil;
import com.example.wiuh.util.ToastUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MemoFragment extends Fragment {
    private MemoAdapter recycleAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_memo, container, false);

        recycleAdapter = new MemoAdapter(new ArrayList<>());
        recycleAdapter.setContext(getContext());

        RecyclerView recyclerView = root.findViewById(R.id.memo_recyclerview);
        recyclerView.setAdapter(recycleAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseUtil.getMemoRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Memo> list = new ArrayList<>();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    Memo m = ds.getValue(Memo.class);
                    m.setKey(ds.getKey());

                    list.add(m);

                    recycleAdapter.updateList(list);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                ToastUtil.showText(getContext(), error.getMessage());
            }
        });

        root.findViewById(R.id.addMemo_btn).setOnClickListener(v -> startAddMemo());

        return root;
    }

    private void startAddMemo() {
        Intent intent = new Intent(getContext(), AddMemoActivity.class);
        startActivity(intent);
    }
}