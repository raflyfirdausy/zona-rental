package com.dev.invinity.rentalyuk.Fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.invinity.rentalyuk.Activity.AuthActivity;
import com.dev.invinity.rentalyuk.Activity.MainActivity;
import com.dev.invinity.rentalyuk.Activity.RentalinBarangActivity;
import com.dev.invinity.rentalyuk.Models.Biodata;
import com.dev.invinity.rentalyuk.Models.Users;
import com.dev.invinity.rentalyuk.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class SayaOverviewFragment extends Fragment {

    private ImageButton imgBtnSetting, imgBtnJualBarang;
    private FragmentManager fragmentManager;
    private String uid;
    boolean status_login;
    LinearLayout linlay1;

    public TextView tv_name,tv_nama, tv_email, tv_noTelp, tv_tanggalLahir, tv_alamat, tv_jenisKelamin;
    private CircleImageView img_FotoProfileOverview;

    //firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference databaseReference;
    private String userID;

    private boolean adaGambar;



    public SayaOverviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_saya_overview, container, false);



        tv_name         = (TextView) v.findViewById(R.id.tv_name);
        tv_nama         = (TextView) v.findViewById(R.id.tv_nama);
        tv_email        = (TextView) v.findViewById(R.id.tv_email);
        tv_noTelp       = (TextView) v.findViewById(R.id.tv_noTelp);
        tv_tanggalLahir = (TextView) v.findViewById(R.id.tv_tanggalLahir);
        tv_alamat       = (TextView) v.findViewById(R.id.tv_alamat);
        tv_jenisKelamin = (TextView) v.findViewById(R.id.tv_jenisKelamin);
        img_FotoProfileOverview = (CircleImageView) v.findViewById(R.id.img_FotoProfileOverview);

        linlay1         = (LinearLayout) v.findViewById(R.id.linlay1);
        linlay1.setVisibility(View.GONE);

        //firebase
        firebaseAuth        = FirebaseAuth.getInstance();
        user                = firebaseAuth.getCurrentUser();
        userID              = user.getUid();
        databaseReference   = FirebaseDatabase.getInstance()
                                .getReference()
                                .child("Users")
                                .child(userID);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                setBiodata(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Fragment
        fragmentManager = getActivity().getSupportFragmentManager();

        imgBtnSetting = (ImageButton) v.findViewById(R.id.setting_profil);
        imgBtnJualBarang = (ImageButton) v.findViewById(R.id.jual_barang);

        imgBtnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(getActivity(), imgBtnSetting);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu_saya, popupMenu.getMenu());
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.actionEdit:
                                //pindah fragment
                                fragmentManager.beginTransaction()
                                        .replace(R.id.fragment_container, new SayaEditFragment())
                                        .commit();
//                                if(status_login){
//                                    Toast.makeText(getActivity(), user.getEmail(), Toast.LENGTH_SHORT).show();
//                                } else {
//                                    Toast.makeText(getActivity(), "Login disit", Toast.LENGTH_SHORT).show();
//                                }


                                break;

                            case R.id.actionLogout:
                                    firebaseAuth.signOut();
                                    Intent intent = new Intent(getActivity(), AuthActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();

                                break;
                        }

                        return true;
                    }
                });

            }
        });

        imgBtnJualBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), RentalinBarangActivity.class);
                startActivity(i);
            }
        });



        return v;
    }

    public void getBiodata(String nama,
                           String email,
                           String noTelp,
                           String tglLahir,
                           String alamat,
                           String jenisKelamin,
                           String imgURL){

        tv_name.setText(nama);
        tv_nama.setText(nama);
        tv_email.setText(email);
        tv_noTelp.setText(noTelp);
        tv_tanggalLahir.setText(tglLahir);
        tv_alamat.setText(alamat);
        tv_jenisKelamin.setText(jenisKelamin);


        if(!imgURL.equalsIgnoreCase("-")){
            Picasso.with(getActivity())
                    .load(imgURL)
                    .placeholder(R.drawable.logo)
                    .into(img_FotoProfileOverview);
        }



    }

    private void setBiodata(DataSnapshot dataSnapshot){
        getBiodata(
                dataSnapshot.getValue(Users.class).getNamaLengkap().toString(),
                user.getEmail().toString(),
                dataSnapshot.getValue(Users.class).getNoTelp().toString(),
                dataSnapshot.getValue(Users.class).getTglLahir().toString(),
                dataSnapshot.getValue(Users.class).getAlamat().toString(),
                dataSnapshot.getValue(Users.class).getJenisKelamin().toString(),
                dataSnapshot.getValue(Users.class).getImgURL().toString()
        );

    }

}
