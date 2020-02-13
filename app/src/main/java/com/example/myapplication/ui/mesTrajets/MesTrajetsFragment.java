package com.example.myapplication.ui.mesTrajets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class MesTrajetsFragment extends Fragment {
    private  MesTrajetsViewModel mesTrajetsViewModel;
    private ListView mtrajets;
    private DatabaseReference mTrajetDatabase;
    private String A;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mesTrajetsViewModel =
                ViewModelProviders.of(this).get(MesTrajetsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_mes_trajet , container, false);
        mtrajets =(ListView) root.findViewById(R.id.trajet);
        mTrajetDatabase = FirebaseDatabase.getInstance().getReference().child("Trajet");
        final ArrayList<String> arrayList =new ArrayList<>();
        arrayList.add("Vehicule,1 Place du Maréchal Foch, 76037 Rouen, depart 8h");
        arrayList.add("Velo,Palais de justice à Rouen, France, 76037 Rouen, depart 9h");
        arrayList.add("Vehicule,Place du Général de Gaulle, 76037 Rouen, depart 8h");
        ArrayAdapter arrayAdapter=new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,arrayList);
        mtrajets.setAdapter(arrayAdapter);
        return root;
    }
}
