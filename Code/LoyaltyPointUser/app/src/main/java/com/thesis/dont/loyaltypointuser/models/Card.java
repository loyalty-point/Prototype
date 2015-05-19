package com.thesis.dont.loyaltypointuser.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tinntt on 5/16/2015.
 */
public class Card implements Parcelable {

    private String id;
    private String name;
    private String image;
    private int point;
    private int isAccepted;

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
        dest.writeInt(point);
        dest.writeInt(isAccepted);
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
        point = in.readInt();
        isAccepted = in.readInt();
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(int isAccepted) {
        this.isAccepted = isAccepted;
    }
}
