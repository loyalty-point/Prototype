package com.thesis.dont.loyaltypointadmin.models;

/**
 * Created by 11120_000 on 04/04/15.
 */
public class History {
    String time;
    String billImage;
    String billCode;
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

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
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


}
