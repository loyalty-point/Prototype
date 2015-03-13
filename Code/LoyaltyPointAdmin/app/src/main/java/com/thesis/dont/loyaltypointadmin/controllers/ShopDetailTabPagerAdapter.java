package com.thesis.dont.loyaltypointadmin.controllers;

/**
 * Created by tinntt on 3/11/2015.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class ShopDetailTabPagerAdapter extends FragmentPagerAdapter {
    private final String[] TITLES = {"Shop Home", "Sales", "Awards", "Users", "Relatives"};
    private String shopId = null;

    public ShopDetailTabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public ShopDetailTabPagerAdapter(FragmentManager fm, String shopId) {
        super(fm);
        this.shopId = shopId;
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
            return new ShopSalesFragment(position, shopId);
        else if (position == 2)
            return new ShopAwardsFragment(position, shopId);
        else if (position == 3)
            return new ShopUserFragment(position, shopId);
        else
            return new ShopRelativeFragment(position, shopId);
    }
}
