package com.golaspico.vanhyori.prov_hv3;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

import modules.User;

public class UserProfile extends AppCompatActivity {

    private MaterialButton mSignOut;
    Calendar myCalendar = Calendar.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private EditText Age;
    private TextView provhID;
    private TextView mUpdate;
    private EditText FirstName,MiddleName,LastName;

    private User myUserGlobal;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        context = getApplicationContext();

        Age = findViewById(R.id.userprofile_birthdate);
        provhID = findViewById(R.id.userprofile_provhid);
        FirstName = findViewById(R.id.userprofile_first_name);
        MiddleName = findViewById(R.id.userprofile_middle_name);
        LastName = findViewById(R.id.userprofile_last_name);
        mUpdate = findViewById(R.id.userprofile_update);

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Users").child(firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User myUser = dataSnapshot.getValue(User.class);
                myUserGlobal = myUser;
                FirstName.setText(myUser.getFirstName());
                MiddleName.setText(myUser.getMiddleName());
                LastName.setText(myUser.getLastName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myUserGlobal.setFirstName(FirstName.getText().toString());
                myUserGlobal.setMiddleName(MiddleName.getText().toString());
                myUserGlobal.setLastName(LastName.getText().toString());

                databaseReference.child("Users").child(firebaseAuth.getUid()).child("firstName").setValue(myUserGlobal.getFirstName());
                databaseReference.child("Users").child(firebaseAuth.getUid()).child("middleName").setValue(myUserGlobal.getMiddleName());
                databaseReference.child("Users").child(firebaseAuth.getUid()).child("lastName").setValue(myUserGlobal.getLastName());
                Toast.makeText(context,"User Updated",Toast.LENGTH_SHORT).show();

            }
        });

        provhID.setText("Prov-H ID : " +firebaseAuth.getCurrentUser().getUid());

        final Spinner spinner = (Spinner) findViewById(R.id.userprofile_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.blood_types, R.layout.spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        mSignOut = findViewById(R.id.userprofile_signout_btn);
        mSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                finish();
            }
        });



        Age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, month);
                        myCalendar.set(Calendar.DAY_OF_MONTH, day);
                        updateLabel();
                    }
                };

                new DatePickerDialog(UserProfile.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }


    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        Age.setText(sdf.format(myCalendar.getTime()));
    }
}
