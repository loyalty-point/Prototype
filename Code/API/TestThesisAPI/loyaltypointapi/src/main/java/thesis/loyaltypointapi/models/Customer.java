package thesis.loyaltypointapi.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Customer implements Parcelable {
    public static final Creator<Customer> CREATOR
            = new Creator<Customer>() {
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };
    String username;
    String fullname;
    String phone;
    String email;
    String address;
    String avatar;
    String barcode;

    public Customer(String username, String fullname, String phone, String email, String address, String barcode, String avatar) {
        this.username = username;
        this.fullname = fullname;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.barcode = barcode;
        this.avatar = avatar;
    }

    private Customer(Parcel in) {
        username = in.readString();
        fullname = in.readString();
        phone = in.readString();
        email = in.readString();
        address = in.readString();
        avatar = in.readString();
        barcode = in.readString();
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(fullname);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(address);
        dest.writeString(avatar);
        dest.writeString(barcode);
    }
}

