package com.dev.invinity.rentalyuk.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.invinity.rentalyuk.Models.BarangModel;
import com.dev.invinity.rentalyuk.Models.Users;
import com.dev.invinity.rentalyuk.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class BarangActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private String title, imgURL;
    private TextView tv_namaBarang,
            tv_hargaBarang,
            tv_stokBarang,
            tv_terental,
            tv_dilihat,
            tv_deskripsiBarang,
            tv_namaToko,
            tv_alamatToko,
            tv_noHpToko;

    private android.support.v7.widget.AppCompatButton btn_rental;

    private String keyBarang, keyPemilikBarang;;
    private ImageView img_barang;
    private CircleImageView img_FotoProfilePerental;

    //firebase
    private StorageReference storageReference;
    private DatabaseReference databaseReference, databaseReferencePemilik, databaseReferencePerental, databaseGetStok;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth.AuthStateListener authStateListener;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barang);
        toolbar                 = (Toolbar) findViewById(R.id.toolbarBarang);
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        appBarLayout            = findViewById(R.id.appBarLayout);
        tv_namaBarang           = findViewById(R.id.tv_namaBarang);
        tv_hargaBarang          = findViewById(R.id.tv_hargaBarang);
        tv_stokBarang           = findViewById(R.id.tv_stokBarang);
        tv_terental             = findViewById(R.id.tv_terental);
        tv_dilihat              = findViewById(R.id.tv_dilihat);
        tv_deskripsiBarang      = findViewById(R.id.tv_deskripsiBarang);

        img_FotoProfilePerental = findViewById(R.id.img_FotoProfilePerental);
        tv_namaToko             = findViewById(R.id.tv_namaToko);
        tv_alamatToko           = findViewById(R.id.tv_alamatToko);
        tv_noHpToko             = findViewById(R.id.tv_noHpToko);

        img_barang              = findViewById(R.id.img_barang);
        btn_rental              = (android.support.v7.widget.AppCompatButton) findViewById(R.id.btn_rental);

        tv_namaBarang.setVisibility(View.VISIBLE);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //firebase
        firebaseAuth        = FirebaseAuth.getInstance();
        user                = firebaseAuth.getCurrentUser();
        userID              = user.getUid();
        databaseReference   = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users")
                .child(getIntent().getStringExtra("keyPemilikBarang"));

        databaseReferencePemilik = FirebaseDatabase.getInstance()
                .getReference()
                .child("Rental")
                .child("pemilik")
                .child(getIntent().getStringExtra("keyPemilikBarang"));

        databaseReferencePerental = FirebaseDatabase.getInstance()
                .getReference()
                .child("Rental")
                .child("perental")
                .child(userID);

        databaseGetStok         = FirebaseDatabase.getInstance().getReference("Barang");


        btn_rental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Show_dialog("Rental Barang " + getIntent().getStringExtra("NamaBarang") + " ?");
            }
        });

        setData();

    }

    private void setData(){
        if(getIntent().hasExtra("Key")){
            //keyBarang
            keyBarang   = getIntent().getStringExtra("Key").toString();
            title       = getIntent().getStringExtra("NamaBarang").toString();

            tv_namaBarang.setText(getIntent().getStringExtra("NamaBarang").toString());
            tv_hargaBarang.setText("Rp. " + getIntent().getStringExtra("HargaBarang").toString());
            tv_deskripsiBarang.setText(getIntent().getStringExtra("DeskripsiBarang").toString());
            tv_stokBarang.setText(getIntent().getStringExtra("StokBarang").toString());
            imgURL  = getIntent().getStringExtra("ImgURLBarang").toString();

            Picasso.with(BarangActivity.this)
                    .load(imgURL)
                    .placeholder(R.drawable.noimage)
                    .into(img_barang);

            appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                int scrollRange = -1;

                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    if (scrollRange == -1) {
                        scrollRange = appBarLayout.getTotalScrollRange();
                    }

                    if (scrollRange + verticalOffset == 0) {
                        collapsingToolbarLayout.setTitle(title);
                    } else  {
                        collapsingToolbarLayout.setTitle(" ");
                    }
                }
            });
        }

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                tv_namaToko.setText(dataSnapshot.getValue(Users.class).getNamaLengkap().toString());
                tv_alamatToko.setText(dataSnapshot.getValue(Users.class).getAlamat().toString());
                tv_noHpToko.setText(dataSnapshot.getValue(Users.class).getNoTelp().toString());


                if(!dataSnapshot.getValue(Users.class).getImgURL()
                        .toString().equalsIgnoreCase("-")){
                    Picasso.with(getApplicationContext())
                            .load(dataSnapshot.getValue(Users.class).getImgURL())
                            .placeholder(R.drawable.logo)
                            .into(img_FotoProfilePerental);
                }

                tv_noHpToko.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tv_noHpToko.getText().toString()));
                        startActivity(intent);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void Show_dialog(String pesan){
        final AlertDialog.Builder builder;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            builder = new AlertDialog.Builder(BarangActivity.this, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(BarangActivity.this);
        }

        builder.setMessage(pesan)
                .setPositiveButton("Rental", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        proses_rental();
                        dialog_berhasil("Info", "Proses Rental Berhasil\nKembali ke Menu Sebelumnya ?");
                    }
                })
                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(BarangActivity.this, "Batal", Toast.LENGTH_LONG).show();
                    }
                })
                .show();
    }

    private void dialog_berhasil(String title, String message){
        final AlertDialog.Builder builder;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            builder = new AlertDialog.Builder(BarangActivity.this, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(BarangActivity.this);
        }

        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       finish();
                    }
                })
                .setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    private void proses_rental(){

        databaseGetStok.child(getIntent().getStringExtra("keyPemilikBarang"))
                .child(getIntent().getStringExtra("Key")).child("stokBarang")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        DatabaseReference x = FirebaseDatabase.getInstance().getReference("Barang")
                                .child(getIntent().getStringExtra("keyPemilikBarang"))
                                .child(getIntent().getStringExtra("Key"))
                                .child("stokBarang");

                        int stokBaru = Integer.parseInt(dataSnapshot.getValue(String.class)) - 1;
                        x.setValue(String.valueOf(stokBaru)).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                tv_stokBarang.setText(String.valueOf(Integer.parseInt(tv_stokBarang.getText().toString()) - 1));
                            }
                        });

                        Map pemilik         = new HashMap();
                        pemilik.put("id_barang", getIntent().getStringExtra("Key"));
                        pemilik.put("id_perental", userID);
                        pemilik.put("status", "belum di set");
                        databaseReferencePemilik.push().setValue(pemilik);

                        Map perental = new HashMap();
                        perental.put("id_barang", getIntent().getStringExtra("Key"));
                        perental.put("id_pemilik", getIntent().getStringExtra("keyPemilikBarang"));

                        databaseReferencePerental.push().setValue(perental);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

}
