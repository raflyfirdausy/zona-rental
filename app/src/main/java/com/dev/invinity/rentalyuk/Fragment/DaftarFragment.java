package com.dev.invinity.rentalyuk.Fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.invinity.rentalyuk.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class DaftarFragment extends Fragment implements View.OnClickListener {

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private EditText et_email, et_password,et_password2, et_namaLengkap;
    private Button btn_daftar;
    private ProgressDialog progressDialog;
    private RadioGroup rb_jenisKelamin;

    //firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference databaseReference;
    private String userID;

    public DaftarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        View v          = inflater.inflate(R.layout.fragment_daftar, container, false);
        View v          = inflater.inflate(R.layout.fragment_daftar_v2, container, false);
        et_email        = (EditText) v.findViewById(R.id.et_email);
        et_password     = (EditText) v.findViewById(R.id.et_password);
        et_password2     = (EditText) v.findViewById(R.id.et_password2);
        et_namaLengkap  = (EditText) v.findViewById(R.id.et_namaLengkap);
        btn_daftar      = (Button) v.findViewById(R.id.btn_daftar);
        rb_jenisKelamin = (RadioGroup) v.findViewById(R.id.rb_jenisKelamin);

        //firebase
        firebaseAuth = FirebaseAuth.getInstance();

        //Fragment
        fragmentManager = getActivity().getSupportFragmentManager();

        //setonclick
        btn_daftar.setOnClickListener(this);
        v.findViewById(R.id.tv_klikdisini).setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_klikdisini:

                //Untuk inisialisasi fragment pertama kali
                fragmentManager.beginTransaction()
                        .replace(R.id.auth_content, new LoginFragment())
                        .commit();
                break;

            case R.id.btn_daftar:
                proses_daftar();
                break;

        }
    }

    private void make_toast(String  pesan){
        Toast.makeText(getActivity(), pesan, Toast.LENGTH_SHORT).show();
    }

    private void proses_daftar(){
        if((TextUtils.isEmpty(et_email.getText().toString()))
                || (TextUtils.isEmpty(et_password.getText().toString()))
                || ((TextUtils.isEmpty(et_namaLengkap.getText().toString())))
                || (rb_jenisKelamin.getCheckedRadioButtonId() == -1)){

            final AlertDialog.Builder builder;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Light_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(getActivity());
            }

            builder.setTitle("Peringatan!")
                    .setMessage("Masih Ada data yaang belum diisi !")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        } else if(!et_password.getText().toString().equals(et_password2.getText().toString())){
            final AlertDialog.Builder builder;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Light_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(getActivity());
            }

            builder.setTitle("Peringatan!")
                    .setMessage("Password Harus Sama !")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            et_password2.setError("Password Harus Sama");
                        }
                    })
                    .show();
        } else {

            //progressDialog
            progressDialog = ProgressDialog.show(getActivity(),
                    "Tunggu Beberapa Saat",
                    "Proses Pendaftaran ...",
                    true);

            firebaseAuth.createUserWithEmailAndPassword(et_email.getText().toString().trim(),
                    et_password.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();

                            if(task.isSuccessful()){
                                et_email.setText("");
                                et_password.setText("");
                                make_toast("Pendaftaran berhasil , Silahkan Login!");

                                //save into realtime cuk
                                String user_id                      = firebaseAuth.getCurrentUser().getUid();
                                DatabaseReference current_user_db   = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

                                String namaLengkap  = et_namaLengkap.getText().toString();
                                String jenisKelamin = (String) ((RadioButton) getView().findViewById(rb_jenisKelamin.getCheckedRadioButtonId())).getText();
                                String noTelp       = "-";
                                String tglLahir     = "-";
                                String alamat       = "-";
                                String imgURL       = "-";


                                Map newPost         = new HashMap();
                                newPost.put("namaLengkap", namaLengkap);
                                newPost.put("jenisKelamin", jenisKelamin);
                                newPost.put("noTelp", noTelp);
                                newPost.put("tglLahir", tglLahir);
                                newPost.put("alamat", alamat);
                                newPost.put("imgURL", imgURL);

                                current_user_db.setValue(newPost);



                                //pindah fragment
                                fragmentManager.beginTransaction()
                                        .replace(R.id.auth_content, new LoginFragment())
                                        .commit();
                            } else {
                                make_toast("Pendaftaran Gagal : " + task.getException().getMessage());
                            }


                        }
                    });

        }
    }
}
