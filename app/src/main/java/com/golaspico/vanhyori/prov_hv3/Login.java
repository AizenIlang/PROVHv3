package com.golaspico.vanhyori.prov_hv3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import modules.User;

public class Login extends AppCompatActivity {

    ConstraintLayout mLayout;
    AnimationDrawable mDrawable;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    ImageView mLogo;
    Button mLogginBtn;
    TextView mRegisterTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //INITIALIZATION

        mLayout = findViewById(R.id.login_constraint);
        mDrawable = (AnimationDrawable) mLayout.getBackground();
        mLogo = findViewById(R.id.login_logo);
        mLogginBtn = findViewById(R.id.login_buttonlogin);
        mRegisterTextView = findViewById(R.id.login_signup_text);


        mDrawable.setEnterFadeDuration(4500);
        mDrawable.setExitFadeDuration(4500);
        mDrawable.start();



        mLogginBtn.setOnClickListener(new OnClickLogin());
        mRegisterTextView.setOnClickListener(new OnClickRegister());



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
            SendTheImage();
        }
    }



    private class OnClickRegister implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            //TODO make a Register form

            Intent registrationForm = new Intent(getApplicationContext(),Registration.class);
            startActivity(registrationForm);
        }
    }

}

