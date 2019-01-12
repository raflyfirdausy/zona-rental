package com.dev.invinity.rentalyuk.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.invinity.rentalyuk.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class KeranjangFragment extends Fragment {


    public KeranjangFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_keranjang, container, false);

        return v;
    }

}
