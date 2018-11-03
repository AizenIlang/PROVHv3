package com.golaspico.vanhyori.prov_hv3;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.zip.Inflater;

import modules.Appointment;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.AppointmentsViewHolder> {

  private TextView Name;
  private TextView Date;
  private TextView Status;
  private TextView Type;
  private Context context;
  private LayoutInflater inflater;
  private ArrayList<Appointment> appointmentArrayList = new ArrayList<Appointment>();
  private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
  private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public AppointmentsAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);

        databaseReference.child("UserAppointments").child(firebaseAuth.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Appointment tempAppointment = dataSnapshot.getValue(Appointment.class);
                appointmentArrayList.add(tempAppointment);
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

    @NonNull
    @Override
    public AppointmentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.comment_list_item,parent,false);
        AppointmentsViewHolder holder = new AppointmentsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentsViewHolder holder, int position) {
        Appointment temp = appointmentArrayList.get(position);
        Name.setText(temp.getHospitalName());
        Date.setText(temp.getDate());
        Type.setText(temp.getType());
        Status.setText(temp.getStatus());
    }

    @Override
    public int getItemCount() {
        return appointmentArrayList.size();
    }

    public class AppointmentsViewHolder extends RecyclerView.ViewHolder{

        public AppointmentsViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.appointment_list_item_name);
            Date = itemView.findViewById(R.id.appointment_list_item_time);
            Status = itemView.findViewById(R.id.appointment_list_item_status);
            Type = itemView.findViewById(R.id.appointment_list_item_type);

        }
    }
}
