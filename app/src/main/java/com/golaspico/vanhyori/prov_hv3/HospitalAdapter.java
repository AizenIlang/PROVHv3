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
import android.widget.RatingBar;
import android.widget.TextView;

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



import java.util.ArrayList;

import modules.Comments;
import modules.Hospital;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.HospitalViewHolder> {

    private ArrayList<Hospital> hospitalList;
    private LayoutInflater inflater;
    private DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();


    //View FIELDS
    private TextView HospitalName;
    private TextView Phone;
    private RatingBar Rating;
    private ImageView Image;
    private TextView Location;
    private TextView Address;
    private CardView cardView;
    private Context context;
    private Activity activity;


    public HospitalAdapter(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        inflater = LayoutInflater.from(context);
        hospitalList = new ArrayList<Hospital>();
        db.child("Hospitals").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Hospital temp = dataSnapshot.getValue(Hospital.class);
                temp.setKey(dataSnapshot.getKey());

                AddHospital(temp);
                Log.e("Add Hospital Called", "On child");


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Hospital temp = dataSnapshot.getValue(Hospital.class);
//                ChangeHospital(temp);

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    Hospital temp = dataSnapshot.getValue(Hospital.class);
                    RemoveHospital(temp);

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void AddHospital(Hospital hospital){

        int endofList=0;
        if(hospitalList.size() > 0){
            endofList = hospitalList.size() - 1;
        }
        hospitalList.add(endofList,hospital);
        //Todo double check the ID by itterating then notifydataposition changed
        notifyItemInserted(0);
//        notifyItemRangeChanged(lastIndex,hospitalList.size());
//        notifyDataSetChanged();
        Log.e("Add Hospital", hospital.getName());
//        notifyDataSetChanged();
    }

    public void RemoveHospital(Hospital hospital){
        int index = 0;
        for(Hospital myhospital : hospitalList){
            if(myhospital.getName().equals(hospital.getName())){
                index = hospitalList.indexOf(myhospital);
                Log.e("Remove Hospital index :", Integer.toString(index));
                break;
            }
        }
        hospitalList.remove(index);
        notifyItemRemoved(index);


//        notifyDataSetChanged();
    }

    public void ChangeHospital(Hospital hospital){

        int index = 0;
        for(Hospital myhospital : hospitalList){
            if(myhospital.getName().equals(hospital.getName())){
                index = hospitalList.indexOf(myhospital);
                break;
            }
        }
        hospitalList.remove(index);
//        notifyItemRemoved(index);
        hospitalList.add(index,hospital);
//        notifyItemInserted(index);
        notifyDataSetChanged();


    }

    @NonNull
    @Override
    public HospitalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.hospital_list_item,parent,false);
        HospitalViewHolder holder = new HospitalViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final HospitalViewHolder holder, int position) {
        final Hospital hospital = hospitalList.get(position);

        HospitalName.setText(hospital.getName());
        Phone.setText(hospital.getContactNumber());
        Rating.setRating(hospital.getRating());
        Location.setText(hospital.getLocation());
        Address.setText(hospital.getAddress());


        final ArrayList<Comments> commentArrayList = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("Hospitals").child(hospital.getKey()).child("Comments").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Comments tempComments = dataSnapshot.getValue(Comments.class);
                commentArrayList.add(tempComments);
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

        storageRef.child(hospital.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(final Uri uri) {
                // Got the download URL for 'users/me/profile.png'

                Picasso.get().load(uri).into((ImageView)holder.itemView.findViewById(R.id.hospital_list_item_imageview));
                 holder.itemView.findViewById(R.id.hospital_list_item_cardview).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        sharedElementTransition(view,uri,hospital);
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
        public HospitalViewHolder(@NonNull View itemView) {
            super(itemView);
            HospitalName = itemView.findViewById(R.id.hospital_list_item_Name);
            Phone = itemView.findViewById(R.id.hospital_list_item_phone);
            Rating = itemView.findViewById(R.id.hospital_list_item_rating);
            Image = itemView.findViewById(R.id.hospital_list_item_imageview);
            Address = itemView.findViewById(R.id.hospital_list_item_address);
            Location = itemView.findViewById(R.id.hospital_list_item_city);
            cardView = itemView.findViewById(R.id.hospital_list_item_cardview);

        }
    }

    private void sharedElementTransition(View view,Uri uri,Hospital hospital){
        Pair[] pair = new Pair[5];
        pair[0] = new Pair<View,String>(view.findViewById(R.id.hospital_list_item_imageview),"hospital_image");
        pair[1] = new Pair<View,String>(view.findViewById(R.id.hospital_list_item_rating),"hospital_rating");
        pair[2] = new Pair<View,String>(view.findViewById(R.id.hospital_list_item_cardview),"hospital_cardview");
        pair[3] = new Pair<View,String>(view.findViewById(R.id.hospital_list_item_Name),"hospital_name");
        pair[4] = new Pair<View,String>(view.findViewById(R.id.hospital_list_item_phone),"hospita_contact");
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity,pair);
        Intent i = new Intent(context, HospitalTabbed.class);


        i.putExtra("THE_URI",uri.toString());
        i.putExtra("Key",hospital.getKey());
        i.putExtra("Details",hospital.getDetails());
        i.putExtra("Rating",hospital.getRating());
        i.putExtra("CityName",hospital.getLocation());
        i.putExtra("Contact",hospital.getContactNumber());
        i.putExtra("Address",hospital.getAddress());
        i.putExtra("Name",hospital.getName());
        context.startActivity(i,options.toBundle());
    }

    private void AddNumberRating(){

    }
}
