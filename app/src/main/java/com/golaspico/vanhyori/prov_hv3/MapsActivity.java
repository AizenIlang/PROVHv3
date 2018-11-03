package com.golaspico.vanhyori.prov_hv3;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        // Add a marker in Sydney and move the camera
        LatLng alfonso = new LatLng(14.5901, 121.0846);
        LatLng javil = new LatLng(14.5654, 121.079391);
        LatLng johnf = new LatLng(14.5898, 121.0646);
        LatLng marikina = new LatLng(14.6211, 121.0826);
        LatLng mission = new LatLng(14.5898, 121.0976);
        LatLng pasigcity = new LatLng(14.5725, 121.0992);
        LatLng pasigmedical = new LatLng(14.5938, 121.1015);
        LatLng rizalmedical = new LatLng(14.5642, 121.0659);
        LatLng sabater = new LatLng(14.5600, 121.0829);
        LatLng medicalcity = new LatLng(14.5894, 121.0694);

        mMap.addMarker(new MarkerOptions().position(alfonso).title("Alfonso Specialist Hospital"));
        mMap.addMarker(new MarkerOptions().position(javil).title("Javillonar Clinic and Hospital"));
        mMap.addMarker(new MarkerOptions().position(johnf).title("John F. Cotton Hospital"));
        mMap.addMarker(new MarkerOptions().position(marikina).title("Marikina Doctors Hospital and Medical Center"));
        mMap.addMarker(new MarkerOptions().position(mission).title("Mission Hospital"));
        mMap.addMarker(new MarkerOptions().position(pasigcity).title("Pasig City General Hospital"));
        mMap.addMarker(new MarkerOptions().position(pasigmedical).title("Pasig Medical and Maternity Hospital Foundation"));
        mMap.addMarker(new MarkerOptions().position(rizalmedical).title("Rizal Medical Center "));
        mMap.addMarker(new MarkerOptions().position(sabater).title("Sabater General Hospital"));
        mMap.addMarker(new MarkerOptions().position(medicalcity).title("The Medical City"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(alfonso,13));
    }
}
