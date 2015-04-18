package com.thesis.dont.loyaltypointuser.controllers;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.thesis.dont.loyaltypointuser.models.Shop;

import java.util.ArrayList;

/**
 * Created by 11120_000 on 17/04/15.
 */
public class ListCardsPagerAdapter extends FragmentStatePagerAdapter {

    Context mContext;
    ArrayList<Shop> mShops;

    public ListCardsPagerAdapter(FragmentManager fm, Context context, ArrayList<Shop> shops) {
        super(fm);
        mContext = context;
        mShops = shops;
    }

    @Override
    public Fragment getItem(int position) {
        if(mShops.size() > position) {
            Shop shop = mShops.get(position);
            return new CardFragment(shop);
        }

        return null;
    }

    @Override
    public int getCount() {
        return mShops.size();
    }

    public java.lang.CharSequence getPageTitle(int position) {
        return String.valueOf(position+1);
    }
}
