package com.thesis.dont.loyaltypointuser.models;

/**
 * Created by tinntt on 6/7/2015.
 */
public class RequiredCustomerInfo {
    private int customerPhone;
    private int customerEmail;
    private int customerFullname;
    private int customerAddress;
    private int customerIdentityNumber;

    public RequiredCustomerInfo(int customerPhone, int customerEmail, int customerFullname, int customerAddress, int customerIdentityNumber){
        this.customerPhone = customerPhone;
        this.customerEmail = customerEmail;
        this.customerFullname = customerFullname;
        this.customerAddress = customerAddress;
        this.customerIdentityNumber = customerIdentityNumber;
    }

    public int isCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(int customerPhone) {
        this.customerPhone = customerPhone;
    }

    public int isCustomerFullname() {
        return customerFullname;
    }

    public void setCustomerFullname(int customerFullname) {
        this.customerFullname = customerFullname;
    }

    public int isCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(int customerEmail) {
        this.customerEmail = customerEmail;
    }

    public int isCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(int customerAddress) {
        this.customerAddress = customerAddress;
    }

    public int isCustomerIdentityNumber() {
        return customerIdentityNumber;
    }

    public void setCustomerIdentityNumber(int customerIdentityNumber) {
        this.customerIdentityNumber = customerIdentityNumber;
    }

    public void setStateAtPosition(int position, int state){
        switch (position){
            case 0:
                this.customerPhone = state;
                break;
            case 1:
                this.customerEmail = state;
                break;
            case 2:
                this.customerFullname = state;
                break;
            case 3:
                this.customerAddress = state;
                break;
            case 4:
                this.customerIdentityNumber = state;
                break;
        }
    }

    public int getStateAtPosition(int position){
        switch (position){
            case 0:
                return this.customerPhone;
            case 1:
                return this.customerEmail;
            case 2:
                return this.customerFullname;
            case 3:
                return this.customerAddress;
            default:
                return this.customerIdentityNumber;
        }
    }
}
