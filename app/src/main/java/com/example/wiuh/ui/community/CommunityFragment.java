package com.example.wiuh.ui.community;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wiuh.R;

public class CommunityFragment extends Fragment {

    private final PostAdapter recycleAdapter;

    public CommunityFragment(PostAdapter recycleAdapter) {
        this.recycleAdapter = recycleAdapter;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_community, container, false);


        RecyclerView recyclerView = root.findViewById(R.id.community_recyclerView);
        recyclerView.setAdapter(recycleAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        return root;
    }
}



