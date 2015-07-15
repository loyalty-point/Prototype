package com.thesis.dont.loyaltypointadmin.controllers;

/**
 * Created by tinntt on 3/11/2015.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.thesis.dont.loyaltypointadmin.models.Customer;

public class UserDetailTabPagerAdapter extends FragmentPagerAdapter {
    private final String[] TITLES = {"Home", "Awards", "Tickets"};
    private Customer mCustomer;
    private String shopId = null;
    private String cardId = null;

    public UserDetailTabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public UserDetailTabPagerAdapter(FragmentManager fm, Customer customer, String shopId, String cardId) {
        super(fm);
        this.mCustomer = customer;
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
            return new UserDetailFragment(mCustomer, this.shopId);
        else if (position == 1)
            return new UserAwardFragment(position,mCustomer.getUsername(), this.shopId, this.cardId);
        else
            return new UserTicketsFragment(position, mCustomer.getUsername(), this.shopId, this.cardId);

    }
}
