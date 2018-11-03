package com.golaspico.vanhyori.prov_hv3.tabbed;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.golaspico.vanhyori.prov_hv3.R;

public class FragmentPhoto extends Fragment {
    private static final String TAG = "FRAGMENT_PHOTO";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photos,container,false);
        return view;


    }
}
