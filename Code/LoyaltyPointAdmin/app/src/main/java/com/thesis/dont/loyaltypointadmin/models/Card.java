package com.thesis.dont.loyaltypointadmin.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tinntt on 5/14/2015.
 */
public class Card implements Parcelable{

    private String id;
    private String name;
    private String image;

    public Card(String cardName, String cardImage){
        name = cardName;
        image = cardImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(image);
    }

    public static final Parcelable.Creator<Card> CREATOR
            = new Parcelable.Creator<Card>() {
        public Card createFromParcel(Parcel in) {
            return new Card(in);
        }

        public Card[] newArray(int size) {
            return new Card[size];
        }
    };

    private Card(Parcel in) {
        id = in.readString();
        name = in.readString();
        image = in.readString();
    }

}
