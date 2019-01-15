package com.dev.invinity.rentalyuk.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.invinity.rentalyuk.Activity.BarangActivity;
import com.dev.invinity.rentalyuk.Fragment.HomeFragment;
import com.dev.invinity.rentalyuk.Models.BarangModel;
import com.dev.invinity.rentalyuk.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BarangAdapter extends RecyclerView.Adapter<HomeFragment.BarangAdapter.MyViewHolder> {
    private Context context;
    private List<BarangModel> barangList;

    public BarangAdapter(Context context, List<BarangModel> barangList) {
        this.context = context;
        this.barangList = barangList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price;
        public ImageView thumbnail;
        public CardView cardView;


        public MyViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.card_view);
            name = view.findViewById(R.id.title);
            price = view.findViewById(R.id.price);
            thumbnail = view.findViewById(R.id.thumbnail);
        }
    }

    @NonNull
    @Override
    public HomeFragment.BarangAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.barang_row, parent, false);

        return new HomeFragment.BarangAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeFragment.BarangAdapter.MyViewHolder holder, final int position) {
        final BarangModel barangModel = barangList.get(position);
        holder.name.setText(barangModel.getNamaBarang());
        holder.price.setText("Rp. " + barangModel.getHargaBarang());



        Picasso.with(context)
                .load(barangModel.getImgURLBarang())
                .placeholder(R.drawable.logo)
                .into(holder.thumbnail);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BarangActivity.class);
                intent.putExtra("Key", barangModel.getKey());
                intent.putExtra("ImgURLBarang", barangModel.getImgURLBarang());
                intent.putExtra("NamaBarang", barangModel.getNamaBarang());
                intent.putExtra("HargaBarang", barangModel.getHargaBarang());
                intent.putExtra("DeskripsiBarang", barangModel.getDeskripsiBarang());
                intent.putExtra("StokBarang", barangModel.getStokBarang());
                intent.putExtra("keyPemilikBarang", barangModel.getKeyPemilikBarang());

                FirebaseDatabase.getInstance()
                        .getReference()
                        .child("Barang")
                        .child(barangModel.getKeyPemilikBarang())
                        .child(barangModel.getKey())
                        .child("dilihat")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    String terakhirDilihat = dataSnapshot.getValue(String.class);
                                    String updateTerakhirDilihat = String.valueOf(Integer.parseInt(terakhirDilihat) + 1);
                                    FirebaseDatabase.getInstance()
                                            .getReference()
                                            .child("Barang")
                                            .child(barangModel.getKeyPemilikBarang())
                                            .child(barangModel.getKey())
                                            .child("dilihat")
                                            .setValue(updateTerakhirDilihat);
                                } else {
                                    String terakhirDilihat = "0";
                                    String updateTerakhirDilihat = String.valueOf(Integer.parseInt(terakhirDilihat) + 1);
                                    FirebaseDatabase.getInstance()
                                            .getReference()
                                            .child("Barang")
                                            .child(barangModel.getKeyPemilikBarang())
                                            .child(barangModel.getKey())
                                            .child("dilihat")
                                            .setValue(updateTerakhirDilihat);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                context.startActivity(intent);
            }
        });

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BarangActivity.class);
                intent.putExtra("Key", barangModel.getKey());
                intent.putExtra("ImgURLBarang", barangModel.getImgURLBarang());
                intent.putExtra("NamaBarang", barangModel.getNamaBarang());
                intent.putExtra("HargaBarang", barangModel.getHargaBarang());
                intent.putExtra("DeskripsiBarang", barangModel.getDeskripsiBarang());
                intent.putExtra("StokBarang", barangModel.getStokBarang());
                intent.putExtra("keyPemilikBarang", barangModel.getKeyPemilikBarang());

                FirebaseDatabase.getInstance()
                        .getReference()
                        .child("Barang")
                        .child(barangModel.getKeyPemilikBarang())
                        .child(barangModel.getKey())
                        .child("dilihat")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    String terakhirDilihat = dataSnapshot.getValue(String.class);
                                    String updateTerakhirDilihat = String.valueOf(Integer.parseInt(terakhirDilihat) + 1);
                                    FirebaseDatabase.getInstance()
                                            .getReference()
                                            .child("Barang")
                                            .child(barangModel.getKeyPemilikBarang())
                                            .child(barangModel.getKey())
                                            .child("dilihat")
                                            .setValue(updateTerakhirDilihat);
                                } else {
                                    String terakhirDilihat = "0";
                                    String updateTerakhirDilihat = String.valueOf(Integer.parseInt(terakhirDilihat) + 1);
                                    FirebaseDatabase.getInstance()
                                            .getReference()
                                            .child("Barang")
                                            .child(barangModel.getKeyPemilikBarang())
                                            .child(barangModel.getKey())
                                            .child("dilihat")
                                            .setValue(updateTerakhirDilihat);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return barangList.size();
    }

    private void make_toast(String pesan){
        Toast.makeText(context, pesan, Toast.LENGTH_SHORT).show();
    }
}
