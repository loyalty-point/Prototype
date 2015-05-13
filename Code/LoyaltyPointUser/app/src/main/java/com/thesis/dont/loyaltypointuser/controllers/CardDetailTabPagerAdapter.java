package com.thesis.dont.loyaltypointuser.controllers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

/**
 * Created by tinntt on 5/13/2015.
 */
public class CardDetailTabPagerAdapter extends FragmentPagerAdapter {
    private final String[] TITLES = {"List Shop", "Events", "Awards", "Users", "History"};
    private String cardId = null;

    public CardDetailTabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public CardDetailTabPagerAdapter(FragmentManager fm, String cardId) {
        super(fm);
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
            return new CardDetailFragment();
        else if (position == 1)
            return new CardEventsFragment();
        else if (position == 2)
            return new CardAwardsFragment();
        else if (position == 3)
            return new CardUsersFragment();
        else
            return new CardHistoriesFragment();

    }
}
