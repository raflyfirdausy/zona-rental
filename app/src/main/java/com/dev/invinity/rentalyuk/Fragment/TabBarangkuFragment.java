package com.dev.invinity.rentalyuk.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.invinity.rentalyuk.Models.BarangModel;
import com.dev.invinity.rentalyuk.Models.BarangModel2;
import com.dev.invinity.rentalyuk.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabBarangkuFragment extends Fragment {

////////////////////////     Start Deklarasi Variable     /////////////////////////

    private RecyclerView recyclerView;
    private List<BarangModel> ITEMLIST;
    private com.dev.invinity.rentalyuk.Adapter.TabBarangkuAdapter mAdapter;

    //firebase
    private StorageReference storageReference;
    private DatabaseReference databaseReferencePerental, databaseReferencePemilik, databaseReferenceBarang,databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth.AuthStateListener authStateListener;
    private String userID;

////////////////////////     End Deklarasi Variable     //////////////////////////

    public TabBarangkuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tab_barangku, container, false);
        recyclerView    = v.findViewById(R.id.konten_barangku);
        ITEMLIST        = new ArrayList<>();
        mAdapter        = new com.dev.invinity.rentalyuk.Adapter.TabBarangkuAdapter(getActivity(), ITEMLIST);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);

        //firebase
        firebaseAuth        = FirebaseAuth.getInstance();
        user                = firebaseAuth.getCurrentUser();
        userID              = user.getUid();

        databaseReferencePemilik   = FirebaseDatabase.getInstance().getReference("Barang/" + userID);
        databaseReferencePemilik.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                BarangModel barangModel;
                ITEMLIST.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    barangModel = ds.getValue(BarangModel.class);
                    barangModel.setKeyPemilikBarang(userID);
                    barangModel.setKey(ds.getKey().toString());
                    ITEMLIST.add(barangModel);
                }

                mAdapter   = new com.dev.invinity.rentalyuk.Adapter.TabBarangkuAdapter(getActivity(), ITEMLIST);
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return v;
    }

}
