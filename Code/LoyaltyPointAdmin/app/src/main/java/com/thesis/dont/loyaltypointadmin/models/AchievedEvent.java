package com.thesis.dont.loyaltypointadmin.models;

/**
 * Created by 11120_000 on 03/04/15.
 */
public class AchievedEvent {
    private int quantity;
    private Event event;

    public AchievedEvent(int quantity, Event event) {
        this.quantity = quantity;
        this.event = event;
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
}
