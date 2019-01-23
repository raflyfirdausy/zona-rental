package com.dev.invinity.rentalyuk.Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.invinity.rentalyuk.Models.Users;
import com.dev.invinity.rentalyuk.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class SayaEditFragment extends Fragment implements View.OnClickListener {

////////////////////////    Start Deklarasi Variable    //////////////////////////

    private EditText etNama, etEmail, etTelp, etTanggal_lahir, etAlamat;
    private TextView et_editFoto;
    private int tanggal, bulan, tahun;
    private ImageView img_fotoProfil;
    private Button btn_simpan;
    private RadioButton rb_lakiLaki, rb_perempuan;
    private RadioGroup rb_jenisKelamin;

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

    //progress dialog
    private ProgressDialog progressDialog;

    //download URL
    private Uri downloadURL;

    //selected image
    private Uri selectedImage;

    //get drawable
    private Drawable gambar_lama;
    private Drawable gambar_baru;

////////////////////////     End Deklarasi Variable     //////////////////////////


    public SayaEditFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_saya_edit, container, false);

        etNama          = (EditText)    v.findViewById(R.id.etNama);
        etEmail         = (EditText)    v.findViewById(R.id.etEmail);
        etTelp          = (EditText)    v.findViewById(R.id.etTelp);
        etTanggal_lahir = (EditText)    v.findViewById(R.id.etTanggal_lahir);
        et_editFoto     = (TextView)    v.findViewById(R.id.txt_editFoto);
        etAlamat        = (EditText)    v.findViewById(R.id.etAlamat) ;
        img_fotoProfil  = (ImageView)   v.findViewById(R.id.img_fotoProfil);
        btn_simpan      = (Button)      v.findViewById(R.id.btn_simpan);
        rb_lakiLaki     = (RadioButton) v.findViewById(R.id.rb_lakiLaki);
        rb_perempuan    = (RadioButton) v.findViewById(R.id.rb_perempuan);
        rb_jenisKelamin = (RadioGroup)  v.findViewById(R.id.rb_jenisKelamin);

        //firebase
        firebaseAuth        = FirebaseAuth.getInstance();
        user                = firebaseAuth.getCurrentUser();
        userID              = user.getUid();

        databaseReference   = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users")
                .child(userID);

        storageReference    = FirebaseStorage.getInstance()
                .getReference()
                .child("Users")
                .child(userID)
                .child("FotoProfil");

        //progressdialog
        progressDialog      = new ProgressDialog(getActivity());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        et_editFoto.setOnClickListener(this);
        etTanggal_lahir.setOnClickListener(this);
        btn_simpan.setOnClickListener(this);
        v.setVerticalScrollBarEnabled(false);

        return v;
    }




    @Override
    public void onClick(View v){

        switch (v.getId()){

            case R.id.etTanggal_lahir:
                getTanggal();
                break;

            case R.id.txt_editFoto:
                cameraOrgallery();
                break;

            case R.id.btn_simpan:
                updateBiodata();
//                Toast.makeText(getActivity(), "Coming Soon!", Toast.LENGTH_SHORT).show();
//                user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(getActivity(), "Email terkirim!", Toast.LENGTH_SHORT).show();
//                    }
//                });
                break;
        }

    }

    private void make_toast(String message){
        Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
    }

    private void getTanggal(){
        final Calendar c    = Calendar.getInstance();
        tanggal             = c.get(Calendar.DAY_OF_MONTH);
        bulan               = c.get(Calendar.MONTH);
        tahun               = c.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        etTanggal_lahir.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                    }
                },tahun,bulan,tanggal);
        datePickerDialog.show();
    }

    private void cameraOrgallery(){
        final CharSequence[] pilihan = { "Kamera", "Gallery","Batal" };

        final AlertDialog.Builder builder;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getActivity());
        }

        builder.setTitle("Pilih Gambar")
                .setItems(pilihan, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if(pilihan[item].equals("Kamera")){

                            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case PICK_IMAGE_CAMERA :
                if(resultCode == RESULT_OK){
                    selectedImage = data.getData();
                    Bitmap foto = (Bitmap) data.getExtras().get("data");
                    img_fotoProfil.setImageBitmap(foto);
                }
                break;

            case PICK_IMAGE_GALLERY:
                if(resultCode == RESULT_OK && data != null && data.getData() != null){
                    selectedImage = data.getData();
                    img_fotoProfil.setImageURI(selectedImage);

                    Picasso.with(getActivity())
                            .load(selectedImage)
                            .into(img_fotoProfil);
                }
                break;
        }

    }

    public void getBiodata(String nama,
                           String email,
                           String noTelp,
                           String tglLahir,
                           String alamat,
                           String jenisKelamin,
                           String imgURL){

        etNama.setText(nama);
        etEmail.setText(email);
        etTelp.setText(noTelp);
        etTanggal_lahir.setText(tglLahir);
        etAlamat.setText(alamat);

        if(jenisKelamin.equalsIgnoreCase("Perempuan")){
            rb_perempuan.setChecked(true);
            rb_lakiLaki.setChecked(false);
        } else {
            rb_perempuan.setChecked(false);
            rb_lakiLaki.setChecked(true);
        }

        Picasso.with(getActivity())
                .load(imgURL)
                .placeholder(R.drawable.logo)
                .into(img_fotoProfil);
    }

    private void getData(DataSnapshot dataSnapshot){

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

    private boolean updateBiodata(){

            //update foto
            Bitmap bitmap               = ((BitmapDrawable) img_fotoProfil.getDrawable()).getBitmap();
            ByteArrayOutputStream baos  =  new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] dataFoto = baos.toByteArray();

            final StorageReference ref  = storageReference.child(userID + ".jpg");
            UploadTask uploadTask       = ref.putBytes(dataFoto);

            progressDialog.setMessage("Proses update ...");
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

                        //save Ke database
                        Users data   = new Users(
                                etNama.getText().toString(),
                                etTelp.getText().toString(),
                                etTanggal_lahir.getText().toString(),
                                etAlamat.getText().toString(),
                                (String) ((RadioButton) getView().
                                        findViewById(rb_jenisKelamin.getCheckedRadioButtonId()))
                                        .getText()
                                        .toString(),
                                downloadURL.toString()
                        );


                        databaseReference.setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();

                                final AlertDialog.Builder builder;
                                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                                    builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Light_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(getActivity());
                                }

                                builder.setTitle("Informasi")
                                        .setMessage("Data Berhasil Di Perbarui !")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                getActivity().getSupportFragmentManager()
                                                        .beginTransaction()
                                                        .replace(R.id.fragment_container, new SayaOverviewFragment())
                                                        .commit();
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
                    make_alertDialog("Peringatan!", "Gagal Upload Foto Profil");
                }
            });


//        //update email
//        user.updateEmail(etEmail.getText().toString().trim());
//
//        //update info user
//        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                .setDisplayName(etNama.getText().toString())
//                .setPhotoUri(downloadURL)
//                .build();
//
//        user.updateProfile(profileUpdates).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                make_alertDialog("Peringatan", "Gagal update info user!");
//            }
//        });
//
//
//        //update database
//        Users data   = new Users(   etNama.getText().toString(),
//                etTelp.getText().toString(),
//                etTanggal_lahir.getText().toString(),
//                etAlamat.getText().toString(),
//                (String) ((RadioButton) getView().
//                        findViewById(rb_jenisKelamin.getCheckedRadioButtonId()))
//                        .getText()
//                        .toString(),
//                downloadURL.toString()
//        );
//
//        DatabaseReference dbUpdate = FirebaseDatabase.getInstance()
//                                        .getReference()
//                                        .child("Users")
//                                        .child(userID);
//
//        //update firebase
//        dbUpdate.setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                progressDialog.dismiss();
//                make_alertDialog("Informasi", "Data Berhasil Di Update!");
//            }
//        });



        return true;

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
