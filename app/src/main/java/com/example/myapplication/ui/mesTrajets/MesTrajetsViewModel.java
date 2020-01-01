package com.example.myapplication.ui.mesTrajets;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MesTrajetsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MesTrajetsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is mes trajet fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
