package com.dev.invinity.rentalyuk.Fragment;


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
import com.dev.invinity.rentalyuk.Adapter.TabStatusPenyewaAdapter;
import com.dev.invinity.rentalyuk.Models.Barang;
import com.dev.invinity.rentalyuk.Models.BarangModel;
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
public class TabStatusPenyewaFragment extends Fragment {

////////////////////////     Start Deklarasi Variable     /////////////////////////

    private RecyclerView recyclerView;
    private List<BarangModel> ITEMLIST;
    private com.dev.invinity.rentalyuk.Adapter.TabStatusPenyewaAdapter mAdapter;

    //firebase
    private StorageReference storageReference;
    private DatabaseReference databaseReferencePerental, databaseReferencePemilik, databaseReferenceBarang,databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth.AuthStateListener authStateListener;
    private String userID;

    private String keyPemilikBarang;

////////////////////////     End Deklarasi Variable     //////////////////////////



    public TabStatusPenyewaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_tab_status_penyewa, container, false);

        recyclerView    = v.findViewById(R.id.konten_statusPenyewa);
        ITEMLIST        = new ArrayList<>();
        mAdapter        = new com.dev.invinity.rentalyuk.Adapter.TabStatusPenyewaAdapter(getActivity(),ITEMLIST);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);

        //firebase
        firebaseAuth        = FirebaseAuth.getInstance();
        user                = firebaseAuth.getCurrentUser();
        userID              = user.getUid();

        databaseReferencePerental   = FirebaseDatabase.getInstance().getReference("Rental/perental/" + userID);
        databaseReferencePerental.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()){
//                    make_toast(ds.getKey());
                    ITEMLIST.clear();
                    final String id_barang = ds.child("id_barang").getValue(String.class);
                    final String id_pemilik = ds.child("id_pemilik").getValue(String.class);

                    databaseReferenceBarang = FirebaseDatabase.getInstance().getReference("Barang/" + id_pemilik + "/" + id_barang);
                    databaseReferenceBarang.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            BarangModel barangModel = null;

                            barangModel = dataSnapshot.getValue(BarangModel.class);
                            barangModel.setKeyPemilikBarang(id_pemilik);
                            barangModel.setKey(id_barang);
                            ITEMLIST.add(barangModel);
                            mAdapter        = new TabStatusPenyewaAdapter(getActivity(), ITEMLIST);
                            recyclerView.setAdapter(mAdapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
//                make_toast(String.valueOf(mAdapter.getItemCount()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return v;
    }


//    private void ambil_data() {
//
//        JsonArrayRequest request = new JsonArrayRequest(URL,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        if (response == null) {
//                            Toast.makeText(getActivity(), "Couldn't fetch the store items! Pleas try again.", Toast.LENGTH_LONG).show();
//                            return;
//                        }
//
//                        List<Barang> items = new Gson().fromJson(response.toString(), new TypeToken<List<Barang>>() {
//                        }.getType());
//
//                        itemsList.clear();
//                        itemsList.addAll(items);
//
//                        // refreshing recycler view
//                        mAdapter.notifyDataSetChanged();
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                // error in getting json
//                Log.e(TAG, "Error: " + error.getMessage());
//                Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        MyApplication.getInstance().addToRequestQueue(request);
//    }

    private void make_toast(String pesan){
        Toast.makeText(getActivity(), pesan, Toast.LENGTH_LONG).show();
    }

}
