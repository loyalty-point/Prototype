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
    private float cardnameX, cardnameY;
    private float usernameX, usernameY;
    private float qrcodeX, qrcodeY;
    private int textColor;

    public Card(String cardName, String cardImage, float cardnameX, float cardnameY, float usernameX, float usernameY, float qrcodeX, float qrcodeY, int textColor){
        name = cardName;
        image = cardImage;
        this.cardnameX = cardnameX;
        this.cardnameY = cardnameY;
        this.usernameX = usernameX;
        this.usernameY = usernameY;
        this.qrcodeX = qrcodeX;
        this.qrcodeY = qrcodeY;
        this.textColor = textColor;
    }

    public float getCardnameX() {
        return cardnameX;
    }

    public void setCardnameX(float cardnameX) {
        this.cardnameX = cardnameX;
    }

    public float getCardnameY() {
        return cardnameY;
    }

    public void setCardnameY(float cardnameY) {
        this.cardnameY = cardnameY;
    }

    public float getUsernameX() {
        return usernameX;
    }

    public void setUsernameX(float usernameX) {
        this.usernameX = usernameX;
    }

    public float getUsernameY() {
        return usernameY;
    }

    public void setUsernameY(float usernameY) {
        this.usernameY = usernameY;
    }

    public float getQrcodeX() {
        return qrcodeX;
    }

    public void setQrcodeX(float qrcodeX) {
        this.qrcodeX = qrcodeX;
    }

    public float getQrcodeY() {
        return qrcodeY;
    }

    public void setQrcodeY(float qrcodeY) {
        this.qrcodeY = qrcodeY;
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

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
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
        dest.writeFloat(cardnameX);
        dest.writeFloat(cardnameY);
        dest.writeFloat(usernameX);
        dest.writeFloat(usernameY);
        dest.writeFloat(qrcodeX);
        dest.writeFloat(qrcodeY);
        dest.writeInt(textColor);
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
        cardnameX = in.readFloat();
        cardnameY = in.readFloat();
        usernameX = in.readFloat();
        usernameY = in.readFloat();
        qrcodeX = in.readFloat();
        qrcodeY = in.readFloat();
        textColor = in.readInt();
    }

}
