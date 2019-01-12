package com.dev.invinity.rentalyuk.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.bumptech.glide.Glide;
import com.dev.invinity.rentalyuk.Activity.BarangActivity;
import com.dev.invinity.rentalyuk.Models.Barang;
import com.dev.invinity.rentalyuk.Models.BarangModel;
import com.dev.invinity.rentalyuk.Models.BarangModel2;
import com.dev.invinity.rentalyuk.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TabPemilikBarangAdapter extends RecyclerView.Adapter<TabPemilikBarangAdapter.MyViewHolder> {

////////////////////////     Start Deklarasi Variable     /////////////////////////

    private Context context;
    private List<BarangModel2> barangList;
    private String actionPilih = "Booked";

////////////////////////     End Deklarasi Variable     //////////////////////////

    public TabPemilikBarangAdapter(Context context, List<BarangModel2> barangList) {
        this.context = context;
        this.barangList = barangList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price,status;
        public ImageView thumbnail;
        public Button button_edit_status;
        public CardView cardView;

        @SuppressLint("ResourceAsColor")
        public MyViewHolder(View itemView) {
            super(itemView);
            cardView            = (CardView) itemView.findViewById(R.id.barang_itemPemilik);
            name                = itemView.findViewById(R.id.barang_namaPemilik);
            price               = itemView.findViewById(R.id.barang_hargaPemilik);
            thumbnail           = itemView.findViewById(R.id.barang_thumbnailPemilik);
            status              = itemView.findViewById(R.id.barang_statusPemilik);
            button_edit_status  = itemView.findViewById(R.id.button_edit_statusPemilik);
        }
    }

    @NonNull
    @Override
    public TabPemilikBarangAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tab_pemilik_barang_row, parent, false);

        return new TabPemilikBarangAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        final BarangModel2 barangModel = barangList.get(position);
        holder.name.setText(barangModel.getNamaBarang());
        holder.price.setText("Rp. " + barangModel.getHargaBarang());
//        final String[] listItem = new String[] {"Booked","Kembali", "Disewa", "Cancel"};
        if(barangModel.getStatus().toString().equalsIgnoreCase("Booked")){
            holder.status.setText("Booked");
            holder.status.setBackgroundResource(R.color.booked);
        } else if (barangModel.getStatus().toString().equalsIgnoreCase("Kembali")){
            holder.status.setText("Kembali");
            holder.status.setBackgroundResource(R.color.kembali);
        } else if (barangModel.getStatus().toString().equalsIgnoreCase("Disewa")){
            holder.status.setText("Disewa");
            holder.status.setBackgroundResource(R.color.di_sewa);
        } else if (barangModel.getStatus().toString().equalsIgnoreCase("Cancel")){
            holder.status.setText("Cancel");
            holder.status.setBackgroundResource(R.color.cancel);
        } else {
            holder.status.setText("Belum Di Konfirmasi");
            holder.status.setBackgroundResource(R.color.black);
        }


        Picasso.with(context)
                .load(barangModel.getImgURLBarang())
                .placeholder(R.drawable.logo)
                .into(holder.thumbnail);

        holder.button_edit_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, barangModel.getNamaBarang()+"\n"+barangModel.getKeyPemilikBarang().toString()+"\n" +barangModel.getKeyTransaksi().toString(), Toast.LENGTH_LONG).show();

                final String[] listItem = new String[] {"Booked","Kembali", "Disewa", "Cancel"};
                final AlertDialog.Builder builder;
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(context);
                }
                builder.setTitle("Pilih Aksi")
                        .setSingleChoiceItems(listItem, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, final int which) {
                                actionPilih = listItem[which].toString();
//                                make_toast("Proses update...");
                                DatabaseReference databaseReference;
                                databaseReference = FirebaseDatabase.getInstance().getReference("Rental/pemilik/" +
                                        barangModel.getKeyPemilikBarang().toString() + "/" + barangModel.getKeyTransaksi().toString());
                                databaseReference.child("status").setValue(actionPilih)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(context, "Success Update!", Toast.LENGTH_LONG).show();
                                        dialog.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Gagal Memperbarui !", Toast.LENGTH_LONG).show();
                                    }
                                });

                            }
                        })
                        .setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create()
                        .show();


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
