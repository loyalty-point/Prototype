package models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 11120_000 on 02/04/15.
 */
public class Product implements Parcelable {
    private int quantity;
    private String barcode;
    private String name;

    public Product(int quantity, String barcode, String name) {
        this.quantity = quantity;
        this.barcode = barcode;
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(quantity);
        dest.writeString(barcode);
        dest.writeString(name);
    }

    public static final Parcelable.Creator<Product> CREATOR
            = new Parcelable.Creator<Product>() {
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    private Product(Parcel in) {
        quantity = in.readInt();
        barcode = in.readString();
        name = in.readString();
    }
}
