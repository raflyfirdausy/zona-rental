package com.dev.invinity.rentalyuk.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.dev.invinity.rentalyuk.Admob.AdManager;
import com.dev.invinity.rentalyuk.Fragment.DaftarFragment;
import com.dev.invinity.rentalyuk.Fragment.HomeFragment;
import com.dev.invinity.rentalyuk.Fragment.LoginFragment;
import com.dev.invinity.rentalyuk.Fragment.SayaOverviewFragment;
import com.dev.invinity.rentalyuk.R;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthActivity extends AppCompatActivity {

    private Fragment fragment;
    private FragmentManager fragmentManager;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        //Fragment
        fragmentManager = getSupportFragmentManager();

        //Untuk inisialisasi fragment pertama kali
        fragmentManager.beginTransaction()
                .replace(R.id.auth_content, new LoginFragment())
                .commit();

        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        AdManager adManager = new AdManager(this, "ca-app-pub-3940256099942544/1033173712");
        adManager.createAd();

        if(user != null){

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

//        if(user != null){
//            if(getIntent().hasExtra("metu") &&
//                    getIntent().getStringExtra("metu").equals("metucuy")){
//                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
//                homeIntent.addCategory( Intent.CATEGORY_HOME );
//                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(homeIntent);
//            } else {
//                Intent intent = new Intent(this, MainActivity.class);
//                startActivity(intent);
//            }
//        }

//        if(getIntent().hasExtra("metu") &&
//                getIntent().getStringExtra("metu").equals("metucuy")){
////            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
////            homeIntent.addCategory( Intent.CATEGORY_HOME );
//////            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////            startActivity(homeIntent);
//            finish();
//        } else {
//            if(user != null){
//                Intent intent = new Intent(this, MainActivity.class);
//                startActivity(intent);
//            }
//        }



        //addmob
//        AdManager adManager = new AdManager(this, "ca-app-pub-3940256099942544/1033173712");
//        adManager.createAd();

    }

//    @Override
//    public void onBackPressed()
//    {
//        super.onBackPressed();
//        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
//        homeIntent.addCategory( Intent.CATEGORY_HOME );
//        startActivity(homeIntent);
//    }
}
