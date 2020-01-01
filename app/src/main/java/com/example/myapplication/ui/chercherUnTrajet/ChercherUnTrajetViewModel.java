package com.example.myapplication.ui.chercherUnTrajet;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ChercherUnTrajetViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public ChercherUnTrajetViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is checher un trajet fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
