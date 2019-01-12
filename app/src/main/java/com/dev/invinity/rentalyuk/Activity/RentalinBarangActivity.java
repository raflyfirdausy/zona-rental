package com.dev.invinity.rentalyuk.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.dev.invinity.rentalyuk.Models.BarangModel;
import com.dev.invinity.rentalyuk.Models.BarangRental;
import com.dev.invinity.rentalyuk.R;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class RentalinBarangActivity extends AppCompatActivity implements View.OnClickListener {

    private android.support.v7.widget.Toolbar toolbar;
    private EditText et_namaBarang, et_hargaBarang, et_stokBarang, et_deskripsiBarang;
    private Spinner sp_kategoriBarang;
    private Button btn_tambahBarang, btn_rentalinBarang;
    private ImageView img_barang;

    private static final int PICK_IMAGE_CAMERA  = 0;
    private static final int PICK_IMAGE_GALLERY = 1;

    //firebase
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth.AuthStateListener authStateListener;
    private String userID;
    private BarangModel barangModel;

    //image barang
    private Uri selectedImage;

    //progress dialog
    private ProgressDialog progressDialog;

    //download URL
    Uri downloadURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rentalin_barang);

        Context context = this;

        toolbar                 = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);

        btn_tambahBarang        = (Button)   findViewById(R.id.btn_tambahBarang);
        btn_rentalinBarang      = (Button)   findViewById(R.id.btn_rentalinBarang);


        img_barang              = (ImageView)findViewById(R.id.img_barang);
        img_barang.setDrawingCacheEnabled(true);
        img_barang.buildDrawingCache();

        et_namaBarang           = (EditText) findViewById(R.id.et_namaBarang);
        et_hargaBarang          = (EditText) findViewById(R.id.et_hargaBarang);
        et_stokBarang           = (EditText) findViewById(R.id.et_stokBarang);
        et_deskripsiBarang      = (EditText) findViewById(R.id.et_deskripsiBarang);
        sp_kategoriBarang       = (Spinner)  findViewById(R.id.sp_kategoriBarang);

        //cek intent
        if(getIntent().hasExtra("EDIT")){
            btn_tambahBarang.setText("Ganti Gambar Barang");
            btn_rentalinBarang.setText("UPDATE BARANG");

            //tv_namaBarang.setText(getIntent().getStringExtra("NamaBarang").toString());
            Picasso.with(this)
                    .load(getIntent().getStringExtra("ImgURLBarang"))
                    .placeholder(R.drawable.logo)
                    .into(img_barang);
            et_namaBarang.setText(getIntent().getStringExtra("NamaBarang"));
            et_hargaBarang.setText(getIntent().getStringExtra("HargaBarang"));
            et_stokBarang.setText(getIntent().getStringExtra("StokBarang"));
            et_deskripsiBarang.setText(getIntent().getStringExtra("DeskripsiBarang"));

        }

        //firebase
        firebaseAuth        = FirebaseAuth.getInstance();
        user                = firebaseAuth.getCurrentUser();
        userID              = user.getUid();
        databaseReference   = FirebaseDatabase.getInstance()
                                .getReference()
                                .child("Barang")
                                .child(userID);

        storageReference    = FirebaseStorage.getInstance()
                                .getReference()
                                .child("Barang")
                                .child(userID);

        progressDialog      = new ProgressDialog(context);

        btn_tambahBarang.setOnClickListener(this);
        btn_rentalinBarang.setOnClickListener(this);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RentalinBarangActivity.this, MainActivity.class);
                i.putExtra("pindahFragment", "fragmentSayaOverview");
                startActivity(i);
                finish();
            }
        });

    }

    public void make_toast(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_tambahBarang :
                    cameraOrgallery();
                break;

            case R.id.btn_rentalinBarang :
                if(getIntent().hasExtra("EDIT")){
                    updateBarang();
                } else {
                    rentalinBarang();
                }

                break;
        }
    }

    private void cameraOrgallery(){
        final CharSequence[] pilihan = { "Kamera", "Gallery","Batal" };

        final AlertDialog.Builder builder;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }

        builder.setTitle("Pilih Gambar")
                .setItems(pilihan, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if(pilihan[item].equals("Kamera")){

                            Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(takePicture, PICK_IMAGE_CAMERA);//zero can be replaced with any action code

                        } else if (pilihan[item].equals("Gallery")){
//                            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
//                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                            startActivityForResult(pickPhoto , 1);//one can be replaced with any action code

                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(intent, PICK_IMAGE_GALLERY);
                        } else {
                            dialog.dismiss();
                        }
                    }
                })
                .show();
    }


    private void updateBarang(){
        if(TextUtils.isEmpty(et_namaBarang.getText().toString())
                || TextUtils.isEmpty(et_hargaBarang.getText().toString())
                || TextUtils.isEmpty(et_stokBarang.getText().toString())
                || TextUtils.isEmpty(et_deskripsiBarang.getText().toString())
                || img_barang.getDrawable() == null){
            make_alertDialog("Peringatan!", "Masih Ada data yang Kosong !");

        } else if (img_barang.getDrawable().getConstantState() ==
                getResources().getDrawable( R.drawable.noimage).getConstantState()){
            make_alertDialog("Peringatan!", "Kamu belum memilih gambar !");

        } else {
//            make_alertDialog("apa", getIntent().getStringExtra("Key"));
            Bitmap bitmap               = ((BitmapDrawable) img_barang.getDrawable()).getBitmap();
            ByteArrayOutputStream baos  =  new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            final StorageReference ref  = storageReference.child(System.currentTimeMillis() + ".jpg");
            UploadTask uploadTask       = ref.putBytes(data);
            progressDialog.setMessage("Loading ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            Task<Uri> urlTask   = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        downloadURL = task.getResult();
                        //    save ke database cuk
                        BarangRental barangRental   = new BarangRental(
                                et_namaBarang.getText().toString(),
                                et_hargaBarang.getText().toString(),
                                et_stokBarang.getText().toString(),
                                et_deskripsiBarang.getText().toString(),
                                sp_kategoriBarang.getSelectedItem().toString(),
                                downloadURL.toString()
                        );
                        String keyBarang = getIntent().getStringExtra("Key");
                        databaseReference.child(keyBarang).setValue(barangRental)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();

                                final AlertDialog.Builder builder;
                                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                                    builder = new AlertDialog.Builder(RentalinBarangActivity.this,
                                            android.R.style.Theme_Material_Light_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(RentalinBarangActivity.this);
                                }

                                builder.setTitle("Info")
                                        .setMessage("barang berhasil Di Update !")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent i = new Intent(RentalinBarangActivity.this, MainActivity.class);
                                                i.putExtra("pindahFragment", "TransaksiFragment");
                                                startActivity(i);
                                                finish();
                                            }
                                        })
                                        .show();
                            }
                        });
                    }
                }
            });
        }
    }

    private void rentalinBarang(){
        if(TextUtils.isEmpty(et_namaBarang.getText().toString())
                || TextUtils.isEmpty(et_hargaBarang.getText().toString())
                || TextUtils.isEmpty(et_stokBarang.getText().toString())
                || TextUtils.isEmpty(et_deskripsiBarang.getText().toString())
                || img_barang.getDrawable() == null){
            make_alertDialog("Peringatan!", "Masih Ada data yang Kosong !");

        } else if (img_barang.getDrawable().getConstantState() ==
                getResources().getDrawable( R.drawable.noimage).getConstantState()){
            make_alertDialog("Peringatan!", "Kamu belum memilih gambar !");

        } else {
            //upload to firebase cuk
//            make_alertDialog("Data yang diinputkan", "Lagi dibuat proses insertnya\n\n" +
//                    et_namaBarang.getText().toString() + " | " +
//                    et_hargaBarang.getText().toString() + " | " +
//                    et_stokBarang.getText().toString() + " | " +
//                    et_deskripsiBarang.getText().toString() + " | " +
//                    sp_kategoriBarang.getSelectedItem().toString());

                Bitmap bitmap               = ((BitmapDrawable) img_barang.getDrawable()).getBitmap();
                ByteArrayOutputStream baos  =  new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                final StorageReference ref  = storageReference.child(System.currentTimeMillis() + ".jpg");
                UploadTask uploadTask       = ref.putBytes(data);

                progressDialog.setMessage("Loading ...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                Task<Uri> urlTask   = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return ref.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadURL = task.getResult();

                        //    save ke database cuk
                        BarangRental barangRental   = new BarangRental(
                                et_namaBarang.getText().toString(),
                                et_hargaBarang.getText().toString(),
                                et_stokBarang.getText().toString(),
                                et_deskripsiBarang.getText().toString(),
                                sp_kategoriBarang.getSelectedItem().toString(),
                                downloadURL.toString()
                        );

                        String uploadID = databaseReference.push().getKey();
                        databaseReference.child(uploadID).setValue(barangRental)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressDialog.dismiss();

                                        final AlertDialog.Builder builder;
                                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                                            builder = new AlertDialog.Builder(RentalinBarangActivity.this,
                                                    android.R.style.Theme_Material_Light_Dialog_Alert);
                                        } else {
                                            builder = new AlertDialog.Builder(RentalinBarangActivity.this);
                                        }

                                        builder.setTitle("Info")
                                                .setMessage("barang berhasil Di Upload !")
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent i = new Intent(RentalinBarangActivity.this, MainActivity.class);
                                                        i.putExtra("pindahFragment", "fragmentSayaOverview");
                                                        startActivity(i);
                                                        finish();
                                                    }
                                                })
                                                .show();
                                    }
                                });

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        make_alertDialog("Peringatan", "Gagal mengirim data " + e.getMessage().toString());
                    }
                });

