package com.thesis.dont.loyaltypointadmin.controllers;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Global;
import com.thesis.dont.loyaltypointadmin.models.Shop;

import java.util.List;

public class ShopDetailActivity extends ActionBarActivity {

    Shop mShop;
    int tabIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        if(mShop == null)
            mShop = i.getParcelableExtra(Global.SHOP_OBJECT);
        
        if(tabIndex == 0)
            tabIndex = i.getIntExtra(Global.TAB_INDEX, 0);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        setTitle(mShop.getName());
        pager.setAdapter(new ShopDetailTabPagerAdapter(getSupportFragmentManager(), mShop.getId()));
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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mShop = intent.getParcelableExtra(Global.SHOP_OBJECT);
        tabIndex = intent.getIntExtra(Global.TAB_INDEX, 0);
    }

    public Shop getCurrentShop() {
        return mShop;
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

    //call back when scan bar code successfully
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Global.SCAN_BARCODE: {
                if(resultCode == RESULT_OK){
                    /*Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.user_fragment);
                    fragment.onActivityResult(requestCode, resultCode, data);*/
                    List<Fragment> fragments = getSupportFragmentManager().getFragments();
                    for(int i=0; i<fragments.size(); i++) {
                        fragments.get(i).onActivityResult(requestCode, resultCode, data);
                    }
                }
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
