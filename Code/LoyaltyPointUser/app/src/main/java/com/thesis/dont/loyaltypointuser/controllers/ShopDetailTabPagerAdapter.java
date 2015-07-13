package com.thesis.dont.loyaltypointuser.controllers;

/**
 * Created by tinntt on 3/11/2015.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class ShopDetailTabPagerAdapter extends FragmentPagerAdapter {
    private final String[] TITLES = { "Events", "Awards", "History"};
    private String shopId = null;
    private String shopName = null;
    private String shopAddress = null;
    private int userPoint = 0;

    public ShopDetailTabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public ShopDetailTabPagerAdapter(FragmentManager fm, String shopId, int userPoint, String shopName, String shopAddress) {
        super(fm);
        this.shopId = shopId;
        this.userPoint = userPoint;
        this.shopName = shopName;
        this.shopAddress = shopAddress;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public Fragment getItem(int position) {
        Log.e("position", String.valueOf(position));
//        if (position == 0)
//            return new ShopDetailFragment(position);
        if (position == 0)
            return new ShopEventsFragment(position);
        else if (position == 1)
            return new ShopAwardsFragment(position, userPoint);
        else
            return new ShopHistoryFragment(position, shopName, shopAddress);
    }
}
