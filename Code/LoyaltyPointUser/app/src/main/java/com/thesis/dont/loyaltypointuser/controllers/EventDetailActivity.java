package com.thesis.dont.loyaltypointuser.controllers;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.thesis.dont.loyaltypointuser.R;
import com.thesis.dont.loyaltypointuser.models.Event;
import com.thesis.dont.loyaltypointuser.models.Global;
import com.thesis.dont.loyaltypointuser.models.Shop;

import java.util.ArrayList;

public class EventDetailActivity extends ActionBarActivity {

    Event event;
    ArrayList<Shop> listShops;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        Intent i = getIntent();
        event = (Event) i.getParcelableExtra(ShopEventsFragment.EVENT_OBJECT);
        listShops = i.getParcelableArrayListExtra(Global.SHOP_ARRAY_OBJECT);

    }
}
