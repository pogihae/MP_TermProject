package com.example.wiuh.ui.memo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MemoViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MemoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("메모 탭");
    }

    public LiveData<String> getText() {
        return mText;
    }
}