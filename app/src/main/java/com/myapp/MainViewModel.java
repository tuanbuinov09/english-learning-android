package com.myapp;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.myapp.model.TranslationHistory;

public class MainViewModel extends ViewModel {
    //private MutableLiveData<TranslationHistory> mutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> mutableLiveData = new MutableLiveData<>();

//    public void setTranslationHistory(TranslationHistory translationHistory) {
//        mutableLiveData.setValue(translationHistory);
//    }
//
//    public MutableLiveData<TranslationHistory> getTranslationHistory() {
//        return mutableLiveData;
//    }

    public void setText(String s) {
        mutableLiveData.setValue(s);
    }
    public MutableLiveData<String> getText() {
        return mutableLiveData;
    }
}
