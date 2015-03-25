package com.thesis.dont.loyaltypointadmin.models;

/**
 * Created by 11120_000 on 18/03/15.
 */
public class Award {
    String id;
    String name;
    int point;
    int quantity;
    String description;
    String image;
    String shopID;

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShopID() {
        return shopID;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

    public Award(String name, int point, int quantity, String description, String image, String shopID) {
        this.name = name;
        this.point = point;
        this.quantity = quantity;
        this.description = description;
        this.image = image;
        this.shopID = shopID;
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
