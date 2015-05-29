package com.thesis.dont.loyaltypointuser.controllers;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.thesis.dont.loyaltypointuser.R;
import com.thesis.dont.loyaltypointuser.models.Event;
import com.thesis.dont.loyaltypointuser.models.Global;
import com.thesis.dont.loyaltypointuser.models.Shop;

import java.util.ArrayList;

public class EventDetailActivity extends ActionBarActivity {

    TextView eventName, eventDate, eventDescription;
    ImageView eventImage;
    ListView shopList;

    Event event;
    ArrayList<Shop> listShops;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        Intent i = getIntent();
        event = (Event) i.getParcelableExtra(ShopEventsFragment.EVENT_OBJECT);
        listShops = i.getParcelableArrayListExtra(Global.SHOP_ARRAY_OBJECT);

        eventName = (TextView) findViewById(R.id.eventName);
        eventDate = (TextView) findViewById(R.id.eventDate);
        eventDescription = (TextView) findViewById(R.id.eventDescription);

        eventName.setText(event.getName());
        eventDate.setText(event.getTime_start() + " - " + event.getTime_end());
//        eventInfo.setText(event.get());
        eventDescription.setText(event.getDescription());
    }
}
