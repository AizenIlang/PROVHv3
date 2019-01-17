package com.golaspico.vanhyori.prov_hv3;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

import modules.Doctor;

public class DoctorsListAdapter extends RecyclerView.Adapter<DoctorsListAdapter.DoctorsViewHolder> {
    private ArrayList<Doctor> doctorArrayList;
    private Context con;
    private LayoutInflater inflater;
    
    public DoctorsListAdapter(ArrayList<Doctor> arrayList, Context context){
        doctorArrayList = new ArrayList<Doctor>();
        doctorArrayList = arrayList;
        con = context;
        inflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public DoctorsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.doctors_list_item,viewGroup,false);
        DoctorsViewHolder holder = new DoctorsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorsViewHolder doctorsViewHolder, int i) {
        Doctor tempDoctor = doctorArrayList.get(i);
        doctorsViewHolder.DoctorsService.setText(tempDoctor.getService());
        String fullName = tempDoctor.getFirstName() + " " + tempDoctor.getLastName();
        doctorsViewHolder.DoctorsName.setText(fullName);

    }

    @Override
    public int getItemCount() {
        return doctorArrayList.size();
    }

    public class DoctorsViewHolder extends RecyclerView.ViewHolder {
        private TextView DoctorsName;
        private TextView DoctorsService;

        public DoctorsViewHolder(@NonNull View itemView) {
            super(itemView);
            DoctorsName =itemView.findViewById(R.id.doctors_list_name);
            DoctorsService = itemView.findViewById(R.id.doctors_list_service);

        }
    }
}
