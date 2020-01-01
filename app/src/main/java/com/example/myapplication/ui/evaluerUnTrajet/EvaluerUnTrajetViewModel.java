package com.example.myapplication.ui.evaluerUnTrajet;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EvaluerUnTrajetViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public EvaluerUnTrajetViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Evaluation fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}

