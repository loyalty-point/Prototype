package com.thesis.dont.loyaltypointuser.controllers;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.thesis.dont.loyaltypointuser.models.Card;
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
    ArrayList<Card> mCards;
    private User mUser = null;
    boolean mIsPendingCards;

    public ListCardsPagerAdapter(FragmentManager fm, Context context, ArrayList<Card> cards, boolean isPending) {
        super(fm);
        mContext = context;
        mCards = cards;
        mIsPendingCards = isPending;
    }

    @Override
    public Fragment getItem(int position) {
        if(mCards.size() > position) {
            Card card = mCards.get(position);
            String qrCode = card.getId() + mUser.getUsername();

            return CardFragment.newInstance(card, mUser, mIsPendingCards, qrCode);
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
        return String.valueOf(position + 1);
    }

    public void setUser(User user) {
        mUser = user;
    }
}