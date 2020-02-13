package com.example.myapplication.ui.profil;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class ProfilFragment extends Fragment {
    private ProfilViewModel profilViewModel;
    private EditText mNomField,mPrenomField, mPhoneField;
    private Button mBack, mConfirm;
    private TextView textView;

    private DatabaseReference mUserDatabase;
    private String userID;
    private String mNom;
    private String mPrenom;
    private String mPhone;
    private String mAdresse;
    private FirebaseAuth mAuth;
    private ImageView mProfileImage;
    private String mProfileImageUrl;
    private Uri resultUri;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        profilViewModel =
                ViewModelProviders.of(this).get(ProfilViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profil, container, false);
       textView = root.findViewById(R.id.text_home);
        mNomField =(EditText) root.findViewById(R.id.nom);
        mPrenomField =(EditText) root.findViewById(R.id.prenom);
        mPhoneField =(EditText) root.findViewById(R.id.phone);

        mBack =(Button) root.findViewById(R.id.back);
        mConfirm=(Button) root.findViewById(R.id.confirm);
        mProfileImage = (ImageView) root.findViewById(R.id.profileImage);
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        getUserInfo();
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInformation();
            }
        });

        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });
        if (ActivityCompat.checkSelfPermission(root.getContext(),ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null) {
                Location lastKnownLocationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lastKnownLocationGPS != null) {
                    Log.d("positions", lastKnownLocationGPS.getLatitude()+" ---- "+lastKnownLocationGPS.getLongitude());
                    getAddressFromLocation(lastKnownLocationGPS.getLatitude(), lastKnownLocationGPS.getLongitude());
                } else {
                    Location loc = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                    Log.d("positions", loc.getLatitude()+" -- "+loc.getLongitude());

                    getAddressFromLocation(loc.getLatitude(), loc.getLongitude());
                }
            }
        }
        else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{ACCESS_FINE_LOCATION}, 100);
            }
        }
mBack.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        mAuth.signOut();
    }
});

        return root;
    }

    private void getUserInfo(){
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("nom")!=null){
                        mNom = map.get("nom").toString();
                        mNomField.setText(mNom);
                    }
                    if(map.get("prenom")!=null){
                        mPrenom = map.get("prenom").toString();
                        mPrenomField.setText(mPrenom);
                    }
                    if(map.get("phone")!=null){
                        mPhone = map.get("phone").toString();
                        mPhoneField.setText(mPhone);
                    }
                    if(map.get("profileImageUrl")!=null){
                        mProfileImageUrl = map.get("profileImageUrl").toString();
                        Glide.with(getActivity()).load(mProfileImageUrl).into(mProfileImage);
                    }
                    if(map.get("adresse")!=null){
                        mAdresse= map.get("adresse").toString();
                        textView.setText(mAdresse);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }



    private void saveUserInformation() {
        mNom = mNomField.getText().toString();
        mPrenom = mPrenomField.getText().toString();
        mPhone = mPhoneField.getText().toString();


        Map userInfo = new HashMap();
        userInfo.put("nom", mNom);
        userInfo.put("prenom",mPrenom);
        userInfo.put("phone", mPhone);
        mUserDatabase.updateChildren(userInfo);
        if(resultUri != null) {

            StorageReference filePath = FirebaseStorage.getInstance().getReference().child("profile_images").child(userID);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = filePath.putBytes(data);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    return;
                }
            });
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getUploadSessionUri();

                    Map newImage = new HashMap();
                    newImage.put("profileImageUrl", downloadUrl.toString());
                    mUserDatabase.updateChildren(newImage);
                    return;
                }
            });
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == getActivity().RESULT_OK){
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            mProfileImage.setImageURI(resultUri);
        }
    }

    private void getAddressFromLocation(double latitude, double longitude) {

        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses.size() > 0) {
                Address fetchedAddress = addresses.get(0);
                StringBuilder stringBuilderAdresse =new StringBuilder("");
                for(int i=0;i<=fetchedAddress.getMaxAddressLineIndex();i++){
                    stringBuilderAdresse.append(fetchedAddress.getAddressLine(i)).append("\n");
                }
                Map userInfo = new HashMap();
                userInfo.put("adresse", stringBuilderAdresse.toString());
                mUserDatabase.updateChildren(userInfo);

            }
        }catch (IOException e){

        }
    }

}
