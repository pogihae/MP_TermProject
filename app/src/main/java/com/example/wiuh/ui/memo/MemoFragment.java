package com.example.wiuh.ui.memo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.wiuh.R;

public class MemoFragment extends Fragment {
    private final MemoAdapter recycleAdapter;

    public MemoFragment(MemoAdapter recycleAdapter) {
        this.recycleAdapter = recycleAdapter;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_memo, container, false);


        RecyclerView recyclerView = root.findViewById(R.id.memo_recyclerview);
        recyclerView.setHasFixedSize(true);

        StaggeredGridLayoutManager staggeredGridLayoutManager;
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        recyclerView.setAdapter(recycleAdapter);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);


        return root;
    }


}