package com.golaspico.vanhyori.prov_hv3;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

import modules.User;

public class Registration extends AppCompatActivity {

    Spinner spinner;
    EditText UserName,Email,Password,ConfirmPassword,FirstName,MiddleName,LastName,Age,Address;
    Button mRegister;
    Calendar myCalendar = Calendar.getInstance();
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;

    String Gender = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //WE SET THE VARIABLES
        UserName = findViewById(R.id.register_username);
        Email = findViewById(R.id.register_email);
        Password = findViewById(R.id.register_password);
        ConfirmPassword = findViewById(R.id.register_password_confirm);
        FirstName = findViewById(R.id.register_first_name);
        MiddleName = findViewById(R.id.register_middle_name);
        LastName = findViewById(R.id.register_last_name);
        Age = findViewById(R.id.register_age);
        Address = findViewById(R.id.register_address);

        mRegister = findViewById(R.id.register_register_btn);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.blood_types, R.layout.spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        FirstName.addTextChangedListener(firstTextWatcher);
        MiddleName.addTextChangedListener(middleTextWatcher);
        LastName.addTextChangedListener(lastTextWatcher);
        UserName.addTextChangedListener(userTextwatcher);
        Password.addTextChangedListener(passTextwatcher);


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

                new DatePickerDialog(Registration.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Validation()){

                    final User myUser = new User(UserName.getText().toString(),
                                            FirstName.getText().toString(),
                                            MiddleName.getText().toString(),
                                            LastName.getText().toString(),
                                            Email.getText().toString(),
                                            false,
                                            "",
                                            Password.getText().toString(),
                                            spinner.getSelectedItem().toString(),
                                            Age.getText().toString(),
                                            false,
                                            false,"",Gender,Address.getText().toString());

                     mAuth.createUserWithEmailAndPassword(myUser.getEmail(),myUser.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                         @Override
                         public void onComplete(@NonNull Task<AuthResult> task) {
                             if(task.isSuccessful()){
                                 FirebaseUser user = mAuth.getCurrentUser();

                                        myUser.setUserKey(user.getUid());
                                        mDatabase.child("Users").child(user.getUid()).setValue(myUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    FirebaseUser user = mAuth.getCurrentUser();
                                                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Intent EmailVerificationIntent = new Intent(getApplicationContext(),EmailVerification.class);
                                                            startActivity(EmailVerificationIntent);
                                                            CloseApp();
                                                        }
                                                    });

                                                }

                                            }
                                        });



                             }else{

                             }
                         }
                     });

