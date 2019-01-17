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


  private Context context;
  private LayoutInflater inflater;

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

//                for(DataSnapshot data :dataSnapshot.getChildren()){
//                    tempAppointment.setDate((String)data.child("date").getValue());
//                    tempAppointment.setHospitalName((String)data.child("hospitalName").getValue());
//                    tempAppointment.setMessage((String)data.child("message").getValue());
//                    tempAppointment.setStatus((String)data.child("status").getValue());
//                    tempAppointment.setType((String)data.child("type").getValue());
//                    tempAppointment.setUid((String)data.child("uid").getValue());
//                }
                AddAppointment(tempAppointment);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Appointment tempAppointment = dataSnapshot.getValue(Appointment.class);

                int theIndex = 0;
                for(Appointment appointment : appointmentArrayList){
                    if(appointment.getMessage().equals(tempAppointment.getMessage())){
                        theIndex = appointmentArrayList.indexOf(appointment);
                        appointmentArrayList.set(theIndex,tempAppointment);

                        notifyItemChanged(theIndex);
                    }

                }


            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Appointment tempAppointment = dataSnapshot.getValue(Appointment.class);
                int theIndex = 0;
                for(Appointment appointment : appointmentArrayList){
                    if(appointment.getMessage().equals(tempAppointment.getMessage())){
                        theIndex = appointmentArrayList.indexOf(appointment);
                        appointmentArrayList.remove(theIndex);

                        notifyItemRemoved(theIndex);
                    }

                }

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
    public void onBindViewHolder(@NonNull final AppointmentsViewHolder holder, final int position) {
        final Appointment temp = appointmentArrayList.get(position);
        holder.Name.setText(temp.getHospitalName());
        holder.Date.setText(temp.getDate());
        holder.Type.setText(temp.getType());
        holder.Status.setText(temp.getStatus());
        holder.Doctor.setText(temp.getDoctor2());
        holder.cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RemoveAppointment(temp.getKey(),temp.getUid(),temp.getHospitalKey());

            }
        });
        if(temp.getStatus().equals("Approved")){
            holder.button.setVisibility(View.INVISIBLE);
        }else{
            holder.button.setVisibility(View.VISIBLE);
        }

        if(temp.getStatus().equals("Appointed")){
            holder.button.setVisibility(View.INVISIBLE);
            holder.cancel_button.setVisibility(View.INVISIBLE);
        }


        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.button.setVisibility(View.INVISIBLE);
                holder.Status.setText("Waiting for Response");

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
//                    dateTimeDialogFragment.setSimpleDateMonthAndDayFormat(new SimpleDateFormat("dd MMMM", Locale.getDefault()));
                    dateTimeDialogFragment.setSimpleDateMonthAndDayFormat(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()));
                } catch (SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException e) {
                    Log.e("DateTime", e.getMessage());
                }

// Set listener
                dateTimeDialogFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Date date) {
                        // Date is get on positive button click
                        // Do something
//                        holder.Date.setText(date.toString());

                        String pattern = "yyyy/MM/dd hh:mm a";
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

                        holder.Date.setText(simpleDateFormat.format(date));

                        ChangeDate(simpleDateFormat.format(date),temp.getKey(),temp.getUid(),temp.getHospitalKey());
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

    private void ChangeDate(String date,String key,String uid, String hospitalKey) {
        databaseReference.child("Appointments").child(hospitalKey).child(key).child("date").setValue(date);
        databaseReference.child("UserAppointments").child(uid).child(key).child("date").setValue(date);

    }

    private void RemoveAppointment(String key, String uid,String hospitalKey) {
        Log.e("Hosp","the hospital key is :" +hospitalKey);
        databaseReference.child("Appointments").child(hospitalKey).child(key).child("status").setValue("Canceled");
        databaseReference.child("UserAppointments").child(uid).child(key).removeValue();

    }

    @Override
    public int getItemCount() {
        return appointmentArrayList.size();
    }

    public class AppointmentsViewHolder extends RecyclerView.ViewHolder{
        private TextView Name;
        private TextView Date;
        private TextView Status;
        private TextView Type;
        private Button button;
        private Button cancel_button;
        private TextView Doctor;

        public AppointmentsViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.appointment_list_item_name);
            Date = itemView.findViewById(R.id.appointment_list_item_time);
            Status = itemView.findViewById(R.id.appointment_list_item_status);
            Type = itemView.findViewById(R.id.appointment_list_item_type);
            button = itemView.findViewById(R.id.appointment_list_item_button);
            cancel_button = itemView.findViewById(R.id.appointments_list_item_cancel_button);
            Doctor = itemView.findViewById(R.id.appointment_list_item_doctor);


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
