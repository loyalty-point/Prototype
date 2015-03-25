package com.thesis.dont.loyaltypointadmin.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 11120_000 on 18/03/15.
 */
public class Award implements Parcelable {
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

    public Award(String id, String name, int point, int quantity, String description, String image, String shopID) {
        this.id = id;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeInt(point);
        dest.writeInt(quantity);
        dest.writeString(description);
        dest.writeString(image);
        dest.writeString(shopID);
    }

    public static final Parcelable.Creator<Award> CREATOR
            = new Parcelable.Creator<Award>() {
        public Award createFromParcel(Parcel in) {
            return new Award(in);
        }

        public Award[] newArray(int size) {
            return new Award[size];
        }
    };

    private Award(Parcel in) {
        id = in.readString();
        name = in.readString();
        point = in.readInt();
        quantity = in.readInt();
        description = in.readString();
        image = in.readString();
        shopID = in.readString();
    }
}
