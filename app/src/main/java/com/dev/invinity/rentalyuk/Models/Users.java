package com.dev.invinity.rentalyuk.Models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Users {

    String alamat;
    String jenisKelamin;
    String namaLengkap;
    String noTelp;
    String tglLahir;
    String imgURL;

    public Users() {
        //kosong ? astaghfirulloh
    }



    public Users(String namaLengkap, String noTelp, String tglLahir, String alamat, String jenisKelamin, String imgURL){
        this.namaLengkap    = namaLengkap;
        this.noTelp         = noTelp;
        this.tglLahir       = tglLahir;
        this.alamat         = alamat;
        this.jenisKelamin   = jenisKelamin;
        this.imgURL         = imgURL;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getNoTelp() {
        return noTelp;
    }

    public void setNoTelp(String noTelp) {
        this.noTelp = noTelp;
    }

    public String getTglLahir() {
        return tglLahir;
    }

    public void setTglLahir(String tglLahir) {
        this.tglLahir = tglLahir;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

}
