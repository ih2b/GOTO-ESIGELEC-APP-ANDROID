package com.example.myapplication.ui.ajouterUnTrajet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.R;

public class AjouterUnTrajetFragment extends Fragment {
    private  AjouterUnTrajetViewModel ajouterUnTrajetViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ajouterUnTrajetViewModel =
                ViewModelProviders.of(this).get(AjouterUnTrajetViewModel.class);
        View root = inflater.inflate(R.layout.fragment_ajouter_un_trajet , container, false);
        final TextView textView = root.findViewById(R.id.text_ajouter);
        ajouterUnTrajetViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
