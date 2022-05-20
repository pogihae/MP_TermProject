package com.example.wiuh.ui;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class BaseDiffCallback<T> extends DiffUtil.Callback {
    private final List<T> mOldList;
    private final List<T> mNewList;

    public BaseDiffCallback(List<T> mOldList, List<T> mNewList) {
        this.mNewList = mNewList;
        this.mOldList = mOldList;
    }

    @Override
    public int getOldListSize() {
        return mOldList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldList.get(oldItemPosition)
                .equals(mNewList.get(newItemPosition));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }
}
