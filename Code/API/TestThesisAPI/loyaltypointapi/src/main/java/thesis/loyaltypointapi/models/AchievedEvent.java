package thesis.loyaltypointapi.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 11120_000 on 03/04/15.
 */
public class AchievedEvent implements Parcelable {
    public static final Parcelable.Creator<AchievedEvent> CREATOR
            = new Parcelable.Creator<AchievedEvent>() {
        public AchievedEvent createFromParcel(Parcel in) {
            return new AchievedEvent(in);
        }

        public AchievedEvent[] newArray(int size) {
            return new AchievedEvent[size];
        }
    };
    private int quantity;
    private Event event;

    public AchievedEvent(int quantity, Event event) {
        this.quantity = quantity;
        this.event = event;
    }

    private AchievedEvent(Parcel in) {
        quantity = in.readInt();
        event = in.readParcelable(Event.class.getClassLoader());
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(quantity);
        dest.writeParcelable(event, flags);
    }
}
