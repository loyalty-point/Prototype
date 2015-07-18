package com.thesis.dont.loyaltypointuser.controllers;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.thesis.dont.loyaltypointuser.R;
import com.thesis.dont.loyaltypointuser.models.Global;

public class CardsListActivity extends BaseNavigationActivity
        implements AccountFragment.OnFragmentInteractionListener
        , CardFragment.OnFragmentInteractionListener
        , PendingCardsFragment.OnFragmentInteractionListener
        , ShopsCardMainFragment.OnFragmentInteractionListener {

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        Intent i = getIntent();
        int fragmentID = i.getIntExtra(Global.FRAGMENT_ID, 0);

        setDefaultSectionLoaded(fragmentID);

        /*FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(new ShopsCardMainFragment(), "ShopsListMainFragment");*/
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onFragmentInteraction(Uri uri) {
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_list_cards, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_refresh:
                android.support.v4.app.Fragment currentFragment = getSupportFragmentManager().getFragments().get(0);
                if(currentFragment instanceof ShopsCardMainFragment)
                    ((ShopsCardMainFragment)currentFragment).refresh();
                else if(currentFragment instanceof PendingCardsFragment)
                    ((PendingCardsFragment)currentFragment).refresh();
                else if(currentFragment instanceof AccountFragment)
                    ((AccountFragment)currentFragment).refresh();
                else if(currentFragment instanceof MyAwardsFragment)
                    ((MyAwardsFragment)currentFragment).refresh();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
