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
    private float cardnameX, cardnameY;
    private float usernameX;
    private float usernameY;
    private float qrcodeX, qrcodeY;
    private int textColor;

    private int point;
    private int isAccepted;

    public Card(String cardName, String cardImage){
        name = cardName;
        image = cardImage;
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

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
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
        dest.writeFloat(cardnameX);
        dest.writeFloat(cardnameY);
        dest.writeFloat(usernameX);
        dest.writeFloat(usernameY);
        dest.writeFloat(qrcodeX);
        dest.writeFloat(qrcodeY);
        dest.writeInt(textColor);
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
        cardnameX = in.readFloat();
        cardnameY = in.readFloat();
        usernameX = in.readFloat();
        usernameY = in.readFloat();
        qrcodeX = in.readFloat();
        qrcodeY = in.readFloat();
        textColor = in.readInt();
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
