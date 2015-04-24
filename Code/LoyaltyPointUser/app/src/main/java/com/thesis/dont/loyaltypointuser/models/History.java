package com.thesis.dont.loyaltypointuser.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tinntt on 4/24/2015.
 */
public class History implements Parcelable {
    String time;
    String billImage;
    String id;
    private String type;
    String username;
    String fullname;
    String phone;
    int totalPoint;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBillImage() {
        return billImage;
    }

    public void setBillImage(String billImage) {
        this.billImage = billImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(int totalPoint) {
        this.totalPoint = totalPoint;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(time);
        dest.writeString(billImage);
        dest.writeString(id);
        dest.writeString(type);
        dest.writeString(username);
        dest.writeString(fullname);
        dest.writeString(phone);
        dest.writeInt(totalPoint);
    }

    public static final Parcelable.Creator<History> CREATOR
            = new Parcelable.Creator<History>() {
        public History createFromParcel(Parcel in) {
            return new History(in);
        }

        public History[] newArray(int size) {
            return new History[size];
        }
    };

    private History(Parcel in) {
        time = in.readString();
        billImage = in.readString();
        id = in.readString();
        type = in.readString();
        username = in.readString();
        fullname = in.readString();
        phone = in.readString();
        totalPoint = in.readInt();
    }
}
