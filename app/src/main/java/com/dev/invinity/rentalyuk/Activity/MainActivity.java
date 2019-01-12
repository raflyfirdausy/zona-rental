package com.dev.invinity.rentalyuk.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.invinity.rentalyuk.Admob.AdManager;
import com.dev.invinity.rentalyuk.Fragment.*;

import com.dev.invinity.rentalyuk.Models.Biodata;
import com.dev.invinity.rentalyuk.Models.Users;
import com.dev.invinity.rentalyuk.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationViewEx bottomNavigation;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private LinearLayout konten_toolbar;
    private android.support.v7.widget.Toolbar toolbar;
    private InterstitialAd interstitialAd;
    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;
    private TextView toolbar_title;
    private EditText etSearch;

    //firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference databaseReference;
    private String userID;

    //fragment
    SayaOverviewFragment sayaOverviewFragment;


    //model user
    Users userInfo;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //firebase
        firebaseAuth        = FirebaseAuth.getInstance();
        user                = firebaseAuth.getCurrentUser();
        userID              = user.getUid();
        databaseReference   = FirebaseDatabase.getInstance()
                                .getReference()
                                .child("Users")
                                .child(userID);


        //get data
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                make_toast("Erorr " + databaseError.getMessage());
            }
        });

        //Konten toolbar
        toolbar         = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        konten_toolbar  = (LinearLayout) toolbar.findViewById(R.id.konten_toolbar);
        toolbar_title   = (TextView) findViewById(R.id.toolbar_title);

        etSearch        = (EditText) findViewById(R.id.etSearch);
        etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        //Bottom navigation
        bottomNavigation = (BottomNavigationViewEx) findViewById(R.id.bnve);
        bottomNavigation.enableAnimation(true);
        bottomNavigation.enableShiftingMode(false);
        bottomNavigation.enableItemShiftingMode(false);


        //Fragment
        fragmentManager = getSupportFragmentManager();
        sayaOverviewFragment = new SayaOverviewFragment();



        //adMob
//        InterstitialAd interstitialAd = AdManager.getAd();
//        if(interstitialAd != null){
//            interstitialAd.show();
//        }


        //cek ada extera intent buat pindah fragment apa engga
        if(getIntent().hasExtra("pindahFragment") &&
                getIntent().getStringExtra("pindahFragment").equals("fragmentSayaOverview")){
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, new SayaOverviewFragment())
                    .commit();
            bottomNavigation.setCurrentItem(2);
        } else if(getIntent().hasExtra("pindahFragment") &&
                getIntent().getStringExtra("pindahFragment").equals("TransaksiFragment")){
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, new TransaksiFragment())
                    .commit();
            bottomNavigation.setCurrentItem(1);
        } else {
            //Untuk inisialisasi fragment pertama kali
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }



        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.action_home:
//                        getSupportActionBar().show();
                        konten_toolbar.setVisibility(View.VISIBLE);
                        toolbar_title.setVisibility(View.GONE);
                        fragment = new HomeFragment();
                        break;
                    case R.id.action_transaksi:
//                        getSupportActionBar().show();
                        konten_toolbar.setVisibility(View.GONE);
                        getSupportActionBar().setTitle("");
                        toolbar_title.setText("TRANSAKSI");
                        toolbar_title.setVisibility(View.VISIBLE);
                        fragment = new TransaksiFragment();
                        break;
//                    case R.id.action_keranjang:
////                        getSupportActionBar().show();
//                        konten_toolbar.setVisibility(View.GONE);
//                        getSupportActionBar().setTitle("");
//                        toolbar_title.setText("KERANJANG");
//                        toolbar_title.setVisibility(View.VISIBLE);
//                        fragment = new KeranjangFragment();
//                        break;
                    case R.id.action_person:
