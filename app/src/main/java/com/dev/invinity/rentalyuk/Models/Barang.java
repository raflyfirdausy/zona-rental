package com.dev.invinity.rentalyuk.Models;

public class Barang {
    String title;
    String image;
    String price;

    public Barang(String title, String image, String price) {
        this.title = title;
        this.image = image;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getPrice() {
        return price;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
