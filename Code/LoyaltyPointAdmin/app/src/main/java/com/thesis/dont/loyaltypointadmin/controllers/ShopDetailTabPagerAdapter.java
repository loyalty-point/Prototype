package com.thesis.dont.loyaltypointadmin.controllers;

/**
 * Created by tinntt on 3/11/2015.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ShopDetailTabPagerAdapter extends FragmentPagerAdapter {
    private final String[] TITLES = {"Shop Home", "Sales", "Awards", "Users", "Relatives"};

    public ShopDetailTabPagerAdapter(FragmentManager fm) {
        super(fm);
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
//            return ShopDetailFragment.newInstance(position);
        if(position == 0)
            return new ShopDetailFragment(position);
        else if(position == 1)
            return new ShopSalesFragment(position);
        else if(position == 2)
            return new ShopAwardsFragment(position);
        else if(position == 3)
            return new ShopUserFragment(position);
        else
            return new ShopRelativeFragment(position);
    }
}
