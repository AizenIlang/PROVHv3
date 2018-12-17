package com.golaspico.vanhyori.prov_hv3;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.Manifest;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import modules.Hospital;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FusedLocationProviderClient fusedLocationProviderClient;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        recyclerView = findViewById(R.id.maps_recycerview);
        recyclerView.setAdapter(new HospitalAdapter(this,this,"",""));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        databaseReference.child("Hospitals").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Hospital hospital = dataSnapshot.getValue(Hospital.class);
                String coordinates[] = hospital.getCoordinates().split(",");
                LatLng tempLang = new LatLng(Double.parseDouble(coordinates[0]),Double.parseDouble(coordinates[1]));
                mMap.addMarker(new MarkerOptions().position(tempLang).title(hospital.getName()).snippet(hospital.getContactNumber()));

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            if(isLocationEnabled(getApplicationContext())){
                try{
                    Task location = fusedLocationProviderClient.getLastLocation();
                    location.addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                Location mylocation = (Location) task.getResult();
                                LatLng myLatlng = new LatLng(mylocation.getLatitude(),mylocation.getLongitude());
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatlng,15));
                            }else{

                            }
                        }
                    });
                }catch (Exception e){

                }
            }

        } else {
            // Show rationale and request permission.
        }
//        // Add a marker in Sydney and move the camera
        LatLng alfonso = new LatLng(14.5901, 121.0846);
//        LatLng javil = new LatLng(14.5654, 121.079391);
//        LatLng johnf = new LatLng(14.5898, 121.0646);
//        LatLng marikina = new LatLng(14.6211, 121.0826);
//        LatLng mission = new LatLng(14.5898, 121.0976);
//        LatLng pasigcity = new LatLng(14.5725, 121.0992);
//        LatLng pasigmedical = new LatLng(14.5938, 121.1015);
//        LatLng rizalmedical = new LatLng(14.5642, 121.0659);
//        LatLng sabater = new LatLng(14.5600, 121.0829);
//        LatLng medicalcity = new LatLng(14.5894, 121.0694);
//
//        mMap.addMarker(new MarkerOptions().position(alfonso).title("Alfonso Specialist Hospital").snippet("(02) 916 2011"));
//        mMap.addMarker(new MarkerOptions().position(javil).title("Javillonar Clinic and Hospital").snippet("841-2303"));
//        mMap.addMarker(new MarkerOptions().position(johnf).title("John F. Cotton Hospital").snippet("02-6328796"));
//        mMap.addMarker(new MarkerOptions().position(marikina).title("Marikina Doctors Hospital and Medical Center").snippet("535-3837"));
//        mMap.addMarker(new MarkerOptions().position(mission).title("Mission Hospital").snippet("656-7906"));
//        mMap.addMarker(new MarkerOptions().position(pasigcity).title("Pasig City General Hospital").snippet("02-6427379"));
//        mMap.addMarker(new MarkerOptions().position(pasigmedical).title("Pasig Medical and Maternity Hospital Foundation").snippet("(02) 865 8400"));
//        mMap.addMarker(new MarkerOptions().position(rizalmedical).title("Rizal Medical Center ").snippet("(02) 641 8194"));
//        mMap.addMarker(new MarkerOptions().position(sabater).title("Sabater General Hospital").snippet("(632) 988-1000"));
//        mMap.addMarker(new MarkerOptions().position(medicalcity).title("The Medical City").snippet("275-9752"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(alfonso,13));


    }


    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }






}
