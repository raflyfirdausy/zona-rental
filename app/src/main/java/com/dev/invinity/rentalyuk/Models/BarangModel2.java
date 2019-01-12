package com.dev.invinity.rentalyuk.Models;

public class BarangModel2 {
    private String deskripsiBarang;
    private String hargaBarang;
    private String imgURLBarang;
    private String kategoriBarang;
    private String namaBarang;
    private String stokBarang;
    private String key;
    private String keyPemilikBarang;
    private String KeyTransaksi;
    private String status;

    public BarangModel2(){
        //kosongan
    }

    public BarangModel2(String status,String deskripsiBarang, String hargaBarang, String imgURLBarang, String kategoriBarang, String namaBarang, String stokBarang, String key, String keyPemilikBarang) {
        this.deskripsiBarang = deskripsiBarang;
        this.hargaBarang = hargaBarang;
        this.imgURLBarang = imgURLBarang;
        this.kategoriBarang = kategoriBarang;
        this.namaBarang = namaBarang;
        this.stokBarang = stokBarang;
        this.key = key;
        this.keyPemilikBarang = keyPemilikBarang;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeskripsiBarang() {
        return deskripsiBarang;
    }

    public void setDeskripsiBarang(String deskripsiBarang) {
        this.deskripsiBarang = deskripsiBarang;
    }

    public String getKeyTransaksi() {
        return KeyTransaksi;
    }

    public void setKeyTransaksi(String keyTransaksi) {
        KeyTransaksi = keyTransaksi;
    }

    public String getHargaBarang() {
        return hargaBarang;
    }

    public void setHargaBarang(String hargaBarang) {
        this.hargaBarang = hargaBarang;
    }

    public String getImgURLBarang() {
        return imgURLBarang;
    }

    public void setImgURLBarang(String imgURLBarang) {
        this.imgURLBarang = imgURLBarang;
    }

    public String getKategoriBarang() {
        return kategoriBarang;
    }

    public void setKategoriBarang(String kategoriBarang) {
        this.kategoriBarang = kategoriBarang;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public String getStokBarang() {
        return stokBarang;
    }

    public void setStokBarang(String stokBarang) {
        this.stokBarang = stokBarang;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKeyPemilikBarang() {
        return keyPemilikBarang;
    }

    public void setKeyPemilikBarang(String keyPemilikBarang) {
        this.keyPemilikBarang = keyPemilikBarang;
    }
}
