package com.thesis.dont.loyaltypointadmin.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tinntt on 4/17/2015.
 */
public class Customer implements Parcelable {
    String username;
    String fullname;
    String phone;
    String email;
    String address;
    String identity_number;
    String barcode;
    String avatar;
    private int point;

    public Customer(String username, String fullname, String phone, String email, String address, String identity_number, String avatar, int point){
        this.username = username;
        this.fullname = fullname;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.identity_number = identity_number;
        this.avatar = avatar;
        this.point = point;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getAddress(){
        return address;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public String getIdentityNumber(){
        return identity_number;
    }

    public void setIdentityNumber(String identity_number){
        this.identity_number = identity_number;
    }

    public String getAvatar(){
        return avatar;
    }

    public void setAvatar(String avatar){
        this.avatar = avatar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(fullname);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(address);
        dest.writeString(identity_number);
        dest.writeString(barcode);
        dest.writeString(avatar);
        dest.writeInt(point);
    }

    public static final Parcelable.Creator<Customer> CREATOR
            = new Parcelable.Creator<Customer>() {
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };

    private Customer(Parcel in) {
        username = in.readString();
        fullname = in.readString();
        phone = in.readString();
        email = in.readString();
        address = in.readString();
        identity_number = in.readString();
        barcode = in.readString();
        avatar = in.readString();
        point = in.readInt();
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
