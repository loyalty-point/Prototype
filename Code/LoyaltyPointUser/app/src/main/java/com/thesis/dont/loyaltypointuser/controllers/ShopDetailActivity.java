package com.thesis.dont.loyaltypointuser.controllers;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.thesis.dont.loyaltypointuser.R;
import com.thesis.dont.loyaltypointuser.models.Global;

public class ShopDetailActivity extends ActionBarActivity {

    String shopName;
    public String shopID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        shopID = getIntent().getStringExtra("SHOP_ID");
        shopName = getIntent().getStringExtra(Global.SHOP_NAME);
        setTitle(shopName);
        pager.setAdapter(new ShopDetailTabPagerAdapter(getSupportFragmentManager(), shopID));
        //Toast.makeText(this,shopId,Toast.LENGTH_LONG).show();

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setTabPaddingLeftRight(70);
        tabs.setBackgroundResource(R.color.PrimaryColor);
        tabs.setTextColorResource(R.color.TextIconsHide);
        tabs.setIndicatorColorResource(R.color.TextIcons);
        tabs.setIndicatorHeight(tabs.getIndicatorHeight()+5);
        getSupportActionBar().setElevation(0);

        tabs.setViewPager(pager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}