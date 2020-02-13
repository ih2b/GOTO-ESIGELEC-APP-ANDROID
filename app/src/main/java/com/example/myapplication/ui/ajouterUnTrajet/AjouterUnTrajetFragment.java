package com.example.myapplication.ui.ajouterUnTrajet;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

//import com.example.myapplication.PlaceAutocompleteAdapter;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.myapplication.R;
import com.google.android.gms.common.ConnectionResult;

import com.google.android.gms.common.api.GoogleApiClient;


import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AjouterUnTrajetFragment extends Fragment implements OnMapReadyCallback, RoutingListener {
    public static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private AjouterUnTrajetViewModel ajouterUnTrajetViewModel;
    private static final LatLngBounds LAT_LNG_BOUNDS =new LatLngBounds(
            new LatLng(-40,-168),new LatLng(71,136)
    );
    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.quantum_googblue};
    public MapView mMapView;
    private CheckBox mautoroute;
    private NumberPicker mContribution,mRetard,mnbplace;
    private TextView mContributiontext,mAutoroutetext,mDatetext,mHeuretext,mplacetext,mRetardtext,mMoyen;
    private TimePicker mHeure;
    private DatePicker mDate;
    private Spinner spinner;
    private  AutocompleteSupportFragment autocompleteSupportFragment;
    ArrayAdapter<String> adapter;
    ArrayList<String> spinnerDataList;
    private Button majouter,mconfirm;
    private LatLng latLng;
    public GoogleMap mMap;
    private DatabaseReference mTrajetDatabase;
    DatabaseReference databaseReference;
    ValueEventListener listener;
    private FirebaseAuth mAuth;

    private String userID;
    PlacesClient placesClient;
//    private GeoDataClient mGoogleApiClient;
//    private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        polylines=new ArrayList<>();
//        SupportMapFragment mapFragment =
//                (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map);
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        mTrajetDatabase = FirebaseDatabase.getInstance().getReference().child("Trajet");
        ajouterUnTrajetViewModel =
                ViewModelProviders.of(this).get(AjouterUnTrajetViewModel.class);
        View root = inflater.inflate(R.layout.fragment_ajouter_un_trajet, container, false);

        spinner =(Spinner) root.findViewById(R.id.moyenSpinner);
        spinnerDataList =new ArrayList<>();
        majouter =(Button) root.findViewById(R.id.ajouter) ;
        mconfirm =(Button) root.findViewById(R.id.confirm) ;
        mautoroute =(CheckBox) root.findViewById(R.id.autoroute) ;
        mContribution =(NumberPicker) root.findViewById(R.id.contribution);
        mnbplace =(NumberPicker) root.findViewById(R.id.nbrplace);
        mRetard =(NumberPicker) root.findViewById(R.id.retard);
        mContributiontext=(TextView) root.findViewById(R.id.contributiontext);
        mplacetext=(TextView) root.findViewById(R.id.nbrplacetext);
        mAutoroutetext=(TextView) root.findViewById(R.id.autoroutetext);
        mMoyen=(TextView) root.findViewById(R.id.Moyen);
        mHeuretext=(TextView) root.findViewById(R.id.Heuretext);
        mDatetext=(TextView) root.findViewById(R.id.Datetext);
        mHeure=(TimePicker) root.findViewById(R.id.Heure);
        mDate=(DatePicker) root.findViewById(R.id.Date);
        mAutoroutetext=(TextView) root.findViewById(R.id.autoroutetext);
        mRetardtext=(TextView) root.findViewById(R.id.retardtext);
        mMapView = (MapView) root.findViewById(R.id.map);
        mconfirm.setVisibility(View.GONE);

        majouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMapView.setVisibility(View.VISIBLE);
                mconfirm.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(),latLng.toString(),Toast.LENGTH_SHORT).show();
                majouter.setVisibility(View.GONE);
                mautoroute.setVisibility(View.GONE);
                mContribution.setVisibility(View.GONE);
                mContributiontext.setVisibility(View.GONE);
                mAutoroutetext.setVisibility(View.GONE);
                mHeure.setVisibility(View.GONE);
                mHeuretext.setVisibility(View.GONE);
                mDate.setVisibility(View.GONE);
                mDatetext.setVisibility(View.GONE);
                spinner.setVisibility(View.GONE);
                mRetard.setVisibility(View.GONE);
                mRetardtext.setVisibility(View.GONE);
                mMoyen.setVisibility(View.GONE);
                mplacetext.setVisibility(View.GONE);
                mnbplace.setVisibility(View.GONE);
            }
        });
        mRetard.setMinValue(5);
        mRetard.setMaxValue(30);
        mnbplace.setMinValue(1);
        mnbplace.setMaxValue(20);
        mContribution.setMinValue(1);
        mContribution.setMaxValue(100);
        mconfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                saveAjouterTrajet();
            }
        });
        //placeAutocompleteAdapter =new PlaceAutocompleteAdapter(getContext(),,LAT_LNG_BOUNDS,null);
        FirebaseDatabase.getInstance().getReference().child("MoyenDeTransport").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot item:dataSnapshot.getChildren()){
                    spinnerDataList.add(item.getValue().toString());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
         adapter =new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,spinnerDataList);
        spinner.setAdapter(adapter);
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView.onCreate(mapViewBundle);
        latLng = new LatLng(49.3919904,1.0797802);
        mMapView.getMapAsync(this);
        mMapView.setVisibility(View.GONE);
