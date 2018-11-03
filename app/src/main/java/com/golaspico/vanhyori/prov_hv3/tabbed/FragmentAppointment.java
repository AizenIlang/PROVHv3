package com.golaspico.vanhyori.prov_hv3.tabbed;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.golaspico.vanhyori.prov_hv3.R;
import com.golaspico.vanhyori.prov_hv3.Registration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import modules.Appointment;
import modules.Hospital;
import modules.User;

public class FragmentAppointment extends Fragment {

    private EditText mMesssage;
    private Button mSendButton;
    private String Key;
    private String HospitalName;
    private boolean isReady = false;
    private User myUser;
    private EditText mDate;
    Calendar myCalendar = Calendar.getInstance();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment,container,false);

        mMesssage = view.findViewById(R.id.appointment_message);
        mSendButton = view.findViewById(R.id.appointment_send_btn);
        mDate = view.findViewById(R.id.appointment_date);

        final Spinner spinner = (Spinner) view.findViewById(R.id.appointment_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.Appointments, R.layout.spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);




        Key = getArguments().getString("Key");
        HospitalName = getArguments().getString("HospitalName");


        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference.child("Users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myUser = dataSnapshot.getValue(User.class);
                isReady = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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
        dateTimeDialogFragment.setMinimumDateTime(new GregorianCalendar(2018, Calendar.NOVEMBER, 3).getTime());
        dateTimeDialogFragment.setMaximumDateTime(new GregorianCalendar(2025, Calendar.DECEMBER, 31).getTime());
        dateTimeDialogFragment.setDefaultDateTime(new GregorianCalendar(2018, Calendar.NOVEMBER, 3, 15, 20).getTime());

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
                mDate.setText(date.toString());
            }

            @Override
            public void onNegativeButtonClick(Date date) {
                // Date is get on negative button click
            }
        });

// Show



        //END OF DATE AND TIME IMPLEMENTATION


        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTimeDialogFragment.show(getActivity().getSupportFragmentManager(), "dialog_time");
            }
        });

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Validation() && isReady){
                    final Appointment myAppointment = new Appointment(mMesssage.getText().toString(),
                            firebaseUser.getUid(),myUser,spinner.getSelectedItem().toString(),
                            mDate.getText().toString(),HospitalName,
                            "Pending"
                            );
                    databaseReference.child("Appointments").child(Key).push().setValue(myAppointment).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getContext(),"Appointment added",Toast.LENGTH_SHORT).show();
                                databaseReference.child("UserAppointments").child(firebaseUser.getUid()).push().setValue(myAppointment);
                            }
                        }
                    });

                }
            }
        });





        return view;

    }



    private boolean Validation(){
        String stringBuilder = "";
        boolean valid = true;

        if(mMesssage.getText().toString().isEmpty()){
            stringBuilder +="Message";
            valid = false;
        }


        if(!valid)
            Toast.makeText(getContext(),stringBuilder + "Should not be empty",Toast.LENGTH_SHORT).show();

        return valid;
    }


    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mDate.setText(sdf.format(myCalendar.getTime()));
    }


}