//                    mDatabase.child("Users").push().setValue(myUser).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//
//                            mAuth.createUserWithEmailAndPassword(myUser.getEmail(),myUser.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<AuthResult> task) {
//                                    if(task.isSuccessful()){
//                                        FirebaseUser user = mAuth.getCurrentUser();
//
//                                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                Intent EmailVerificationIntent = new Intent(getApplicationContext(),EmailVerification.class);
//                                                startActivity(EmailVerificationIntent);
//                                                CloseApp();
//                                            }
//                                        });
//                                    }else{
//
//                                    }
//                                }
//                            });
//
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(getApplicationContext(),"Failed to Register, please check for a stable connection",Toast.LENGTH_SHORT).show();
//                        }
//                    });

                }
            }
        });
    }

    private TextWatcher firstTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(!FirstName.getText().toString().isEmpty()){
                String regex = "(.)*(\\d)(.)*";
                Pattern pattern = Pattern.compile(regex);

                boolean containsNumber = pattern.matcher(FirstName.getText().toString()).matches();

                if(containsNumber){
                    Toast.makeText(getApplicationContext(),"First Name cannnot have numbers",Toast.LENGTH_SHORT).show();

                }

                if(FirstName.getText().toString().length() <= 1){
                    Toast.makeText(getApplicationContext(),"First Name Atlease 2 Letters",Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private TextWatcher middleTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(!MiddleName.getText().toString().isEmpty()){
                String regex = "(.)*(\\d)(.)*";
                Pattern pattern = Pattern.compile(regex);

                boolean containsNumber = pattern.matcher(MiddleName.getText().toString()).matches();

                if(containsNumber){
                    Toast.makeText(getApplicationContext(),"Middle Name cannnot have numbers",Toast.LENGTH_SHORT).show();

                }

                if(FirstName.getText().toString().length() <= 1){
                    Toast.makeText(getApplicationContext(),"Middle Name Atlease 2 Letters",Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private TextWatcher lastTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(!LastName.getText().toString().isEmpty()){
                String regex = "(.)*(\\d)(.)*";
                Pattern pattern = Pattern.compile(regex);

                boolean containsNumber = pattern.matcher(LastName.getText().toString()).matches();

                if(containsNumber){
                    Toast.makeText(getApplicationContext(),"Last Name cannnot have numbers",Toast.LENGTH_SHORT).show();

                }

                if(LastName.getText().toString().length() <= 1){
                    Toast.makeText(getApplicationContext(),"Last Name Atlease 2 Letters",Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private TextWatcher userTextwatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String regex = "(.*[A-Z].*)";
            Pattern pattern = Pattern.compile(regex);

            boolean containsUpper = pattern.matcher(UserName.getText().toString()).matches();

            if(!containsUpper){




               final Toast testToast =  Toast.makeText(getApplicationContext(),"User Name must have Upper case",Toast.LENGTH_SHORT);
               testToast.setGravity(Gravity.TOP|Gravity.LEFT, 0, 0);
               testToast.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        testToast.cancel();
                    }
                }, 3000);

                return;
            }else{

            }

            if(UserName.getText().length() < 7){
                final Toast testToast = Toast.makeText(getApplicationContext(), "User Name at lease 8 Characters",Toast.LENGTH_SHORT);
                testToast.setGravity(Gravity.TOP|Gravity.LEFT, 0, 0);
                testToast.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        testToast.cancel();
                    }
                }, 3000);

                return;
            }else{

            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


    private TextWatcher passTextwatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String regex = "(.*[A-Z].*)";
            Pattern pattern = Pattern.compile(regex);

            boolean containsUpper = pattern.matcher(Password.getText().toString()).matches();

            if(!containsUpper){
                final Toast testToast = Toast.makeText(getApplicationContext(),"User Name must have Upper case",Toast.LENGTH_SHORT);
                testToast.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        testToast.cancel();
                    }
                }, 1000);
            }

            if(Password.getText().length() < 7){
                final Toast testToast = Toast.makeText(getApplicationContext(), "User Name at lease 8 Characters",Toast.LENGTH_SHORT);
                testToast.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        testToast.cancel();
                    }
                }, 1000);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        Age.setText(sdf.format(myCalendar.getTime()));
    }

    private boolean Validation(){
        String stringBuilder = "";
        boolean valid = true;

        if(Gender.isEmpty()){
            stringBuilder +="Gender";
            valid = false;
        }

        if(UserName.getText().toString().isEmpty()){
            stringBuilder +="UserName";
            valid = false;
        }
        if(Email.getText().toString().isEmpty()){
            stringBuilder += "Email ";
            valid = false;
        }

        if(Password.getText().toString().isEmpty()){
            stringBuilder += "Password ";


            valid = false;
        }

        String regex = "(.)*(\\d)(.)*";
        Pattern pattern = Pattern.compile(regex);

        String regEx = "[A-Z]";
        Pattern patternUppercase = Pattern.compile(regEx);
        boolean containsNumber = pattern.matcher(Password.getText().toString()).matches();
        boolean containsNumberUsername = pattern.matcher(UserName.getText().toString()).matches();
        boolean containsUppercaseUsername = patternUppercase.matcher(UserName.getText().toString()).matches();

        int count = 0;
        for (char c : UserName.getText().toString().toCharArray()) {
            if (Character.isUpperCase(c)) {
                count++;
            }
        }

        if(count == 0){
           stringBuilder += "Username needs to have Uppercase";
           valid = false;
        }

        if(!containsNumberUsername){
            stringBuilder += "Username needs to have a number";
            valid = false;
        }
        if(!containsNumber){
            stringBuilder += "Password needs to have a number";
            valid = false;
        }

        if(ConfirmPassword.getText().toString().isEmpty()){
            stringBuilder += "Confirm Password ";
            valid = false;
        }

        if(FirstName.getText().toString().isEmpty()){
            stringBuilder += "First Name ";
            valid = false;
        }

        if(MiddleName.getText().toString().isEmpty()){
            stringBuilder += "Middle Name ";
            valid = false;
        }

        if(MiddleName.getText().length() < 1){
            stringBuilder += "Middle Name atleast 2 letters";
            valid = false;
        }

        if(LastName.getText().toString().isEmpty()){
            stringBuilder += "Last Name";
            valid = false;
        }

        if(Age.getText().toString().isEmpty()){
            stringBuilder += "Birthdate ";
            valid = false;
        }

        if(Address.getText().toString().isEmpty()){
            stringBuilder += "Address ";
            valid = false;
        }

        if(!valid){
            final Toast testToast = Toast.makeText(getApplicationContext(),stringBuilder + "Should not be empty",Toast.LENGTH_SHORT);
            testToast.setGravity(Gravity.TOP|Gravity.LEFT, 0, 0);
            testToast.show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    testToast.cancel();
                }
            }, 1000);
        }




        return valid;
    }

    private void CloseApp(){
        finish();
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.register_gender_male:
                if (checked)
                    Gender = "Male";
                    break;
            case R.id.register_gender_female:
                if (checked)
                    Gender = "Female";
                    break;
        }
    }
}
