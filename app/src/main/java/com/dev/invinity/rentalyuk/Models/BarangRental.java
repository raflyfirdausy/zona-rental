package com.dev.invinity.rentalyuk.Models;

public class BarangRental {

    public String namaBarang;
    public String hargaBarang;
    public String stokBarang;
    public String deskripsiBarang;
    public String kategoriBarang;
    public String imgURLBarang;

    public BarangRental(){
        //kosong ?? astaghfirulloh :v
    }

    public BarangRental(String namaBarang,
                        String hargaBarang,
                        String stokBarang,
                        String deskripsiBarang,
                        String kategoriBarang,
                        String imgURLBarang) {

        this.namaBarang = namaBarang;
        this.hargaBarang = hargaBarang;
        this.stokBarang = stokBarang;
        this.deskripsiBarang = deskripsiBarang;
        this.kategoriBarang = kategoriBarang;
        this.imgURLBarang = imgURLBarang;
    }

    public String getImgURLBarang() {
        return imgURLBarang;
    }

    public void setImgURLBarang(String imgURLBarang) {
        this.imgURLBarang = imgURLBarang;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public String getHargaBarang() {
        return hargaBarang;
    }

    public void setHargaBarang(String hargaBarang) {
        this.hargaBarang = hargaBarang;
    }

    public String getStokBarang() {
        return stokBarang;
    }

    public void setStokBarang(String stokBarang) {
        this.stokBarang = stokBarang;
    }

    public String getDeskripsiBarang() {
        return deskripsiBarang;
    }

    public void setDeskripsiBarang(String deskripsiBarang) {
        this.deskripsiBarang = deskripsiBarang;
    }

    public String getKategoriBarang() {
        return kategoriBarang;
    }

    public void setKategoriBarang(String kategoriBarang) {
        this.kategoriBarang = kategoriBarang;
    }
}
