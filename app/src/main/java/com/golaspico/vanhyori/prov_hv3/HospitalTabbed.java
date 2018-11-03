package com.golaspico.vanhyori.prov_hv3;

import android.content.Intent;
import android.net.Uri;
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
import com.squareup.picasso.Picasso;

public class HospitalTabbed extends AppCompatActivity {

    private SectionPageAdapter sectionPageAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private TextView mName,mAddress,mContact,mCity;
    private ImageView imageView;
    private RatingBar ratingBar;

    private String Key;


    private FragmentPhoto fragmentPhoto;
    private FragmentComment fragmentComment;
    private FragmentInfo fragmentInfo;
    private FragmentAppointment fragmentAppointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_tabbed);

        sectionPageAdapter = new SectionPageAdapter(getSupportFragmentManager());

        fragmentComment = new FragmentComment();
        fragmentInfo = new FragmentInfo();
        fragmentAppointment = new FragmentAppointment();
        //INITIALIZE THE IDS
        mName = findViewById(R.id.hospital_tab_name);
        mAddress = findViewById(R.id.hospital_tab_address);
        mCity = findViewById(R.id.hospital_tab_cityname);
        mContact = findViewById(R.id.hospital_tab_phone);
        imageView = findViewById(R.id.hospital_tab_image);
        ratingBar = findViewById(R.id.hospital_tab_rating);

        Intent i = getIntent();
        Uri uri = Uri.parse(i.getStringExtra("THE_URI"));

//        ratingBar.setRating(i.getIntExtra("Rating",0));
//        details.setText(i.getStringExtra("Details"));
        Picasso.get().load(uri).into(imageView);


        mName.setText(i.getStringExtra("Name"));
        mAddress.setText(i.getStringExtra("Address"));
        mCity.setText(i.getStringExtra("CityName"));
        mContact.setText(i.getStringExtra("Contact"));
        ratingBar.setRating(i.getIntExtra("Rating",0));
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


        fragmentComment.setArguments(bundle);
        fragmentInfo.setArguments(bundle);
        fragmentAppointment.setArguments(bundle);

        adapter.addFragment(new FragmentPhoto());
        adapter.addFragment(fragmentInfo);
        adapter.addFragment(fragmentComment);
        adapter.addFragment(fragmentAppointment);
        viewPager.setAdapter(adapter);
    }


}
