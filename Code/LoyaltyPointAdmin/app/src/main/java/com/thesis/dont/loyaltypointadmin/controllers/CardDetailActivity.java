package com.thesis.dont.loyaltypointadmin.controllers;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Card;
import com.thesis.dont.loyaltypointadmin.models.Global;
import com.thesis.dont.loyaltypointadmin.models.Shop;

import java.util.List;

public class CardDetailActivity extends ActionBarActivity {

    int tabIndex = 0;
    Card card = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        card = i.getParcelableExtra(Global.CARD_OBJECT);
        if(tabIndex == 0)
            tabIndex = i.getIntExtra(Global.TAB_INDEX, 0);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        setTitle(card.getName());
        pager.setAdapter(new CardDetailTabPagerAdapter(getSupportFragmentManager(), card.getId()));
        //Toast.makeText(this,shopId,Toast.LENGTH_LONG).show();

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setTabPaddingLeftRight(70);
        tabs.setBackgroundResource(R.color.PrimaryColor);
        tabs.setTextColorResource(R.color.TextIconsHide);
        tabs.setIndicatorColorResource(R.color.TextIcons);
        tabs.setIndicatorHeight(tabs.getIndicatorHeight()+5);
        getSupportActionBar().setElevation(0);

        pager.setCurrentItem(tabIndex);
        tabs.setViewPager(pager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                /*Intent upIntent = NavUtils.getParentActivityIntent(this);
                upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                NavUtils.navigateUpFromSameTask(this);*/
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
