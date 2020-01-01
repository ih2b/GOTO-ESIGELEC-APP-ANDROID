package com.example.myapplication.ui.evaluerUnTrajet;

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


public class EvaluerUnTrajetFragment extends Fragment {
    private EvaluerUnTrajetViewModel evaluerUnTrajetViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        evaluerUnTrajetViewModel =
                ViewModelProviders.of(this).get(EvaluerUnTrajetViewModel.class);
        View root = inflater.inflate(R.layout.fragment_evaluer_un_trajet , container, false);
        final TextView textView = root.findViewById(R.id.text_evaluer);
        evaluerUnTrajetViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
