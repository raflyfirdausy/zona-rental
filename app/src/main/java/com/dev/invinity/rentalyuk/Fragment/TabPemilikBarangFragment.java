package com.dev.invinity.rentalyuk.Fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.dev.invinity.rentalyuk.Adapter.TabPemilikBarangAdapter;
import com.dev.invinity.rentalyuk.Adapter.TabStatusPenyewaAdapter;
import com.dev.invinity.rentalyuk.Models.Barang;
import com.dev.invinity.rentalyuk.Models.BarangModel;
import com.dev.invinity.rentalyuk.Models.BarangModel2;
import com.dev.invinity.rentalyuk.R;
import com.dev.invinity.rentalyuk.app.MyApplication;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabPemilikBarangFragment extends Fragment {

////////////////////////     Start Deklarasi Variable     /////////////////////////

    private RecyclerView recyclerView;
    private List<BarangModel2> ITEMLIST;
    private com.dev.invinity.rentalyuk.Adapter.TabPemilikBarangAdapter mAdapter;

    //firebase
    private StorageReference storageReference;
    private DatabaseReference databaseReferencePerental, databaseReferencePemilik, databaseReferenceBarang, databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth.AuthStateListener authStateListener;
    private String userID;

////////////////////////     End Deklarasi Variable     //////////////////////////


    public TabPemilikBarangFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tab_pemilik_barang, container, false);

        recyclerView = v.findViewById(R.id.konten_pemilik_barang);
        ITEMLIST = new ArrayList<>();
        mAdapter = new com.dev.invinity.rentalyuk.Adapter.TabPemilikBarangAdapter(getActivity(), ITEMLIST);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);

        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        userID = user.getUid();

        databaseReferencePemilik = FirebaseDatabase.getInstance().getReference("Rental/pemilik/" + userID);
        databaseReferencePemilik.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    final String KeyTransaksi = ds.getKey();
                    ITEMLIST.clear();
                    final String id_barang = ds.child("id_barang").getValue(String.class);
                    final String status = ds.child("status").getValue(String.class);

                    databaseReferenceBarang = FirebaseDatabase.getInstance().getReference("Barang/" + userID + "/" + id_barang);
                    databaseReferenceBarang.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            BarangModel2 barangModel = null;
                            barangModel = dataSnapshot.getValue(BarangModel2.class);
                            barangModel.setKeyPemilikBarang(userID);
                            barangModel.setKey(id_barang);
                            barangModel.setKeyTransaksi(KeyTransaksi);
                            barangModel.setStatus(status);
                            ITEMLIST.add(barangModel);
                            mAdapter = new TabPemilikBarangAdapter(getActivity(), ITEMLIST);
                            recyclerView.setAdapter(mAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return v;
    }

    private void make_toast(String pesan) {
        Toast.makeText(getActivity(), pesan, Toast.LENGTH_LONG).show();
    }

    private void make_alertDialog(String title, String message) {
        final AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getActivity());
        }

        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}
