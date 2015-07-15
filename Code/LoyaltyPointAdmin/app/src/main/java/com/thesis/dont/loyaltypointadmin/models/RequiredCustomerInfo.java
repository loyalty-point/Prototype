package com.thesis.dont.loyaltypointadmin.models;

/**
 * Created by tinntt on 6/7/2015.
 */
public class RequiredCustomerInfo {
    private boolean customerPhone;
    private boolean customerEmail;
    private boolean customerFullname;
    private boolean customerAddress;
    private boolean customerIdentityNumber;

    public RequiredCustomerInfo(boolean customerPhone, boolean customerEmail, boolean customerFullname, boolean customerAddress, boolean customerIdentityNumber){
        this.customerPhone = customerPhone;
        this.customerEmail = customerEmail;
        this.customerFullname = customerFullname;
        this.customerAddress = customerAddress;
        this.customerIdentityNumber = customerIdentityNumber;
    }

    public boolean isCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(boolean customerPhone) {
        this.customerPhone = customerPhone;
    }

    public boolean isCustomerFullname() {
        return customerFullname;
    }

    public void setCustomerFullname(boolean customerFullname) {
        this.customerFullname = customerFullname;
    }

    public boolean isCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(boolean customerEmail) {
        this.customerEmail = customerEmail;
    }

    public boolean isCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(boolean customerAddress) {
        this.customerAddress = customerAddress;
    }

    public boolean isCustomerIdentityNumber() {
        return customerIdentityNumber;
    }

    public void setCustomerIdentityNumber(boolean customerIdentityNumber) {
        this.customerIdentityNumber = customerIdentityNumber;
    }

    public void setStateAtPosition(int position, boolean state){
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

    public boolean getStateAtPosition(int position){
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
