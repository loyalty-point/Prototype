package com.thesis.dont.loyaltypointuser.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tinntt on 3/5/2015.
 */
public class Shop implements Parcelable{
    String id;
    String name;
    String address;
    String phone_number;
    String category;
    String image;
    float exchange_ratio;
    String cardImg;
    int point;
    int isAccepted;

    public Shop(String id, String name, String address, String phone_number, String category, float exchange_ratio, String image, String cardImg, int point){
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone_number = phone_number;
        this.category = category;
        this.exchange_ratio = exchange_ratio;
        this.image = image;
        this.cardImg = cardImg;
        this.point = point;
    }

    public int isAccepted() {
        return isAccepted;
    }

    public void setAccepted(int isAccepted) {
        this.isAccepted = isAccepted;
    }

    public String getCardImg() {
        return cardImg;
    }

    public void setCardImg(String cardImg) {
        this.cardImg = cardImg;
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

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(phone_number);
        dest.writeString(category);
        dest.writeString(image);
        dest.writeFloat(exchange_ratio);
        dest.writeInt(point);
        dest.writeInt(isAccepted);
    }

    public static final Creator<Shop> CREATOR
            = new Creator<Shop>() {
        public Shop createFromParcel(Parcel in) {
            return new Shop(in);
        }

        public Shop[] newArray(int size) {
            return new Shop[size];
        }
    };

    private Shop(Parcel in) {
        id = in.readString();
        name = in.readString();
        address = in.readString();
        phone_number = in.readString();
        category = in.readString();
        image = in.readString();
        exchange_ratio = in.readFloat();
        point = in.readInt();
        isAccepted = in.readInt();
    }
}
