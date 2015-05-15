package com.thesis.dont.loyaltypointuser.controllers;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.thesis.dont.loyaltypointuser.models.Global;
import com.thesis.dont.loyaltypointuser.models.Shop;
import com.thesis.dont.loyaltypointuser.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 11120_000 on 17/04/15.
 */
public class ListCardsPagerAdapter extends FragmentPagerAdapter {

    Context mContext;
    ArrayList<Shop> mShops;
    private User mUser = null;
    boolean mIsPendingCards;

    public ListCardsPagerAdapter(FragmentManager fm, Context context, ArrayList<Shop> shops, boolean isPending) {
        super(fm);
        mContext = context;
        mShops = shops;
        mIsPendingCards = isPending;
    }

    @Override
    public Fragment getItem(int position) {
        if(mShops.size() > position) {
            Shop shop = mShops.get(position);

            return CardFragment.newInstance(shop, mUser, mIsPendingCards);
        }

        return null;
    }

    public void setListShops(ArrayList<Shop> listShops) {
        mShops = listShops;
    }

    @Override
    public int getCount() {
        return mShops.size();
    }

    public java.lang.CharSequence getPageTitle(int position) {
        return String.valueOf(position + 1);
    }

    public void setUser(User user) {
        mUser = user;
    }
}