package com.golaspico.vanhyori.prov_hv3;

import android.content.Intent;
import android.net.Uri;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import modules.Hospital;

public class HospitalDetails extends AppCompatActivity {

    private ImageView imageView;
    private MaterialButton CommentsBtn;
    private TextView details;
    private RatingBar ratingBar;
    private String Key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hospital_details);

        imageView = findViewById(R.id.hospital_details_image);
        ratingBar = findViewById(R.id.hospital_details_rating);
        details = findViewById(R.id.hospital_details_details);
        Intent i = getIntent();
        Uri uri = Uri.parse(i.getStringExtra("THE_URI"));
        Key = i.getStringExtra("Key");
        ratingBar.setRating(i.getIntExtra("Rating",0));
        details.setText(i.getStringExtra("Details"));
        Picasso.get().load(uri).into(imageView);


        CommentsBtn = findViewById(R.id.hospital_details_comment_btn);
        CommentsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CommentsSection.class);
                intent.putExtra("Key", Key);
                startActivity(intent);
            }
        });
    }
}
