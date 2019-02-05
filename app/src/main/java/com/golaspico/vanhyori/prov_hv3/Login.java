package com.golaspico.vanhyori.prov_hv3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import modules.User;

public class Login extends AppCompatActivity {

    ConstraintLayout mLayout;
//    AnimationDrawable mDrawable;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ImageView mLogo;
    Button mLoginBtn;
    TextView mRegisterTextView,mResetBtn;

    EditText mEmail;
    EditText mPassword;

    FirebaseUser mUser;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser temp = mAuth.getCurrentUser();

         if(temp != null){
             if(temp.isEmailVerified()){
                 Intent lobbyIntent = new Intent(getApplicationContext(),Lobby.class);
                 startActivity(lobbyIntent);
                 finish();
             }

         }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //INITIALIZATION

        mLayout = findViewById(R.id.login_constraint);
//        mDrawable = (AnimationDrawable) mLayout.getBackground();
        mLogo = findViewById(R.id.login_logo);
        mLoginBtn = findViewById(R.id.login_buttonlogin);
        mRegisterTextView = findViewById(R.id.login_signup_text);
        mEmail = findViewById(R.id.login_edit_email);
        mPassword = findViewById(R.id.login_edit_password);
        mResetBtn = findViewById(R.id.login_forget_password);


//        mDrawable.setEnterFadeDuration(4500);
//        mDrawable.setExitFadeDuration(4500);
//        mDrawable.start();



        mLoginBtn.setOnClickListener(new OnClickLogin());
        mRegisterTextView.setOnClickListener(new OnClickRegister());
        mResetBtn.setOnClickListener(new OnClickReset());


    }

    private class OnClickReset implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(),ResetPassword.class);
            startActivity(intent);
        }
    }


    private void SendTheImage(){

        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

// Create a reference to "mountains.jpg"
        StorageReference mountainsRef = storageRef.child("mountains.jpg");

// Create a reference to 'images/mountains.jpg'
        StorageReference mountainImagesRef = storageRef.child("images/mountains.jpg");

// While the file names are the same, the references point to different files
        mountainsRef.getName().equals(mountainImagesRef.getName());    // true
        mountainsRef.getPath().equals(mountainImagesRef.getPath());    // false


        // Get the data from an ImageView as bytes
        mLogo.setDrawingCacheEnabled(true);
        mLogo.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) mLogo.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(getApplicationContext(),"Failed To Upload",Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
            }
        });
    }

    private class OnClickLogin implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            LoginAttempt();
        }
    }

    private boolean LoginAttempt(){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User tempUser = dataSnapshot.getValue(User.class);
                if(tempUser.getUserName().equals(mEmail.getText().toString())){
                    SignIn(tempUser.getEmail());
                }
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



        return false;
    }

    private void SignIn(String theEmail){

                mAuth.signInWithEmailAndPassword(theEmail,mPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    mUser = mAuth.getCurrentUser();
                    //WE DOOUBLE CHECK IF THE ACCOUNT IS ACTIVATED


                    if(mUser.isEmailVerified()){

                        Intent lobbyIntent = new Intent(getApplicationContext(),Lobby.class);
                        startActivity(lobbyIntent);
                        finish();
                    }else{

                        mUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent EmailVerificationIntent = new Intent(getApplicationContext(),EmailVerification.class);
                                startActivity(EmailVerificationIntent);
                            }
                        });

                    }


                }else{
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                }

            }
        });
    }



    private class OnClickRegister implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            //TODO make a Register form

            Intent registrationForm = new Intent(getApplicationContext(),Registration.class);
            startActivity(registrationForm);
        }
    }

    private void CloseApp(){
        finish();
    }

}

