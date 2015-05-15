package com.thesis.dont.loyaltypointuser.controllers;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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


}
