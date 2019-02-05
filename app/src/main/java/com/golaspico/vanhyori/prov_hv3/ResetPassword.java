package com.golaspico.vanhyori.prov_hv3;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ResetPassword extends AppCompatActivity {

    //THE RESET PASSWORD IS CURRENTLY USING THE ACITIVITY MAIN LAYOUT

    private Button mButton;
    private EditText mEmail;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        mButton = findViewById(R.id.resetpassword_btn);
        mEmail = findViewById(R.id.resetpassword_email);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mEmail.getText().toString().isEmpty()){
                    Log.d("Reset",mEmail.getText().toString());
                    String resetme = mEmail.getText().toString();
                    firebaseAuth.sendPasswordResetEmail(resetme).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),task.getException().toString(),
                                        Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(),"Please Check your email fo the reset password",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }else{
                    Toast.makeText(getApplicationContext(),"Email Should not be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
