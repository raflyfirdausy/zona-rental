package com.dev.invinity.rentalyuk.Fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dev.invinity.rentalyuk.Activity.MainActivity;
import com.dev.invinity.rentalyuk.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private EditText et_email, et_password;
    private Button btn_login;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private InterstitialAd interstitialAd;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        View v          = inflater.inflate(R.layout.fragment_login, container, false);
        View v          = inflater.inflate(R.layout.fragment_login_v2, container, false);
        et_email        = (EditText) v.findViewById(R.id.et_email);
        et_password     = (EditText) v.findViewById(R.id.et_password);
        btn_login       = (Button) v.findViewById(R.id.btn_login);

        //firebase
        firebaseAuth    = FirebaseAuth.getInstance();

        //Fragment
        fragmentManager = getActivity().getSupportFragmentManager();

        //onclick
        btn_login.setOnClickListener(this);
        v.findViewById(R.id.tv_daftardisini).setOnClickListener(this);

        //admob
//        MobileAds.initialize(getActivity(),"ca-app-pub-3251522909904904~6459332883");

        //Banner ad
//        AdView mAdView = (AdView) v.findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_daftardisini:
                //Fragment
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                //Untuk inisialisasi fragment pertama kali
                fragmentManager.beginTransaction()
                        .replace(R.id.auth_content, new DaftarFragment())
                        .commit();
                break;

            case R.id.btn_login:

                if((TextUtils.isEmpty(et_email.getText().toString())) || (TextUtils.isEmpty(et_password.getText().toString()))){
                    final AlertDialog.Builder builder;
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                        builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Light_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(getActivity());
                    }

                    builder.setTitle("Peringatan!")
                            .setMessage("username atau password masih Kosong!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                } else {

                    //progressDialog
                    progressDialog = ProgressDialog.show(getActivity(),
                            "Tunggu Beberapa Saat",
                            "Proses Login ...",
                            true);



                    firebaseAuth.signInWithEmailAndPassword(et_email.getText().toString(),
                            et_password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    progressDialog.dismiss();

                                    if(task.isSuccessful()){
                                        Intent i = new Intent(getActivity(), MainActivity.class);
                                        startActivity(i);
                                        getActivity().finish();
                                    } else {
                                        make_toast("Login gagal : " + task.getException().getMessage());
                                    }
                                }
                            });

                }

                break;

        }
    }

    private void make_toast(String  pesan){
        Toast.makeText(getActivity(), pesan, Toast.LENGTH_SHORT).show();
    }

}