if(!Places.isInitialized()){
    Places.initialize(getContext(),"AIzaSyDyVMPGVZ-uunm4JNJaNv3-dKxXNPvCSmY");
}
        placesClient = Places.createClient(getContext());


        autocompleteSupportFragment=(AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID,Place.Field.LAT_LNG,Place.Field.NAME));
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                latLng =place.getLatLng();


            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinner.getSelectedItem().toString().equals("Vehicule")){
                    mautoroute.setVisibility(View.VISIBLE);
                    mContribution.setVisibility(View.VISIBLE);
                    mContributiontext.setVisibility(View.VISIBLE);
                    mAutoroutetext.setVisibility(View.VISIBLE);
                }else {
                    mContribution.setVisibility(View.GONE);
                    mContributiontext.setVisibility(View.GONE);
                    mAutoroutetext.setVisibility(View.GONE);
                    mautoroute.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }
    );
        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        mMap.addMarker(new MarkerOptions().position(new LatLng(49.3832747, 1.0725068)).title("Esigelec"));
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.setMyLocationEnabled(true);
        getRouteToMarker();
    }
    public void getRouteToMarker(){
        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(false)
                .key("AIzaSyDyVMPGVZ-uunm4JNJaNv3-dKxXNPvCSmY")
                .waypoints(new LatLng(49.3919904,1.0797802),new LatLng(49.3919904,1.0785802), new LatLng(49.3832747, 1.0725068))
                .build();
        routing.execute();

    }
    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    @Override
    public void onRoutingFailure(RouteException e) {
        if(e != null) {
            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(getContext(), "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        if(polylines.size()>0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i <route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);

            Toast.makeText(getContext(),"Route "+ (i+1) +": distance - "+ route.get(i).getDistanceValue()+": duration - "+ route.get(i).getDurationValue(),Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRoutingCancelled() {

    }


    public void saveAjouterTrajet(){
        String inspinner =spinner.getSelectedItem().toString();
        String inpointdedepartlatitude =String.valueOf(latLng.latitude);
        String inpointdedepartlong =String.valueOf(latLng.longitude);
        Date D =new Date(mDate.getDayOfMonth(),mDate.getMonth(),mDate.getYear());
        String indate = String.valueOf(D);
        String inheure =String.valueOf(mHeure.getCurrentHour());
        String inretard =String.valueOf(mRetard.getValue());
        String inplace =String.valueOf(mnbplace.getValue());
        String incontr =String.valueOf(mContribution.getValue());
        String inautoroute;
        if(mautoroute.isChecked()){ inautoroute="autoroute";}else { inautoroute ="";}
        Map trajetInfo = new HashMap();
        trajetInfo.put("user", userID);
        trajetInfo.put("moyenTransport", inspinner);
        trajetInfo.put("ptDepartlat",latLng.latitude);
        trajetInfo.put("ptDepartlong", latLng.longitude);
        trajetInfo.put("date", indate);
        trajetInfo.put("heure",inheure);
        trajetInfo.put("retard", inretard);
        trajetInfo.put("nbrplace",inplace);
        trajetInfo.put("contribution", incontr);
        trajetInfo.put("typetrajet",inautoroute);
        mTrajetDatabase.push().setValue(trajetInfo);
    }

}