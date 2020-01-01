package com.example.myapplication.ui.statistiques;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StatistiqueViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public StatistiqueViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is statistique fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
