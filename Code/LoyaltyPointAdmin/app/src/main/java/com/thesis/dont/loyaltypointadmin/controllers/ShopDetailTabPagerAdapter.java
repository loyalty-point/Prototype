package com.thesis.dont.loyaltypointadmin.controllers;

/**
 * Created by tinntt on 3/11/2015.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class ShopDetailTabPagerAdapter extends FragmentPagerAdapter {
    private final String[] TITLES = {"Home", "Events", "Awards", "Users", "Registers", "History", "Relatives"};
    private String shopId = null;
    private String cardId = null;

    public ShopDetailTabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public ShopDetailTabPagerAdapter(FragmentManager fm, String shopId, String cardId) {
        super(fm);
        this.shopId = shopId;
        this.cardId = cardId;
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
            return new ShopDetailFragment(position);
        else if (position == 1)
            return new ShopEventsFragment(position);
        else if (position == 2)
            return new ShopAwardsFragment(position);
        else if (position == 3)
            return new ShopUserFragment(position);
        else if (position == 4)
            return new ShopRegistersFragment(position);
        else if(position == 5)
            return new ShopHistoryFragment(position);
        else
            return new ShopRelativeFragment(position, shopId);

    }
}
