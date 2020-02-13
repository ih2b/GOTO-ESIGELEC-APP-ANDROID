package com.example.myapplication.ui.chercherUnTrajet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.R;

public class ChercherUnTrajetFragment extends Fragment {
    private ChercherUnTrajetViewModel chercherUnTrajetViewModel;
    private ListView mtrajets;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        chercherUnTrajetViewModel =
                ViewModelProviders.of(this).get(ChercherUnTrajetViewModel.class);
        View root = inflater.inflate(R.layout.fragment_chercher_un_trajet , container, false);

        return root;
    }
}
