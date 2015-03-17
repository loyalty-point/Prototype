package com.thesis.dont.loyaltypointadmin.models;

/**
 * Created by 11120_000 on 18/03/15.
 */
public class Award {
    String name;
    int point;
    int quantity;
    String image;

    public Award(String name, int point, int quantity, String image) {
        this.name = name;
        this.point = point;
        this.quantity = quantity;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
