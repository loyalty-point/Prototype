package com.thesis.dont.loyaltypointadmin.controllers;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Global;
import com.thesis.dont.loyaltypointadmin.models.User;

public class CustomerRegisterInfoActivity extends ActionBarActivity {

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_register_info);

        Intent i = getIntent();
        mUser = i.getParcelableExtra(Global.USER_OBJECT);
    }


}
