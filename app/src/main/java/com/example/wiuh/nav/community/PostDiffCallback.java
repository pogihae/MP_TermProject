package com.example.wiuh.nav.community;

import androidx.recyclerview.widget.DiffUtil;

import com.example.wiuh.model.Post;

import java.util.List;

public class PostDiffCallback extends DiffUtil.Callback {
    private final List<Post> mOldPostList;
    private final List<Post> mNewPostList;

    public PostDiffCallback(List<Post> mOldPostList, List<Post> mNewPostList) {
        this.mNewPostList = mNewPostList;
        this.mOldPostList = mOldPostList;
    }

    @Override
    public int getOldListSize() {
        return mOldPostList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewPostList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldPostList.get(oldItemPosition)
                .equals(mNewPostList.get(newItemPosition));
    }
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }
}