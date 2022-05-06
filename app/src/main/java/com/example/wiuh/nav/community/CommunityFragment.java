package com.example.wiuh.nav.community;

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
import com.example.wiuh.model.Post;
import com.example.wiuh.util.FirebaseUtil;
import com.example.wiuh.util.ToastUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CommunityFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_community, container, false);

        PostAdapter recycleAdapter = new PostAdapter(new ArrayList<>());
        recycleAdapter.setContext(getContext());

        RecyclerView recyclerView = root.findViewById(R.id.community_recyclerView);
        recyclerView.setAdapter(recycleAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseUtil.getPostRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Post> list = new ArrayList<>();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    Post p = ds.getValue(Post.class);
                    p.setKey(ds.getKey());
                    list.add(p);
                }
                recycleAdapter.updateList(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                ToastUtil.showText(getContext(), error.getMessage());
            }
        });

        root.findViewById(R.id.btn_addPost).setOnClickListener(v -> startAddPost());

        return root;
    }

    private void startAddPost() {
        Intent intent = new Intent(getContext(), AddPostActivity.class);
        startActivity(intent);
    }
}




