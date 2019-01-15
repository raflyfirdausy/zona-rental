package com.dev.invinity.rentalyuk.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev.invinity.rentalyuk.Models.BarangModel;
import com.dev.invinity.rentalyuk.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TabStatusPenyewaAdapter extends RecyclerView.Adapter<TabStatusPenyewaAdapter.MyViewHolder> {

////////////////////////     Start Deklarasi Variable     /////////////////////////

    private Context context;
    private List<BarangModel> barangList;

////////////////////////     End Deklarasi Variable     //////////////////////////

    public TabStatusPenyewaAdapter(Context context, List<BarangModel> barangList) {
        this.context = context;
        this.barangList = barangList;
    }

    @NonNull
    @Override
    public TabStatusPenyewaAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tab_status_penyewa_row, parent, false);

        return new TabStatusPenyewaAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final BarangModel barangModel = barangList.get(position);
        holder.name.setText(barangModel.getNamaBarang());
        holder.price.setText("Rp. " + barangModel.getHargaBarang());


        if (barangModel.getStatus().toString().equalsIgnoreCase("Booked")) {
            holder.status.setText("Booked");
            holder.status.setBackgroundResource(R.color.booked);
        } else if (barangModel.getStatus().toString().equalsIgnoreCase("Kembali")) {
            holder.status.setText("Kembali");
            holder.status.setBackgroundResource(R.color.kembali);
        } else if (barangModel.getStatus().toString().equalsIgnoreCase("Disewa")) {
            holder.status.setText("Disewa");
            holder.status.setBackgroundResource(R.color.di_sewa);
        } else if (barangModel.getStatus().toString().equalsIgnoreCase("Cancel")) {
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


    }

    @Override
    public int getItemCount() {
        return barangList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price;
        public ImageView thumbnail;
        public Button button_ulasan;
        public CardView cardView;
        public TextView status;

        @SuppressLint("ResourceAsColor")
        public MyViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.barang_item_penyewa);
            name = view.findViewById(R.id.barang_namaPenyewa);
            status = view.findViewById(R.id.barang_statusPenyewa);
            price = view.findViewById(R.id.barang_hargaPenyewa);
            thumbnail = view.findViewById(R.id.barang_thumbnailPenyewa);
            button_ulasan = view.findViewById(R.id.button_ulasanPenyewa);
        }
    }
}
