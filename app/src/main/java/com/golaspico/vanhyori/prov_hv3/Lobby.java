package com.golaspico.vanhyori.prov_hv3;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import modules.Hospital;

public class Lobby extends AppCompatActivity {


    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    RecyclerView hospitalRecyclerView;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FloatingActionButton floatingActionButton;
    Button mSearchBtn;
    Button mSpeechBtn;
    EditText mSearchEdit;
    Activity myActivity;
    HospitalAdapter hospitalAdapter;
    Spinner spinner;
    ArrayList<Hospital> hospitalArrayList;
    Context context;
    Activity activity;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    private LatLng myLatlng;


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser temp =mAuth.getCurrentUser();
        if(temp == null){
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        BottomAppBar bottomAppBar = findViewById(R.id.lobby_bottom_appbar);
        setSupportActionBar(bottomAppBar);

        onMapReady();

        context = this;
        activity = this;
        hospitalArrayList = new ArrayList<Hospital>();
        myActivity = this;
        floatingActionButton = findViewById(R.id.lobby_fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Appointments.class);
                startActivity(intent);
            }
        });

        //implementing the recyclerview
        hospitalRecyclerView = findViewById(R.id.lobby_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        hospitalRecyclerView.setLayoutManager(layoutManager);

        hospitalRecyclerView.setItemAnimator(new DefaultItemAnimator());





        //










        final Spinner spinner = (Spinner) findViewById(R.id.lobby_spinner_category);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category, R.layout.spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        mSearchEdit = findViewById(R.id.lobby_search_edit_text);

        mSearchBtn = findViewById(R.id.lobby_search_btn);

        mSpeechBtn = findViewById(R.id.lobby_speech);


        mSearchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                UpdateList(mSearchEdit.getText().toString(),spinner.getSelectedItem().toString());
            }
        });

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchEdit.setText("");


                   UpdateList(mSearchEdit.getText().toString(),spinner.getSelectedItem().toString());


            }
        });


        mSpeechBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSpeechInput();
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lobby_bar_menu,menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId() == R.id.lobby_menu_map){
//            createNotificationChannel();
//        }

        if(item.getItemId() == R.id.lobby_menu_user){
            Intent intent = new Intent(this,UserProfile.class);
            startActivityForResult(intent,10);
        }

        if(item.getItemId() == R.id.lobby_menu_map_nearest){
            Intent intent = new Intent(this,MapsActivity.class);
            startActivity(intent);
        }
        return true;
    }

    public void getSpeechInput(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent,10);
        }else{
            Toast.makeText(this,"Your Device is not supported",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 10 :
                if(data != null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mSearchEdit.setText(result.get(0));

                }

                break;
        }
    }

    private void createNotificationChannel() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,"Hwoarang")
                .setSmallIcon(R.drawable.ic_assignment_black_24dp)
                .setContentTitle("PROV-H")
                .setContentText("Nov 30, 8:00am Alfonso Specialist Hospital")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("PROV-H"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, mBuilder.build());
    }

    public void UpdateList(String search, String option){
        ArrayList<Hospital> myTemp = new ArrayList<Hospital>();



        String input = search.toLowerCase();
        for(Hospital hospital : hospitalArrayList){
            if(option.equals("Name")){
                if(hospital.getName().toLowerCase().contains(input)){
                    myTemp.add(hospital);
                }
            }

            if(option.equals("Services")){
                if(hospital.getServices().toLowerCase().contains(input)){
                    myTemp.add(hospital);
                }
            }

            if(option.equals("Location")){
                if(hospital.getLocation().toLowerCase().contains(input)){
                    myTemp.add(hospital);
                }
            }

        }

        hospitalAdapter.filterList(myTemp);


    }

    public void SortNearest(){
        Collections.sort(hospitalArrayList, new Comparator<Hospital>() {
            @Override
            public int compare(Hospital hospital, Hospital t1) {
                return Integer.valueOf(hospital.getDistance().compareTo(t1.getDistance()));
            }
        });
        for(Hospital hosp : hospitalArrayList){
            Log.e("Distance","The result Sorted Distance : " + hosp.getName() + " : "+ hosp.getDistance());
        }

        hospitalAdapter.filterList(hospitalArrayList);

    }


    public void onMapReady() {
//        mMap = googleMap;




        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
//            mMap.setMyLocationEnabled(true);
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            if(isLocationEnabled(getApplicationContext())){
                try{
                    Task location = fusedLocationProviderClient.getLastLocation();
                    location.addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                Location mylocation = (Location) task.getResult();
                                try{
                                    myLatlng = new LatLng(mylocation.getLatitude(),mylocation.getLongitude());
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

//                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatlng,15));
                                GenerateTheList();

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


    public void GenerateTheList() {
        hospitalAdapter = new HospitalAdapter(context, activity, "", "", hospitalArrayList,false);
        hospitalRecyclerView.setAdapter(hospitalAdapter);

        db.child("Hospitals").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Hospital hospital = dataSnapshot.getValue(Hospital.class);
                String coordinates[] = hospital.getCoordinates().split(",");
                LatLng tempLang = new LatLng(Double.parseDouble(coordinates[0]), Double.parseDouble(coordinates[1]));
//                mMap.addMarker(new MarkerOptions().position(tempLang).title(hospital.getName()).snippet(hospital.getContactNumber()));
                float result[] = new float[10];
                Location.distanceBetween(myLatlng.latitude, myLatlng.longitude, tempLang.latitude, tempLang.longitude, result);
                Log.e("Distance", "The result Distance : " + result[0]);

                //We thn add it to the list
                Hospital tempHospital = dataSnapshot.getValue(Hospital.class);
                tempHospital.setKey(dataSnapshot.getKey());
                tempHospital.setDistance(result[0]);
                hospitalArrayList.add(tempHospital);
                SortNearest();

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

    }






}

