package com.dev.invinity.rentalyuk.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.invinity.rentalyuk.Activity.RentalinBarangActivity;
import com.dev.invinity.rentalyuk.Models.BarangModel;
import com.dev.invinity.rentalyuk.R;
import com.google.android.gms.tasks.OnFailureListener;
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

import java.util.List;

public class TabBarangkuAdapter extends RecyclerView.Adapter<TabBarangkuAdapter.MyViewHolder> {
    ////////////////////////     Start Deklarasi Variable     /////////////////////////

    private Context context;
    private List<BarangModel> barangList;
    private String actionPilih = "Booked";

    //firebase
    private StorageReference storageReference;
    private DatabaseReference databaseReference, databaseCek;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth.AuthStateListener authStateListener;
    private String userID;

    private Boolean status = null;

    ////////////////////////     End Deklarasi Variable     //////////////////////////

    public TabBarangkuAdapter(Context context, List<BarangModel> barangList) {
        this.context = context;
        this.barangList = barangList;
    }

    @NonNull
    @Override
    public TabBarangkuAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tab_barangku_row, parent, false);

        return new TabBarangkuAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final BarangModel barangModel = barangList.get(position);
        holder.name.setText(barangModel.getNamaBarang());
        holder.price.setText("Rp. " + barangModel.getHargaBarang());
        holder.stok.setText("Stok : " + barangModel.getStokBarang());
        Picasso.with(context)
                .load(barangModel.getImgURLBarang())
                .placeholder(R.drawable.logo)
                .into(holder.thumbnail);

        holder.button_editBarangku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RentalinBarangActivity.class);
                intent.putExtra("EDIT", "EDIT COY");
                intent.putExtra("Key", barangModel.getKey());
                intent.putExtra("ImgURLBarang", barangModel.getImgURLBarang());
                intent.putExtra("NamaBarang", barangModel.getNamaBarang());
                intent.putExtra("HargaBarang", barangModel.getHargaBarang());
                intent.putExtra("DeskripsiBarang", barangModel.getDeskripsiBarang());
                intent.putExtra("StokBarang", barangModel.getStokBarang());
                intent.putExtra("keyPemilikBarang", barangModel.getKeyPemilikBarang());
                context.startActivity(intent);

            }
        });

        holder.button_hapusBarangku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(context);
                }
                builder.setTitle("Peringatan")
                        .setCancelable(false)
                        .setMessage("Hapus Barang " + holder.name.getText() + " ?")

                        .setPositiveButton("HAPUS", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //cek apakah barang lagi di rental atau engga
                                status = false;
                                databaseCek.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                            if (ds.child("id_barang").getValue(String.class).equalsIgnoreCase(barangModel.getKey())) {
                                                status = true;
                                            }

                                        }

                                        if (status == false) {
                                            databaseReference.child(barangModel.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    make_toast("Barang berhasil Di Hapus!");
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    make_toast("Errorr : " + e.getMessage());
                                                }
                                            });
                                        } else {
                                            make_alertDialog("Peringatan!", "Barang Tidak bisa dihapus karena barang sedang di rental!");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                            }
                        })
                        .setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return barangList.size();
    }

    private void make_toast(String pesan) {
        Toast.makeText(context, pesan, Toast.LENGTH_SHORT).show();
    }

    private void make_alertDialog(String title, String message) {
        final AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price, stok;
        public ImageView thumbnail;
        public Button button_editBarangku, button_hapusBarangku;
        public CardView cardView;

        @SuppressLint("ResourceAsColor")
        public MyViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.barang_itemBarangku);
            name = itemView.findViewById(R.id.barang_namaBarangku);
            price = itemView.findViewById(R.id.barang_hargaBarangku);
            stok = itemView.findViewById(R.id.barang_stokBarangku);
            thumbnail = itemView.findViewById(R.id.barang_thumbnailBarangku);
            button_editBarangku = itemView.findViewById(R.id.button_editBarangku);
            button_hapusBarangku = itemView.findViewById(R.id.button_hapusBarangku);

            //firebase
            firebaseAuth = FirebaseAuth.getInstance();
            user = firebaseAuth.getCurrentUser();
            userID = user.getUid();
            databaseReference = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Barang")
                    .child(userID);
            databaseCek = FirebaseDatabase.getInstance()
                    .getReference("Rental/pemilik/")
                    .child(userID);
        }
    }
}
