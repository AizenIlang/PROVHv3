package com.golaspico.vanhyori.prov_hv3;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
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
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import modules.DirectionsJSONParser;
import modules.Hospital;
import retrofit2.http.Url;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FusedLocationProviderClient fusedLocationProviderClient;
    private HospitalAdapter hospitalAdapter;

    private RecyclerView recyclerView;
    private ArrayList<Hospital> hospitalArrayList;
    private Context context;
    private Activity activity;
    private DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    private LatLng myLatlng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        context = this;
        activity = this;
        hospitalArrayList = new ArrayList<Hospital>();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        recyclerView = findViewById(R.id.maps_recycerview);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());





    }

    public void GenerateTheList(){
        hospitalAdapter = new HospitalAdapter(context,activity,"","",hospitalArrayList,true);
        recyclerView.setAdapter(hospitalAdapter);

        databaseReference.child("Hospitals").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Hospital hospital = dataSnapshot.getValue(Hospital.class);
                String coordinates[] = hospital.getCoordinates().split(",");
                LatLng tempLang = new LatLng(Double.parseDouble(coordinates[0]),Double.parseDouble(coordinates[1]));
                mMap.addMarker(new MarkerOptions().position(tempLang).title(hospital.getName()).snippet(hospital.getContactNumber()));
                float result[] = new float[10];
                Location.distanceBetween(myLatlng.latitude,myLatlng.longitude,tempLang.latitude,tempLang.longitude,result);


                Log.e("Distance","The result Distance : " +result[0]);

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



//        db.child("Hospitals").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot ds : dataSnapshot.getChildren()){
//                    Hospital tempHospital = ds.getValue(Hospital.class);
//                    tempHospital.setKey(ds.getKey());
//
//                    hospitalArrayList.add(tempHospital);
//                    hospitalAdapter = new HospitalAdapter(context,activity,"","",hospitalArrayList);
//
//
//                }
//                recyclerView.setAdapter(hospitalAdapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

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
                                myLatlng = new LatLng(mylocation.getLatitude(),mylocation.getLongitude());
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatlng,15));
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


    @Override
    public boolean onMarkerClick(Marker marker) {

        String url = getRequestUrl(myLatlng,marker.getPosition());
        Log.e("MapDirection","TheMarker got Clicked");
        TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
        taskRequestDirections.execute(url);
        return false;
    }

    private String getRequestUrl(LatLng origin, LatLng dest){
        String str_org = "origin" +  origin.latitude + "," + origin.longitude;
        String str_dest = "destination" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor-false";
        String mode = "mode-driving";
        String param = str_org + "&" + str_dest + "&" +sensor + "&"+mode;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/directions/"+output+"?"+param;
        return url;
    }

    private String requestDirection(String reqUrl){
        String responseString ="";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection =null;
        try{
            URL url = new URL(reqUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuffer stringBuffer= new StringBuffer();
            String line= "";
            while((line = bufferedReader.readLine()) != null){
                stringBuffer.append(line);
            }

            responseString = stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();

        }catch (Exception e){
            e.printStackTrace();
        } finally {
            if(inputStream != null){
                try{
                    inputStream.close();
                }catch (Exception e){

                }

            }
            httpURLConnection.disconnect();
        }
        return responseString;
    }


    public class TaskRequestDirections extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";
            try{
                responseString = requestDirection(strings[0]);
            }catch (Exception e){
                e.printStackTrace();
            }

            return responseString;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //We get Json
            TaskParser taskParser = new TaskParser();
            taskParser.execute(s);
        }
    }

    public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String,String>>> >{
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject = null;
            List<List<HashMap<String,String>>> routes = null;

            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionsJSONParser directionsJSONParser = new DirectionsJSONParser();
                routes = directionsJSONParser.parse(jsonObject);
            }catch (Exception e){

            }
            return routes;

        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            //Get list and display on map

            ArrayList points = null;
            PolylineOptions polylineOptions = null;

            for(List<HashMap<String,String>> path: lists){
                points = new ArrayList();
                polylineOptions = new PolylineOptions();
                for (HashMap<String,String> point : path){
                    double lat = Double.parseDouble(point.get("lat"));
                    double lon = Double.parseDouble(point.get("lon"));

                    points.add(new LatLng(lat,lon));
                }
                polylineOptions.addAll(points);
                polylineOptions.width(15);
                polylineOptions.color(Color.BLUE);
                polylineOptions.geodesic(true);
            }

            if(polylineOptions != null){
                mMap.addPolyline(polylineOptions);
            }else{
                Toast.makeText(getApplicationContext(),"Direction not found",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
