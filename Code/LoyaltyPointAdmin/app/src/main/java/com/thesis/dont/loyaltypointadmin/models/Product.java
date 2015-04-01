package com.thesis.dont.loyaltypointadmin.models;

/**
 * Created by 11120_000 on 02/04/15.
 */
public class Product {
    int quantity;
    String barcode;
    String name;

    public Product(int quantity, String barcode, String name) {
        this.quantity = quantity;
        this.barcode = barcode;
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