//                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        progressDialog.dismiss();
//                        make_alertDialog("Informasi", "Barang berhasil Di upload");
//
//
//                        //save ke database cuk
//                        BarangRental barangRental   = new BarangRental(
//                                et_namaBarang.getText().toString(),
//                                et_hargaBarang.getText().toString(),
//                                et_stokBarang.getText().toString(),
//                                et_deskripsiBarang.getText().toString(),
//                                sp_kategoriBarang.getSelectedItem().toString(),
//                                taskSnapshot.getUploadSessionUri().toString()
//                        );
//
//
//                        String uploadID = databaseReference.push().getKey();
//                        databaseReference.child(uploadID).setValue(barangRental);
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        progressDialog.dismiss();;
//                        make_alertDialog("Informasi", "Barang Gagal Di upload");
//                    }
//                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                        double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//                        progressDialog.setMessage("Proses ... " + (int)progress + "%");
//                        progressDialog.show();
//                    }
//                });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){

            case PICK_IMAGE_CAMERA :
                if(resultCode == RESULT_OK){
                    selectedImage = data.getData();
//                    img_barang.setImageURI(selectedImage);


//                    try{
//                        Bitmap bm   = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
//                        img_barang.setImageBitmap(bm);
//                    } catch (FileNotFoundException e) {
//                        make_toast("Error : " + e.getMessage().toString());
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        make_toast("Error : " + e.getMessage().toString());
//                        e.printStackTrace();
//                    }

                    Bitmap foto = (Bitmap) data.getExtras().get("data");
                    img_barang.setImageBitmap(foto);

//                    Picasso.with(this)
//                            .load(selectedImage)
//                            .into(img_barang);
                }
                break;

            case PICK_IMAGE_GALLERY:
                if(resultCode == RESULT_OK && data != null && data.getData() != null){
                    selectedImage = data.getData();
                    img_barang.setImageURI(selectedImage);

                    Picasso.with(this)
                            .load(selectedImage)
                            .into(img_barang);
                }
                break;
        }

    }

    private void make_alertDialog(String title, String message){
        final AlertDialog.Builder builder;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
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
