package com.golaspico.vanhyori.prov_hv3.tabbed;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import modules.Appointment;
import modules.Doctor;
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
    private EditText mPreferredDoctor;
    Calendar myCalendar = Calendar.getInstance();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment,container,false);

        mMesssage = view.findViewById(R.id.appointment_message);
        mSendButton = view.findViewById(R.id.appointment_send_btn);
        mDate = view.findViewById(R.id.appointment_date);
        mPreferredDoctor = view.findViewById(R.id.appointment_preferred_doctor);
        Key = getArguments().getString("Key");
        HospitalName = getArguments().getString("HospitalName");

        final Spinner spinner = (Spinner) view.findViewById(R.id.appointment_spinner);
//        final Spinner spinner_doctor = view.findViewById(R.id.appointment_spinner_doctor);

// Create an ArrayAdapter using the string array and a default spinner layout
//        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
//                R.array.Doctors, R.layout.spinner_item);
        final ArrayList<String> arrayList = new ArrayList<String>();
//        final ArrayList<String> arrayListdoctor = new ArrayList<String>();
//        spinner_doctor.setAdapter(adapter);

//        databaseReference.child("Doctors").child(Key).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
//                    Doctor tempString = dataSnapshot1.getValue(Doctor.class);
//                    String myDoctor = tempString.getFirstName() + " " +tempString.getLastName();
//                    arrayListdoctor.add(myDoctor);
//                }
//                if(arrayListdoctor.size() > 0){
//
//
//                    ArrayAdapter<String> adapterdoctor = new ArrayAdapter<String>(getContext(),R.layout.spinner_item,arrayListdoctor);
//// Specify the layout to use when the list of choices appears
//                    adapterdoctor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//                    //Test the new adapter
//                    MySpinnerAdapterDoctor mySpinnerAdapterdoctor = new MySpinnerAdapterDoctor(getContext(),arrayListdoctor);
//                    mySpinnerAdapterdoctor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    spinner_doctor.setAdapter(mySpinnerAdapterdoctor);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        databaseReference.child("Hospitals").child(Key).child("Services").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String tempString = dataSnapshot.getValue(String.class);
                if(tempString != null){
                    String breakerString[] = tempString.split("@");
                    for(String yeah : breakerString){
                        arrayList.add(yeah);
                    }
                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(),R.layout.spinner_item,arrayList);
// Specify the layout to use when the list of choices appears
                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    //Test the new adapter
                    MySpinnerAdapter mySpinnerAdapter = new MySpinnerAdapter(getContext(),arrayList);
                    mySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(mySpinnerAdapter);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

// Apply the adapter to the spinner





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
       Date today = Calendar.getInstance().getTime();
        //TODO CHECK IF NO ERROR.
        dateTimeDialogFragment.setMinimumDateTime(today);
        dateTimeDialogFragment.setMaximumDateTime(new GregorianCalendar(2025, Calendar.DECEMBER, 31).getTime());
        dateTimeDialogFragment.setDefaultDateTime(today);

