package com.thesis.dont.loyaltypointadmin.models;

/**
 * Created by tinntt on 3/5/2015.
 */
public class Shop {
    String id;
    String name;
    String address;
    String phone_number;
    String category;
    String image;
    float exchange_ratio;

    public Shop(String name, String address, String phone_number, String category, float exchange_ratio, String image){
        this.name = name;
        this.address = address;
        this.phone_number = phone_number;
        this.category = category;
        this.exchange_ratio = exchange_ratio;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getExchange_ratio() {
        return exchange_ratio;
    }

    public void setExchange_ratio(float exchange_ratio) {
        this.exchange_ratio = exchange_ratio;
    }
}
