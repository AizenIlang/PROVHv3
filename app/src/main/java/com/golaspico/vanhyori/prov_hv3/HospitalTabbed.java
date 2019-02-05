package com.golaspico.vanhyori.prov_hv3;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.golaspico.vanhyori.prov_hv3.tabbed.FragmentAppointment;
import com.golaspico.vanhyori.prov_hv3.tabbed.FragmentComment;
import com.golaspico.vanhyori.prov_hv3.tabbed.FragmentInfo;
import com.golaspico.vanhyori.prov_hv3.tabbed.FragmentPhoto;
import com.golaspico.vanhyori.prov_hv3.tabbed.SectionPageAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import modules.Comments;

public class HospitalTabbed extends AppCompatActivity {

    private SectionPageAdapter sectionPageAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private TextView mName,mAddress,mContact,mCity,mRatingDetails,mRatingReview;
    private ImageView imageView;
    private RatingBar ratingBar;

    private String Key;


    private FragmentPhoto fragmentPhoto;
    private FragmentComment fragmentComment;
    private FragmentInfo fragmentInfo;
    private FragmentAppointment fragmentAppointment;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_tabbed);

        sectionPageAdapter = new SectionPageAdapter(getSupportFragmentManager());

        fragmentComment = new FragmentComment();
        fragmentInfo = new FragmentInfo();
        fragmentAppointment = new FragmentAppointment();
        fragmentPhoto = new FragmentPhoto();
        //INITIALIZE THE IDS
        mName = findViewById(R.id.hospital_tab_name);
        mAddress = findViewById(R.id.hospital_tab_address);
        mCity = findViewById(R.id.hospital_tab_cityname);
        mContact = findViewById(R.id.hospital_tab_phone);
        imageView = findViewById(R.id.hospital_tab_image);
        ratingBar = findViewById(R.id.hospital_tab_rating);
        mRatingDetails = findViewById(R.id.hospital_tab_rating_details);
        mRatingReview = findViewById(R.id.hospital_tab_rating_review);

        Intent i = getIntent();
        Uri uri = Uri.parse(i.getStringExtra("THE_URI"));

//        ratingBar.setRating(i.getIntExtra("Rating",0));
//        details.setText(i.getStringExtra("Details"));
        Picasso.get().load(uri).into(imageView);


        mName.setText(i.getStringExtra("Name"));
        mAddress.setText(i.getStringExtra("Address"));
        mCity.setText(i.getStringExtra("CityName"));
        mContact.setText(i.getStringExtra("Contact"));

            Key = i.getStringExtra("Key");
            databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("Hospitals").child(Key).child("Comments").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    double total = 0.0;
                    double count = 0.0;
                    double average = 0.0;

                    for(DataSnapshot ds: dataSnapshot.getChildren()) {
                        double rating = Double.parseDouble(ds.child("rate").getValue().toString());
                        total = total + rating;
                        count = count + 1;
                        average = total / count;
                        average = Math.round(average);
                    }
                    DatabaseReference dbrate = FirebaseDatabase.getInstance().getReference();
                    dbrate.child("Hospitals").child(Key).child("Rating").setValue(average);
                    float x = (float)average;
                    ratingBar.setRating(x);
                    mRatingReview.setText(count + " (Reviews)");
                    if(x >= 0){
                        mRatingDetails.setText("Bad");

                    }
                    if( x >= 1){
                        mRatingDetails.setText("Poor");
                    }
                    if( x >= 2){
                        mRatingDetails.setText("Average");
                    }
                    if( x >= 3){
                        mRatingDetails.setText("Good");
                    }
                    if(x >= 4){
                        mRatingDetails.setText("Great");
                    }
                    if( x >= 5){
                        mRatingDetails.setText("Excellent");
                    }

                    if(count <= 0){
                        mRatingDetails.setText("No rating yet");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

//            ratingBar.setRating(i.getIntExtra("Rating",0));
//            mRatingReview.setText(i.getStringExtra("RatingReview"));
//            mRatingDetails.setText(i.getStringExtra("RatingDetails"));




//        i.putExtra("THE_URI",uri.toString());
//        i.putExtra("Key",hospital.getKey());
//        i.putExtra("Details",hospital.getDetails());
//        i.putExtra("Rating",hospital.getRating());
//        i.putExtra("CityName",hospital.getLocation());
//        i.putExtra("Contact",hospital.getContactNumber());
//        i.putExtra("Address",hospital.getAddress());
        //todo make viewpager and add it int tablayout
        viewPager = findViewById(R.id.hospital_tab_viewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.hospital_tab_tab);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_photo_black_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_info_black_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_comment_white_24dp);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_assignment_black_24dp);

    }

    private void setupViewPager(ViewPager viewPager){
        SectionPageAdapter adapter = new SectionPageAdapter(getSupportFragmentManager());
        Intent i = getIntent();
        Key = i.getStringExtra("Key");
        Bundle bundle = new Bundle();
        bundle.putString("Key",Key);
        bundle.putString("HospitalName",i.getStringExtra("Name"));
        bundle.putString("Contact",i.getStringExtra("Contact"));

        fragmentComment.setArguments(bundle);
        fragmentInfo.setArguments(bundle);
        fragmentAppointment.setArguments(bundle);
        fragmentPhoto.setArguments(bundle);
        adapter.addFragment(fragmentPhoto);
        adapter.addFragment(fragmentInfo);
        adapter.addFragment(fragmentComment);
        adapter.addFragment(fragmentAppointment);
        viewPager.setAdapter(adapter);
    }


}
