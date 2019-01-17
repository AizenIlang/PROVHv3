package com.golaspico.vanhyori.prov_hv3;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;



import java.util.ArrayList;

import modules.Comments;
import modules.Hospital;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.HospitalViewHolder> {

    private ArrayList<Hospital> hospitalList;
    private ArrayList<Hospital> originalList;
    private LayoutInflater inflater;
    private DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();


    //View FIELDS

    private LinearLayout cardView;
    private Context context;
    private Activity activity;
    private boolean isMap;


    public HospitalAdapter(Context context, Activity activity,String category,String search, ArrayList<Hospital> hospitalArrayList,boolean isMap) {
        this.context = context;
        this.activity = activity;
        inflater = LayoutInflater.from(context);
        hospitalList = hospitalArrayList;
        this.isMap = isMap;


    }



    @NonNull
    @Override
    public HospitalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(isMap){
            view = inflater.inflate(R.layout.hospital_list_item_map,parent,false);
        }else{
            view = inflater.inflate(R.layout.hospital_list_item,parent,false);
        }

        HospitalViewHolder holder = new HospitalViewHolder(view);
        Log.e("Holder","On CreateViewHolder getting called");
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final HospitalViewHolder holder, int position) {

        final Hospital hospital = hospitalList.get(position);

        HospitalViewHolder tempHolder = holder;

        holder.HospitalName.setText(hospital.getName());
        holder.Phone.setText(hospital.getContactNumber());


        holder.Location.setText(hospital.getLocation());
        holder.Address.setText(hospital.getAddress());
        if(isMap){
            holder.Distance.setText(hospital.getDistance().toString());
        }else{
            holder.Rating.setRating(hospital.getRating());
            final DatabaseReference dbrate = FirebaseDatabase.getInstance().getReference();

            db.child("Hospitals").child(hospital.getKey()).child("Comments").addValueEventListener(new ValueEventListener() {
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

                    dbrate.child("Hospitals").child(hospital.getKey()).child("Rating").setValue(average);
                    float x = (float)average;
                    holder.Rating.setRating(x);
                    holder.RatingReview.setText(count + " (Reviews)");
                    if(x >= 0){
                        holder.RatingDetails.setText("Bad");

                    }
                    if( x >= 1){
                        holder.RatingDetails.setText("Poor");
                    }
                    if( x >= 2){
                        holder.RatingDetails.setText("Average");
                    }
                    if( x >= 3){
                        holder.RatingDetails.setText("Good");
                    }
                    if(x >= 4){
                        holder.RatingDetails.setText("Great");
                    }
                    if( x >= 5){
                        holder.RatingDetails.setText("Excellent");
                    }

                    if(count <= 0){
                        holder.RatingDetails.setText("No rating yet");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        Log.e("Holder","On bind getting called");
        Log.e("Holder",hospital.getName());
        storageRef.child(hospital.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(final Uri uri) {
                // Got the download URL for 'users/me/profile.png'


                Picasso.get().load(uri).into((ImageView)holder.itemView.findViewById(R.id.hospital_list_item_imageview));
                 holder.itemView.findViewById(R.id.hospital_list_item_cardview).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        sharedElementTransition(view,uri,hospital,holder);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });



    }

    @Override
    public int getItemCount() {
        return hospitalList.size();
    }


    public class HospitalViewHolder extends RecyclerView.ViewHolder {
        private TextView HospitalName;
        private TextView Phone;
        private RatingBar Rating;
        private ImageView Image;
        private TextView Location;
        private TextView Address;
        private TextView Distance;
        private TextView RatingDetails;
        private TextView RatingReview;

        public HospitalViewHolder(@NonNull View itemView) {
            super(itemView);
            HospitalName = itemView.findViewById(R.id.hospital_list_item_Name);
            Phone = itemView.findViewById(R.id.hospital_list_item_phone);
            Rating = itemView.findViewById(R.id.hospital_list_item_rating);
            Image = itemView.findViewById(R.id.hospital_list_item_imageview);
            Address = itemView.findViewById(R.id.hospital_list_item_address);
            Location = itemView.findViewById(R.id.hospital_list_item_city);
            cardView = itemView.findViewById(R.id.hospital_list_item_cardview);
            RatingDetails = itemView.findViewById(R.id.hospital_list_item_rating_details);
            RatingReview = itemView.findViewById(R.id.hospital_list_item_rating_reviews);
            if(isMap){
                Distance = itemView.findViewById(R.id.hospital_list_item_map_distance);
            }

        }
    }

    private void sharedElementTransition(View view,Uri uri,Hospital hospital,HospitalViewHolder holder){
        Pair[] pair = new Pair[5];
        Intent i = new Intent(context, HospitalTabbed.class);
        if(isMap){
            pair = new Pair[4];
            pair[0] = new Pair<View,String>(view.findViewById(R.id.hospital_list_item_imageview),"hospital_image");
//        pair[1] = new Pair<View,String>(view.findViewById(R.id.hospital_list_item_rating),"hospital_rating");
            pair[1] = new Pair<View,String>(view.findViewById(R.id.hospital_list_item_cardview),"hospital_cardview");
            pair[2] = new Pair<View,String>(view.findViewById(R.id.hospital_list_item_Name),"hospital_name");
            pair[3] = new Pair<View,String>(view.findViewById(R.id.hospital_list_item_phone),"hospita_contact");


        }else{
            pair[0] = new Pair<View,String>(view.findViewById(R.id.hospital_list_item_imageview),"hospital_image");
            pair[1] = new Pair<View,String>(view.findViewById(R.id.hospital_list_item_rating),"hospital_rating");
            pair[2] = new Pair<View,String>(view.findViewById(R.id.hospital_list_item_cardview),"hospital_cardview");
            pair[3] = new Pair<View,String>(view.findViewById(R.id.hospital_list_item_Name),"hospital_name");
            pair[4] = new Pair<View,String>(view.findViewById(R.id.hospital_list_item_phone),"hospita_contact");


        }

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity,pair);

        i.putExtra("THE_URI",uri.toString());
        i.putExtra("Key",hospital.getKey());
        i.putExtra("Details",hospital.getDetails());

        i.putExtra("CityName",hospital.getLocation());
        i.putExtra("Contact",hospital.getContactNumber());
        i.putExtra("Address",hospital.getAddress());
        i.putExtra("Name",hospital.getName());


        if(isMap){
            i.putExtra("Rating",hospital.getRating());
            i.putExtra("isMap",true);
        }else{
            i.putExtra("Rating",hospital.getRating());
            i.putExtra("isMap",false);
            i.putExtra("RatingDetails",holder.RatingDetails.getText());
            i.putExtra("RatingReview",holder.RatingReview.getText());
        }
        context.startActivity(i,options.toBundle());
    }



    public void filterList(ArrayList<Hospital> filteredList){
        hospitalList = filteredList;
        notifyDataSetChanged();
    }
}
