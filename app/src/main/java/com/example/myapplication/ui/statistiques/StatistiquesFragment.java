package com.example.myapplication.ui.statistiques;

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
import com.example.myapplication.ui.evaluerUnTrajet.EvaluerUnTrajetViewModel;

public class StatistiquesFragment extends Fragment {
    private StatistiqueViewModel statistiqueViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        statistiqueViewModel =
                ViewModelProviders.of(this).get(StatistiqueViewModel.class);
        View root = inflater.inflate(R.layout.fragmenet_statistique , container, false);
        final TextView textView = root.findViewById(R.id.text_stati);
        statistiqueViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
