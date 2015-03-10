package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Global;
import com.thesis.dont.loyaltypointadmin.models.UserModel;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialAccount;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;

/**
 * Created by 11120_000 on 09/03/15.
 */
public class BaseNavigationActivity extends MaterialNavigationDrawer {

    @Override
    public void init(Bundle savedInstanceState) {
        MaterialAccount account = new MaterialAccount(this.getResources(),"Nguyễn Tấn Đô","nguyentando106@gmail.com", R.drawable.user_avatar, R.color.MaterialGreen);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.user_cover);
        account.setBackground(bm);
        this.addAccount(account);

        // add section to the top list
        MaterialSection sectionTop1 = newSection("Shops List", R.drawable.ic_user, new ShopsListMainFragment());
        //sectionTop1.setNotifications(4);

        MaterialSection sectionTop2 = newSection("Section 2", R.drawable.ic_user, new Intent(this, RegisterActivity.class));
        //sectionTop2.setNotifications(10);

        addSection(sectionTop1);
        addSection(sectionTop2);

        // add section to the bottom list
        MaterialSection sectionBottom1 = newSection("Settings", R.drawable.ic_user, new Intent(this, SettingsActivity.class));
        MaterialSection sectionBottom2 = newSection("Help & Feedback", R.drawable.ic_user, new Intent(this, SettingsActivity.class));
        MaterialSection sectionBottom3 = newSection("About", R.drawable.ic_user, new Intent(this, SettingsActivity.class));

        addBottomSection(sectionBottom1);
        addBottomSection(sectionBottom2);
        addBottomSection(sectionBottom3);

        // add section to the account list
        MaterialSection sectionAccount1 = newSection("Nguyễn Tấn Đô", R.drawable.ic_user, new Intent(this, ShopsListActivity.class));
        MaterialSection sectionAccount2 = newSection("Nguyễn Trương Trung Tín", R.drawable.ic_user, new Intent(this, ShopsListActivity.class));
        addAccountSection(sectionAccount1);
        addAccountSection(sectionAccount2);

        ActionBar actionBar = getActionBar();
        actionBar.getCustomView();
    }
}
