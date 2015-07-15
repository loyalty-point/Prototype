package com.thesis.dont.loyaltypointadmin.controllers;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.thesis.dont.loyaltypointadmin.models.Card;
import com.thesis.dont.loyaltypointadmin.models.Shop;

import java.util.ArrayList;

/**
 * Created by 11120_000 on 22/04/15.
 */
public class ListCardsPagerAdapter extends FragmentPagerAdapter {

    Context mContext;
    ArrayList<Card> mCards;

    public ListCardsPagerAdapter(FragmentManager fm, Context context, ArrayList<Card> cards) {
        super(fm);
        mContext = context;
        mCards = cards;
    }

    @Override
    public Fragment getItem(int position) {
        if(mCards.size() > position) {
            Card card = mCards.get(position);
            return new CardFragment(mContext, card);
        }

        return null;
    }

    public void setListCards(ArrayList<Card> listCards) {
        mCards = listCards;
    }

    @Override
    public int getCount() {
        return mCards.size();
    }

    public java.lang.CharSequence getPageTitle(int position) {
        return String.valueOf(position+1);
    }
}

