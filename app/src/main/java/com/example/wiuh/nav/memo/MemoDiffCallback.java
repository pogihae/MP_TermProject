package com.example.wiuh.nav.memo;

import androidx.recyclerview.widget.DiffUtil;

import com.example.wiuh.model.Memo;

import java.util.List;

public class MemoDiffCallback extends DiffUtil.Callback {
    private final List<Memo> mOldMemoList;
    private final List<Memo> mNewMemoList;

    public MemoDiffCallback(List<Memo> mOldMemoList, List<Memo> mNewMemoList) {
        this.mNewMemoList = mNewMemoList;
        this.mOldMemoList = mOldMemoList;
    }

    @Override
    public int getOldListSize() {
        return mOldMemoList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewMemoList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldMemoList.get(oldItemPosition)
                .equals(mNewMemoList.get(newItemPosition));
    }
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }
}
