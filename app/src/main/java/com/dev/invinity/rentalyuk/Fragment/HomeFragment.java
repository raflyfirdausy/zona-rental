package com.dev.invinity.rentalyuk.Fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;
import com.dev.invinity.rentalyuk.Activity.MainActivity;
import com.dev.invinity.rentalyuk.Adapter.BarangAdapter;
import com.dev.invinity.rentalyuk.Models.Barang;
import com.dev.invinity.rentalyuk.Models.BarangModel;
import com.dev.invinity.rentalyuk.R;
import com.dev.invinity.rentalyuk.Adapter.ViewPagerAdapter;
import com.dev.invinity.rentalyuk.app.MyApplication;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;


//        List<String> images=new ArrayList<>();
//        images.add("https://png.pngtree.com/thumb_back/fw800/back_pic/03/77/42/9157c0064c4312c.jpg");
//        images.add("https://png.pngtree.com/thumb_back/fw800/back_pic/04/25/26/235837da0907eca.jpg");
//        images.add("https://png.pngtree.com/thumb_back/fw800/back_pic/03/62/26/6857aa748e42084.jpg");
//        images.add("https://png.pngtree.com/thumb_back/fw800/back_pic/00/03/18/86561db3f646925.jpg");


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

////////////////////////     Start Deklarasi Variable     /////////////////////////

//    private static final String TAG = HomeFragment.class.getSimpleName();
//    private static final String URL = "https://api.androidhive.info/json/movies_2017.json";
    private RecyclerView recyclerView;
    private List<BarangModel> ITEMLIST;
    private com.dev.invinity.rentalyuk.Adapter.BarangAdapter barangAdapter;
    private String keyPemilikBarang;
    private ViewPager vp_header;

    private LinearLayout LL_konten;
    private TextView tv_tulisanKategori;

    //firebase
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth.AuthStateListener authStateListener;
    private String userID;

////////////////////////     End Deklarasi Variable     //////////////////////////

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        ViewPager viewPager = (ViewPager) v.findViewById(R.id.view_pager);

        recyclerView    = v.findViewById(R.id.konten_rekomendasi);
        ITEMLIST        = new ArrayList<>();
        barangAdapter   = new com.dev.invinity.rentalyuk.Adapter.BarangAdapter(getActivity(), ITEMLIST);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(0), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(barangAdapter);
        recyclerView.setNestedScrollingEnabled(false);

        //HIDEEEEE
        LL_konten            = (LinearLayout) v.findViewById(R.id.LL_konten);
        tv_tulisanKategori   = (TextView) v.findViewById(R.id.tv_tulisanKategori);

        viewPager.setVisibility(View.VISIBLE);
        LL_konten.setVisibility(View.GONE);
        tv_tulisanKategori.setVisibility(View.GONE);

        //firebase
        databaseReference   = FirebaseDatabase.getInstance().getReference("Barang");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                BarangModel barangModel = null;
                ITEMLIST.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    keyPemilikBarang = ds.getKey().toString();
                    for(DataSnapshot DS : ds.getChildren()){
                        barangModel = DS.getValue(BarangModel.class);
                        barangModel.setKeyPemilikBarang(keyPemilikBarang);
                        barangModel.setKey(DS.getKey().toString());
                        ITEMLIST.add(barangModel);
                    }
                }
                barangAdapter   = new com.dev.invinity.rentalyuk.Adapter.BarangAdapter(getActivity(), ITEMLIST);
                recyclerView.setAdapter(barangAdapter);
//                make_toast(String.valueOf(barangAdapter.getItemCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity());
        viewPager.setAdapter(viewPagerAdapter);

       // ambil_data();


        v.findViewById(R.id.kategori1).setOnClickListener(this);
        v.findViewById(R.id.kategori2).setOnClickListener(this);
        v.findViewById(R.id.kategori3).setOnClickListener(this);
        v.findViewById(R.id.kategori4).setOnClickListener(this);
        v.findViewById(R.id.kategori5).setOnClickListener(this);
        v.findViewById(R.id.kategori6).setOnClickListener(this);

        return  v;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.kategori1:
                Toast.makeText(getActivity(),"Kategori 1",Toast.LENGTH_SHORT).show();
                break;

            case R.id.kategori2:
                Toast.makeText(getActivity(),"Kategori 2",Toast.LENGTH_SHORT).show();
                break;

            case R.id.kategori3:
                Toast.makeText(getActivity(),"Kategori 3",Toast.LENGTH_SHORT).show();
                break;

            case R.id.kategori4:
                Toast.makeText(getActivity(),"Kategori 4",Toast.LENGTH_SHORT).show();
                break;

            case R.id.kategori5:
                Toast.makeText(getActivity(),"Kategori 5",Toast.LENGTH_SHORT).show();
                break;


            case R.id.kategori6:
                Toast.makeText(getActivity(),"Kategori 6",Toast.LENGTH_SHORT).show();
                break;

        }
    }

//    private void ambil_data() {
//
//        JsonArrayRequest request = new JsonArrayRequest(URL,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        if (response == null) {
//                            Toast.makeText(getActivity(), "Couldn't fetch the store items! Pleas try again.", Toast.LENGTH_LONG).show();
//                            return;
//                        }
//
//                        List<Barang> items = new Gson().fromJson(response.toString(), new TypeToken<List<Barang>>() {
//                        }.getType());
//
//                        itemsList.clear();
//                        itemsList.addAll(items);
//
//                        // refreshing recycler view
//                        mAdapter.notifyDataSetChanged();
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                // error in getting json
//                Log.e(TAG, "Error: " + error.getMessage());
////                Toast.makeText(getActivity(), "Error: " + error.getMessage().toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        MyApplication.getInstance().addToRequestQueue(request);
//    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    public static class BarangAdapter extends RecyclerView.Adapter<BarangAdapter.MyViewHolder>{
        private Context context;
        private List<Barang> barangList;

        public static class MyViewHolder extends RecyclerView.ViewHolder {
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


        public BarangAdapter(Context context, List<Barang> barangList) {
            this.context = context;
            this.barangList = barangList;
        }



        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.barang_row, parent, false);


               return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final Barang barang = barangList.get(position);
            holder.name.setText(barang.getTitle());
            holder.price.setText(barang.getPrice());

            Picasso.with(context)
                    .load(barang.getImage())
                    .placeholder(R.drawable.logo)
                    .into(holder.thumbnail);

            holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    Toast.makeText(getActivity(),
//                            position + " | " + barang.getTitle() + " | " + barang.getPrice()
//                            ,Toast.LENGTH_SHORT).show();
                }
            });


            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(getActivity(),
//                            position + " | " + barang.getTitle() + " | " + barang.getPrice()
//                            ,Toast.LENGTH_SHORT).show();
                }
            });

        }

        @Override
        public int getItemCount() {
            return barangList.size();
        }

    }

    private void make_toast(String pesan){
        Toast.makeText(getActivity(), pesan, Toast.LENGTH_SHORT).show();
    }

    private void make_alertDialog(String title, String message){
        final AlertDialog.Builder builder;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getActivity());
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
}
