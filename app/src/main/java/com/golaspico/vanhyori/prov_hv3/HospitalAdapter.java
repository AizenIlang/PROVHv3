package com.golaspico.vanhyori.prov_hv3;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import modules.Hospital;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.HospitalViewHolder> {

    private ArrayList<Hospital> hospitalList;
    private LayoutInflater inflater;


    //View FIELDS
    private TextView HospitalName;
    private TextView Content;
    private RatingBar Rating;
    private ImageView Image;
    private TextView Location;

    public HospitalAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.hospitalList = getHospitalList();
    }

    public ArrayList<Hospital> getHospitalList(){

        ArrayList<Hospital> hosp = new ArrayList<>();
        Hospital test = new Hospital(1,"Mandaluyong Hospital",
                "Mandaluyong","Plainview Santo Rosario St.","555-55-55",
                "manda@mh.com","nothingyet","Morgue luyong to the rescqill",2,"NoImage yet");
        hosp.add(test);
        hosp.add(test);
        hosp.add(test);
        hosp.add(test);
        hosp.add(test);
        hosp.add(test);
        hosp.add(test);
        hosp.add(test);
        hosp.add(test);
        hosp.add(test);
        hosp.add(test);



        return hosp;
    }

    @NonNull
    @Override
    public HospitalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.hospital_list_item,parent,false);
        HospitalViewHolder holder = new HospitalViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HospitalViewHolder holder, int position) {
        Hospital hospital = hospitalList.get(position);

        HospitalName.setText(hospital.getName());
        Content.setText(hospital.getAddress());
        Rating.setRating(hospital.getRating());
        Location.setText(hospital.getAddress());
    }

    @Override
    public int getItemCount() {
        return hospitalList.size();
    }


    public class HospitalViewHolder extends RecyclerView.ViewHolder {
        public HospitalViewHolder(@NonNull View itemView) {
            super(itemView);
            HospitalName = itemView.findViewById(R.id.hospital_list_item_Name);
            Content = itemView.findViewById(R.id.hospital_list_item_content);
            Rating = itemView.findViewById(R.id.hospital_list_item_rating);
            Image = itemView.findViewById(R.id.hospital_list_item_imageview);
            Location = itemView.findViewById(R.id.hospital_list_item_city);

        }
    }
}
