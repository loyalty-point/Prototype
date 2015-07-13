package com.thesis.dont.loyaltypointadmin.controllers;

/**
 * Created by tinntt on 3/11/2015.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class UserDetailTabPagerAdapter extends FragmentPagerAdapter {
    private final String[] TITLES = { "Awards", "Tickets"};
    private String userId = null;
    private String shopId = null;
    private String cardId = null;

    public UserDetailTabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public UserDetailTabPagerAdapter(FragmentManager fm, String userId, String shopId, String cardId) {
        super(fm);
        this.userId = userId;
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
//        if (position == 0)
//            return new UserDetailFragment();
        if (position == 0)
            return new UserAwardFragment(position,this.userId, this.shopId, this.cardId);
        else
            return new UserTicketsFragment(position, this.userId, this.shopId, this.cardId);

    }
}
