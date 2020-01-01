package com.example.myapplication.ui.ajouterUnTrajet;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AjouterUnTrajetViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AjouterUnTrajetViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Ajouter un trajet fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