// Define new day and month format
        try {
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
                String pattern = "yyyy/MM/dd hh:mm a";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

                mDate.setText(simpleDateFormat.format(date));
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
                Log.e("The Spinner Value", spinner.getSelectedItem().toString());
                mSendButton.setEnabled(false);
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // This method will be executed once the timer is over
                        mSendButton.setEnabled(true);
                        Log.d("sendButtonAppointment","resend1");

                    }
                },30000);// set time as per your requirement


                if(Validation() && isReady){
                    Doctor newDoctor = new Doctor();
                    newDoctor.setFirstName(mPreferredDoctor.getText().toString());
                    newDoctor.setMiddleName("");
                    newDoctor.setLastName("");
                    newDoctor.setService("");

                    final String theKey = databaseReference.push().getKey();
                    final Appointment myAppointment = new Appointment(mMesssage.getText().toString(),
                            firebaseUser.getUid(),myUser,arrayList.get(spinner.getSelectedItemPosition()-1),
                            mDate.getText().toString(),HospitalName,
                            "Pending",myUser.getUserName(),theKey,newDoctor,mPreferredDoctor.getText().toString()
                            );
                    myAppointment.setHospitalKey(Key);

                    databaseReference.child("Appointments").child(Key).child(theKey).setValue(myAppointment).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getContext(),"Appointment added",Toast.LENGTH_SHORT).show();
                                databaseReference.child("UserAppointments").child(firebaseUser.getUid()).child(theKey).setValue(myAppointment);
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


    public class MySpinnerAdapter extends ArrayAdapter<String> {

        public MySpinnerAdapter(Context context, List<String> items) {
            super(context, R.layout.spinner_item, items);
        }

        @Override
        public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
            if (position == 0) {
                return initialSelection(true);
            }
            return getCustomView(position, convertView, parent);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            if (position == 0) {
                return initialSelection(false);
            }
            return getCustomView(position, convertView, parent);
        }


        @Override
        public int getCount() {
            return super.getCount() + 1; // Adjust for initial selection item
        }

        private View initialSelection(boolean dropdown) {
            // Just an example using a simple TextView. Create whatever default view
            // to suit your needs, inflating a separate layout if it's cleaner.
            TextView view = new TextView(getContext());
            view.setText("Select Service");
            int spacing = getContext().getResources().getDimensionPixelSize(R.dimen.separator_vertical_padding);
            view.setPadding(0, spacing, 0, spacing);

            if (dropdown) { // Hidden when the dropdown is opened
                view.setHeight(0);
            }

            return view;
        }

        private View getCustomView(int position, View convertView, ViewGroup parent) {
            // Distinguish "real" spinner items (that can be reused) from initial selection item
            View row = convertView != null && !(convertView instanceof TextView)
                    ? convertView :
                    LayoutInflater.from(getContext()).inflate(R.layout.spinner_item, parent, false);

            position = position - 1; // Adjust for initial selection item
            String item = getItem(position);
            TextView myTextView = row.findViewById(R.id.spinner_item);
            myTextView.setText(item);
            // ... Resolve views & populate with data ...

            return row;
        }

    }


    public class MySpinnerAdapterDoctor extends ArrayAdapter<String> {

        public MySpinnerAdapterDoctor(Context context, List<String> items) {
            super(context, R.layout.spinner_item, items);
        }

        @Override
        public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
            if (position == 0) {
                return initialSelection(true);
            }
            return getCustomView(position, convertView, parent);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            if (position == 0) {
                return initialSelection(false);
            }
            return getCustomView(position, convertView, parent);
        }


        @Override
        public int getCount() {
            return super.getCount() + 1; // Adjust for initial selection item
        }

        private View initialSelection(boolean dropdown) {
            // Just an example using a simple TextView. Create whatever default view
            // to suit your needs, inflating a separate layout if it's cleaner.
            TextView view = new TextView(getContext());
            view.setText("Select Doctor");
            int spacing = getContext().getResources().getDimensionPixelSize(R.dimen.separator_vertical_padding);
            view.setPadding(0, spacing, 0, spacing);

            if (dropdown) { // Hidden when the dropdown is opened
                view.setHeight(0);
            }

            return view;
        }

        private View getCustomView(int position, View convertView, ViewGroup parent) {
            // Distinguish "real" spinner items (that can be reused) from initial selection item
            View row = convertView != null && !(convertView instanceof TextView)
                    ? convertView :
                    LayoutInflater.from(getContext()).inflate(R.layout.spinner_item, parent, false);

            position = position - 1; // Adjust for initial selection item
            String item = getItem(position);
            TextView myTextView = row.findViewById(R.id.spinner_item);
            myTextView.setText(item);
            // ... Resolve views & populate with data ...

            return row;
        }

    }

}


