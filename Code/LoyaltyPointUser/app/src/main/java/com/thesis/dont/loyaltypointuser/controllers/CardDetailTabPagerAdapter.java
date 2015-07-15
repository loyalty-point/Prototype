package com.thesis.dont.loyaltypointuser.controllers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.thesis.dont.loyaltypointuser.models.Card;

/**
 * Created by tinntt on 5/13/2015.
 */
public class CardDetailTabPagerAdapter extends FragmentPagerAdapter {
    private final String[] TITLES = {"List Shop", "Events", "Awards", "History"};
    private Card mCard = null;
    private int userPoint = 0;

    public CardDetailTabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public CardDetailTabPagerAdapter(FragmentManager fm, Card card, int userPoint) {
        super(fm);
        this.mCard = card;
        this.userPoint = userPoint;
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
            return new CardDetailFragment(position, mCard);
        else if (position == 1)
            return new CardEventsFragment(position, mCard);
        else if (position == 2)
            return new CardAwardsFragment(position, mCard);
        else
            return new CardHistoriesFragment(position,mCard.getId());

    }
}