//                        getSupportActionBar().hide();
                        konten_toolbar.setVisibility(View.GONE);
                        getSupportActionBar().setTitle("");
                        toolbar_title.setText("SAYA");
                        toolbar_title.setVisibility(View.VISIBLE);
                        fragment = new SayaOverviewFragment();
                        break;
                }
                final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment).commit();
                return true;
            }
        });
    }

    private void make_toast(String  pesan){
        Toast.makeText(this, pesan, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed()
    {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()){
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Tekan Back Sekali lagi untuk Keluar", Toast.LENGTH_SHORT).show();
        }

        mBackPressed = System.currentTimeMillis();
    }

    private void getData(DataSnapshot dataSnapshot){
//        for(DataSnapshot ds : dataSnapshot.getChildren()){
//            DataSnapshot ds = null;
//            userInfo        = dataSnapshot.getValue(Users.class);
//            make_toast();

//            String nama = ds.child("namaLengkap").getValue(String.class);
//            make_toast(ds.getValue().toString());
//            userInfo.setAlamat(dataSnapshot.getValue(Users.class).getAlamat());               //set Alamat
//            userInfo.setJenisKelamin(dataSnapshot.getValue(Users.class).getJenisKelamin());   //set jenis kelamin
//            userInfo.setNamaLengkap(dataSnapshot.getValue(Users.class).getNamaLengkap());     // set nama
//            userInfo.setNoTelp(dataSnapshot.getValue(Users.class).getNoTelp());               //set no telp
//            userInfo.setTglLahir(dataSnapshot.getValue(Users.class).getTglLahir());           //set tanggal lahir
//
//        make_toast(userInfo.getNamaLengkap() + " " +
//                userInfo.getAlamat() + " " +
//                userInfo.getJenisKelamin() + " " +
//                userInfo.getNoTelp() + " " +
//                userInfo.getTglLahir() + " " );
//        }


//        make_toast(dataSnapshot.getValue(Users.class).getNamaLengkap());
//        make_toast(dataSnapshot.getValue(Users.class).getAlamat());
//        make_toast(dataSnapshot.getValue(Users.class).getJenisKelamin());
//        make_toast(dataSnapshot.getValue(Users.class).getNoTelp());
//        make_toast(dataSnapshot.getValue(Users.class).getTglLahir());

//        Biodata biodata = new Biodata();
//        biodata.setNamaLengkap(dataSnapshot.getValue(Users.class).getNamaLengkap().toString());
//        biodata.setEmail(user.getEmail().toString());
//        biodata.setNoTelp(dataSnapshot.getValue(Users.class).getNoTelp().toString());
//        biodata.setTglLahir(dataSnapshot.getValue(Users.class).getTglLahir().toString());
//        biodata.setAlamat(dataSnapshot.getValue(Users.class).getAlamat().toString());

//        make_toast("Selamat Datang " + dataSnapshot.getValue(Users.class).getNamaLengkap());
//        make_toast(user.getEmail().toString());


//        tv_nama.setText(dataSnapshot.getValue(Users.class).getNamaLengkap());
//        tv_name.setText(dataSnapshot.getValue(Users.class).getNamaLengkap());
//        tv_email.setText(user.getEmail());
//        tv_noTelp.setText(dataSnapshot.getValue(Users.class).getNoTelp());
//        tv_tanggalLahir.setText(dataSnapshot.getValue(Users.class).getTglLahir());
//        tv_alamat.setText(dataSnapshot.getValue(Users.class).getAlamat());

//        String nama     = dataSnapshot.getValue(Users.class).getNamaLengkap();
//        String email    = user.getEmail();
//        String noTelp   = dataSnapshot.getValue(Users.class).getNoTelp();
//        String tglLahir = dataSnapshot.getValue(Users.class).getTglLahir();
//        String alamat   = dataSnapshot.getValue(Users.class).getAlamat();

//        Bundle bundle = new Bundle();
//        bundle.putString("nama",        dataSnapshot.getValue(Users.class).getNamaLengkap().toString());
//        bundle.putString("email",       user.getEmail().toString());
//        bundle.putString("noTelp",      dataSnapshot.getValue(Users.class).getNoTelp().toString());
//        bundle.putString("tglLahir",    dataSnapshot.getValue(Users.class).getTglLahir().toString());
//        bundle.putString("alamat",      dataSnapshot.getValue(Users.class).getAlamat().toString());

//        sayaOverviewFragment.bNama      = dataSnapshot.getValue(Users.class).getNamaLengkap().toString();
//        sayaOverviewFragment.bEmail     = user.getEmail().toString();
//        sayaOverviewFragment.bNoTelp    = dataSnapshot.getValue(Users.class).getNoTelp().toString();
//        sayaOverviewFragment.bTglLahir  = dataSnapshot.getValue(Users.class).getTglLahir().toString();
//        sayaOverviewFragment.bAlamat    = dataSnapshot.getValue(Users.class).getAlamat().toString();

//        new SayaOverviewFragment().setArguments(bundle);

    }

}
