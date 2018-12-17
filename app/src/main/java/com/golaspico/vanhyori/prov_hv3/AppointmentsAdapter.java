package com.golaspico.vanhyori.prov_hv3;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.zip.Inflater;

import modules.Appointment;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.AppointmentsViewHolder> {

  private TextView Name;
  private TextView Date;
  private TextView Status;
  private TextView Type;
  private Context context;
  private LayoutInflater inflater;
  private Button button;
  private Button cancel_button;
  private ArrayList<Appointment> appointmentArrayList;
  private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
  private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
  private Activity activity;

    public AppointmentsAdapter(Context context) {
        this.context = context;
        this.activity = activity;
        inflater = LayoutInflater.from(context);
        appointmentArrayList = new ArrayList<Appointment>();

        databaseReference.child("UserAppointments").child(firebaseAuth.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Appointment tempAppointment = dataSnapshot.getValue(Appointment.class);
                AddAppointment(tempAppointment);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Appointment tempAppointment = dataSnapshot.getValue(Appointment.class);


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
        View view = inflater.inflate(R.layout.appointments_list_item,parent,false);
        AppointmentsViewHolder holder = new AppointmentsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentsViewHolder holder, int position) {
        final Appointment temp = appointmentArrayList.get(position);
        Name.setText(temp.getHospitalName());
        Date.setText(temp.getDate());
        Type.setText(temp.getType());
        Status.setText(temp.getStatus());
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("UserAppointments").child(firebaseAuth.getUid()).removeValue();
                databaseReference.child("Appointments").child(temp.getKey()).removeValue();
                 appointmentArrayList.clear();

                notifyDataSetChanged();
            }
        });
        if(temp.getStatus().equals("Approved")){
            button.setVisibility(View.INVISIBLE);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.setVisibility(View.INVISIBLE);
                Status.setText("Approved");

                //DATE AND TIME IMPLEMENTATION
                // Initialize
                final SwitchDateTimeDialogFragment dateTimeDialogFragment = SwitchDateTimeDialogFragment.newInstance(
                        "Title example",
                        "OK",
                        "Cancel"
                );

// Assign values
                dateTimeDialogFragment.startAtCalendarView();
                dateTimeDialogFragment.set24HoursMode(false);
                java.util.Date today = Calendar.getInstance().getTime();
                //TODO CHECK IF NO ERROR.
                dateTimeDialogFragment.setMinimumDateTime(today);
                dateTimeDialogFragment.setMaximumDateTime(new GregorianCalendar(2025, Calendar.DECEMBER, 31).getTime());
                dateTimeDialogFragment.setDefaultDateTime(today);

// Define new day and month format
                try {
                    dateTimeDialogFragment.setSimpleDateMonthAndDayFormat(new SimpleDateFormat("dd MMMM", Locale.getDefault()));


                } catch (SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException e) {
                    Log.e("DateTime", e.getMessage());
                }

// Set listener
                dateTimeDialogFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Date date) {
                        // Date is get on positive button click
                        // Do something
                        Date.setText(date.toString());
                    }

                    @Override
                    public void onNegativeButtonClick(Date date) {
                        // Date is get on negative button click
                    }
                });

// Show
                FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                dateTimeDialogFragment.show(manager, "dialog_time");

                //END OF DATE AND TIME IMPLEMENTATION
            }

        });


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
            button = itemView.findViewById(R.id.appointment_list_item_button);
            cancel_button = itemView.findViewById(R.id.appointments_list_item_cancel_button);


        }
    }


    public void AddAppointment(Appointment appointment){

        int endofList=0;
        if(appointmentArrayList.size() > 0){
            endofList = appointmentArrayList.size() - 1;
        }
        appointmentArrayList.add(endofList,appointment);
        //Todo double check the ID by itterating then notifydataposition changed
        notifyItemInserted(0);
        Log.e("Number of Appointments", String.valueOf(appointmentArrayList.size()));

    }
}
