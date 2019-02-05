package com.golaspico.vanhyori.prov_hv3.tabbed;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.golaspico.vanhyori.prov_hv3.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import modules.Images;

public class FragmentPhoto extends Fragment {
    private static final String TAG = "FRAGMENT_PHOTO";

    private ConstraintLayout coordinatorLayout;
    private DatabaseReference db;
    private String Key;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private ScrollView scrollView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photos,container,false);

        Key = getArguments().getString("Key");
        db = FirebaseDatabase.getInstance().getReference().child("Images").child(Key);
        scrollView = view.findViewById(R.id.fragment_photos_scrollview);
        final GridLayout gridLayout = new GridLayout(getContext());
        gridLayout.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        gridLayout.setColumnCount(3);
        gridLayout.setRowCount(2);

        db.addChildEventListener(new ChildEventListener() {
             int r = 0;
             int c = 0;
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Images image = dataSnapshot.getValue(Images.class);

                final ImageView imageView;

                    imageView = new ImageView(getContext());
//            imageView.setText(facilities.get(i));
                    gridLayout.addView(imageView);
//            imageView.setCompoundDrawablesWithIntrinsicBounds(rightIc, 0, 0, 0);
                    GridLayout.LayoutParams param =new GridLayout.LayoutParams();
                    param.height =  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
                    param.width =  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
//                    param.rightMargin = 5;
//                    param.topMargin = 5;
                    param.setGravity(Gravity.CENTER);
                    if(c > 1){
                        c = 0;
                        r++;
                    }
                    if(r > 2){
                        r = 0;
                    }
                    param.columnSpec = GridLayout.spec(r);
                    param.rowSpec = GridLayout.spec(c);
                    c++;


                    imageView.setLayoutParams (param);

                Log.e("Hospitl Key ",Key);
                Log.e("HospitalImage", image.getImage());
                storageReference.child(image.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(final Uri uri) {
                        // Got the download URL for 'users/me/profile.png'


                        Picasso.get().load(uri).into(imageView);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });



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

        coordinatorLayout = view.findViewById(R.id.fragment_photos_coord);
//        coordinatorLayout.addView(gridLayout);
        scrollView.addView(gridLayout);
        return view;


    }
}
