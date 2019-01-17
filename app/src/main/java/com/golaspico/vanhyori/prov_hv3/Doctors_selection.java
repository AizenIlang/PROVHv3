package com.golaspico.vanhyori.prov_hv3;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import modules.Doctor;

public class Doctors_selection extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Doctor> arrayList;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_doctors_selection);
        recyclerView = findViewById(R.id.doctors_selection_recyclerview);
        Intent intent = getIntent();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Doctors").child(intent.getStringExtra("Key")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Doctor tempDoc = dataSnapshot.getValue(Doctor.class);
                arrayList.add(tempDoc);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
