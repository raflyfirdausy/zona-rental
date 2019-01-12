package com.dev.invinity.rentalyuk.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.dev.invinity.rentalyuk.Models.jajal;

import com.dev.invinity.rentalyuk.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class jajalActivity extends AppCompatActivity {

    // Write a message to the database
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jajal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button save = (Button) findViewById(R.id.bt_JAJAL);
        final EditText nama = (EditText)findViewById(R.id.et_namaBarang);
        final EditText harga = (EditText)findViewById(R.id.et_harga);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namaCuy = nama.getText().toString();
                String hargaCuy = harga.getText().toString();

                jajal JAJAL = new jajal(namaCuy, hargaCuy);
                databaseReference.child("barang").push().setValue(JAJAL);
            }
        });


    }

}
