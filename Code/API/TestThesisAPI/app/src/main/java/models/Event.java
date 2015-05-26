package models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tinntt on 3/24/2015.
 */
public class Event implements Parcelable {
    private String shopName;
    private String id;
    private int type;
    private String name;
    private String time_start;
    private String time_end;
    private String description;
    private String barcode;
    private String goods_name;
    private float ratio;
    private int number;
    private int point;
    private String image;

    public Event(String id, int type, String name, String time_start, String time_end, String description,
                 String barcode, String goods_name, float ratio, int number, int point, String image) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.time_start = time_start;
        this.time_end = time_end;
        this.description = description;
        this.barcode = barcode;
        this.goods_name = goods_name;
        this.ratio = ratio;
        this.number = number;
        this.point = point;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(shopName);
        dest.writeString(id);
        dest.writeInt(type);
        dest.writeString(name);
        dest.writeString(time_start);
        dest.writeString(time_end);
        dest.writeString(description);
        dest.writeString(barcode);
        dest.writeString(goods_name);
        dest.writeFloat(ratio);
        dest.writeInt(number);
        dest.writeInt(point);
        dest.writeString(image);
    }

    public static final Parcelable.Creator<Event> CREATOR
            = new Parcelable.Creator<Event>() {
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    private Event(Parcel in) {
        shopName = in.readString();
        id = in.readString();
        type = in.readInt();
        name = in.readString();
        time_start = in.readString();
        time_end = in.readString();
        description = in.readString();
        barcode = in.readString();
        goods_name = in.readString();
        ratio = in.readFloat();
        number = in.readInt();
        point = in.readInt();
        image = in.readString();
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}
