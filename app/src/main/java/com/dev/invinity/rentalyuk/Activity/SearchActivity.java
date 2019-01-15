package com.dev.invinity.rentalyuk.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.invinity.rentalyuk.Admob.AdManager;
import com.dev.invinity.rentalyuk.Fragment.HomeFragment;
import com.dev.invinity.rentalyuk.Models.Barang;
import com.dev.invinity.rentalyuk.Models.BarangModel;
import com.dev.invinity.rentalyuk.R;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;
    private EditText et_searchBarang;
    private RecyclerView rv_kontenSearch;
    private List<Barang> itemsList;
    private List<BarangModel> ITEMLIST;
    private HomeFragment.BarangAdapter mAdapter;
    private com.dev.invinity.rentalyuk.Adapter.BarangAdapter barangAdapter;
    private String keyPemilikBarang;

    //firebase
    private StorageReference storageReference;
    private DatabaseReference databaseReference, databaseReferenceX;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth.AuthStateListener authStateListener;
    private String userID;

    //Query
    Query a;

    //Model
    BarangModel barangModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar             = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        et_searchBarang     = (EditText) findViewById(R.id.et_searchBarang);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rv_kontenSearch     =  findViewById(R.id.rv_kontenSearch);
        ITEMLIST        = new ArrayList<>();
        mAdapter        = new HomeFragment.BarangAdapter(SearchActivity.this, itemsList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(SearchActivity.this, 2);
        rv_kontenSearch.setLayoutManager(mLayoutManager);
        rv_kontenSearch.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(0), true));
        rv_kontenSearch.setItemAnimator(new DefaultItemAnimator());
//        rv_kontenSearch.setAdapter(mAdapter);
        rv_kontenSearch.setNestedScrollingEnabled(false);



        et_searchBarang.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Toast.makeText(SearchActivity.this, "Searching...", Toast.LENGTH_SHORT).show();
                    databaseReference = FirebaseDatabase.getInstance().getReference("Barang");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            barangModel = null;
                            ITEMLIST.clear();
                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                keyPemilikBarang = ds.getKey();
                                databaseReferenceX = databaseReference.child(Objects.requireNonNull(ds.getKey()));
                                Query X = databaseReferenceX.orderByChild("namaBarang")
                                        .startAt(et_searchBarang.getText().toString())
                                        .endAt(et_searchBarang.getText().toString()+"\uf8ff"); // \uf8ff  ~
                                X.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for(DataSnapshot DS : dataSnapshot.getChildren()){
                                            barangModel = DS.getValue(BarangModel.class);
                                            barangModel.setKeyPemilikBarang(keyPemilikBarang);
                                            barangModel.setKey(DS.getKey());
                                            ITEMLIST.add(barangModel);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }

                            barangAdapter   = new com.dev.invinity.rentalyuk.Adapter.BarangAdapter(SearchActivity.this, ITEMLIST);
                            rv_kontenSearch.setAdapter(barangAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    return true;
                }
                return false;
            }
        });


//        et_searchBarang.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//
//                if(keyCode == EditorInfo.IME_ACTION_SEARCH ||
//                        keyCode == EditorInfo.IME_ACTION_DONE ||
//                        event.getAction() == KeyEvent.ACTION_DOWN &&
//                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
//
//                        databaseReference = FirebaseDatabase.getInstance().getReference("Barang");
//                        databaseReference.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                barangModel = null;
//                                ITEMLIST.clear();
//                                for(DataSnapshot ds : dataSnapshot.getChildren()){
//                                    databaseReferenceX = databaseReference.child(Objects.requireNonNull(ds.getKey()));
//                                    Query X = databaseReferenceX.orderByChild("namaBarang")
//                                            .startAt(et_searchBarang.getText().toString())
//                                            .endAt(et_searchBarang.getText().toString()+"~"); // \uf8ff
//                                    X.addValueEventListener(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                            for(DataSnapshot DS : dataSnapshot.getChildren()){
//                                                barangModel = DS.getValue(BarangModel.class);
//                                                barangModel.setKeyPemilikBarang(keyPemilikBarang);
//                                                barangModel.setKey(DS.getKey());
//                                                ITEMLIST.add(barangModel);
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                        }
//                                    });
//                                }
//                                barangAdapter   = new com.dev.invinity.rentalyuk.Adapter.BarangAdapter(SearchActivity.this, ITEMLIST);
//                                rv_kontenSearch.setAdapter(barangAdapter);
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });
//                }
//
//                return false;
//            }
//        });

//        databaseReference = FirebaseDatabase.getInstance().getReference("Barang");
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                barangModel = null;
//                ITEMLIST.clear();
//                for(DataSnapshot ds : dataSnapshot.getChildren()){
//                    databaseReferenceX = databaseReference.child(ds.getKey().toString());
//                    Query X = databaseReferenceX.orderByChild("namaBarang").startAt("ge").endAt("ge\uf8ff");
//                    X.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            for(DataSnapshot DS : dataSnapshot.getChildren()){
//                                barangModel = DS.getValue(BarangModel.class);
//                                barangModel.setKeyPemilikBarang(keyPemilikBarang);
//                                barangModel.setKey(DS.getKey().toString());
//                                ITEMLIST.add(barangModel);
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//                }
//                barangAdapter   = new com.dev.invinity.rentalyuk.Adapter.BarangAdapter(SearchActivity.this, ITEMLIST);
//                rv_kontenSearch.setAdapter(barangAdapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private void make_alertDialog(String title, String message){
        final AlertDialog.Builder builder;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            builder = new AlertDialog.Builder(SearchActivity.this, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(SearchActivity.this);
        }

        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }


}
