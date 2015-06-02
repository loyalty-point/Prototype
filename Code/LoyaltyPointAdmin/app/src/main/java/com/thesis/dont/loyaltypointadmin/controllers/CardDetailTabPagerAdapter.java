package com.thesis.dont.loyaltypointadmin.controllers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

/**
 * Created by tinntt on 5/13/2015.
 */
public class CardDetailTabPagerAdapter extends FragmentPagerAdapter {
    private final String[] TITLES = {"List Shop", "Events", "Awards", "Users", "Register", "History"};
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
            return CardDetailFragment.newInstance(position, cardId);
        else if (position == 1)
            return CardEventsFragment.newInstance(position, cardId);
        else if (position == 2)
            return CardAwardsFragment.newInstance(position, cardId);
        else if (position == 3)
            return new CardUsersFragment(position, cardId);
        else if (position ==4)
            return new CardRegisterFragment(position, cardId);
        else
            return new CardHistoriesFragment();

    }
}
