package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Global;
import com.thesis.dont.loyaltypointadmin.models.Shop;
import com.thesis.dont.loyaltypointadmin.models.ShopModel;
import com.thesis.dont.loyaltypointadmin.models.User;

import java.util.List;

public class UserDetailActivity extends ActionBarActivity {

    String userId;
    String userFullName;
    String cardId, shopId;
    int userPoint;

    Shop mShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        userId = i.getStringExtra(Global.USER_NAME);
        userFullName = i.getStringExtra(Global.USER_FULLNAME);
        cardId = i.getStringExtra(Global.CARD_ID);
        shopId = i.getStringExtra(Global.SHOP_ID);
        mShop = i.getParcelableExtra(Global.SHOP_OBJECT);
        userPoint = i.getIntExtra(Global.USER_POINT, 0);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        setTitle(userFullName);
        pager.setAdapter(new UserDetailTabPagerAdapter(getSupportFragmentManager(), userId, shopId, cardId));
        //Toast.makeText(this,shopId,Toast.LENGTH_LONG).show();

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setTabPaddingLeftRight(70);
        tabs.setBackgroundResource(R.color.PrimaryColor);
        tabs.setTextColorResource(R.color.TextIconsHide);
        tabs.setIndicatorColorResource(R.color.TextIcons);
        tabs.setIndicatorHeight(tabs.getIndicatorHeight() + 5);
        getSupportActionBar().setElevation(0);

        tabs.setViewPager(pager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                /*Intent upIntent = NavUtils.getParentActivityIntent(this);
                upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                upIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                upIntent.putExtra(Global.SHOP_OBJECT, mShop);
                NavUtils.navigateUpFromSameTask(this);*/
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //call back when scan bar code successfully
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case Global.SCAN_BARCODE: {
//                if(resultCode == RESULT_OK){
//                    /*Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.user_fragment);
//                    fragment.onActivityResult(requestCode, resultCode, data);*/
//                    List<Fragment> fragments = getSupportFragmentManager().getFragments();
//                    for(int i=0; i<fragments.size(); i++) {
//                        fragments.get(i).onActivityResult(requestCode, resultCode, data);
//                    }
//                }
//                break;
//            }
//        }
    }
}
