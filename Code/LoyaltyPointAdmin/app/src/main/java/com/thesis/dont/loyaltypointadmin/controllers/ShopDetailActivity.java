package com.thesis.dont.loyaltypointadmin.controllers;

import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.astuetz.PagerSlidingTabStrip;
import com.thesis.dont.loyaltypointadmin.R;

public class ShopDetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new ShopDetailTabPagerAdapter(getSupportFragmentManager()));

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setTabPaddingLeftRight(70);
        tabs.setViewPager(pager);
    }

}
