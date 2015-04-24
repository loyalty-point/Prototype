package com.thesis.dont.loyaltypointadmin.controllers;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.thesis.dont.loyaltypointadmin.models.Shop;

import java.util.ArrayList;

/**
 * Created by 11120_000 on 22/04/15.
 */
public class ListShopsPagerAdapter extends FragmentStatePagerAdapter {

    Context mContext;
    ArrayList<Shop> mShops;

    public ListShopsPagerAdapter(FragmentManager fm, Context context, ArrayList<Shop> shops) {
        super(fm);
        mContext = context;
        mShops = shops;
    }

    @Override
    public Fragment getItem(int position) {
        if(mShops.size() > position) {
            Shop shop = mShops.get(position);
            return new CardFragment(mContext, shop);
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

