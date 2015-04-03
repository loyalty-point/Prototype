package com.thesis.dont.loyaltypointuser.controllers;

/**
 * Created by tinntt on 3/11/2015.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class ShopDetailTabPagerAdapter extends FragmentPagerAdapter {
    private final String[] TITLES = {"Shop Home", "Events", "Awards", "History", "Relatives"};
    private String shopId = null;
    private int userPoint = 0;

    public ShopDetailTabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public ShopDetailTabPagerAdapter(FragmentManager fm, String shopId, int userPoint) {
        super(fm);
        this.shopId = shopId;
        this.userPoint = userPoint;
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
        if (position == 0)
            return new ShopDetailFragment(position, shopId);
        else if (position == 1)
            return new ShopEventsFragment(position, shopId);
        else if (position == 2)
            return new ShopAwardsFragment(position, shopId, userPoint);
        else if (position == 3)
            return new ShopHistoryFragment(position, shopId);
        else
            return new ShopRelativeFragment(position, shopId);
    }
}
