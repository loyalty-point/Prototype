package com.thesis.dont.loyaltypointadmin.controllers;

/**
 * Created by tinntt on 3/11/2015.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class UserDetailTabPagerAdapter extends FragmentPagerAdapter {
    private final String[] TITLES = {"Home", "Awards", "Tickets"};
    private String userId = null;
    private String shopId = null;

    public UserDetailTabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public UserDetailTabPagerAdapter(FragmentManager fm, String userId, String shopId) {
        super(fm);
        this.userId = userId;
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
            return new UserDetailFragment();
        else if (position == 1)
            return new UserAwardFragment(position,this.userId, this.shopId);
        else
            return new UserTicketsFragment(position, this.userId, this.shopId);

    }
}
