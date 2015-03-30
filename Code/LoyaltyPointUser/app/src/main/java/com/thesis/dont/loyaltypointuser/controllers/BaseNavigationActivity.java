package com.thesis.dont.loyaltypointuser.controllers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.thesis.dont.loyaltypointuser.R;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialAccount;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;
import it.neokree.materialnavigationdrawer.elements.listeners.MaterialSectionListener;

public class BaseNavigationActivity extends MaterialNavigationDrawer {

    @Override
    public void init(Bundle savedInstanceState) {
        allowArrowAnimation();

        MaterialAccount account = new MaterialAccount(this.getResources(),"Nguyễn Tấn Đô","nguyentando106@gmail.com", R.drawable.user_avatar, R.color.MaterialGreen);
        //Bitmap bm = BitmapFactory.decodeResource(getResources(), R.color.MaterialBlueGrey);
        int color[] = {R.color.MaterialDeepOrange};
        Bitmap bm = Bitmap.createBitmap(color, 1, 1, Bitmap.Config.ARGB_8888);
        //bm.eraseColor(R.color.MaterialDeepOrange);
        //account.setBackground(bm);
        account.setBackground(R.drawable.account_background);

        //account.setBackground(R.color.MaterialBlueGrey);
        this.addAccount(account);

        // add section to the top list
        MaterialSection sectionTop1 = newSection("My Cards", R.drawable.ic_store, new ShopsCardMainFragment());
        //MaterialSection sectionTop2 = newSection("Bar Code", R.drawable.ic_store, new Intent(this, BarCodeActivity.class));
        MaterialSection sectionTop2 = newSection("My Account", R.drawable.account_circle_ic, new AccountFragment());
        sectionTop1.setSectionColor(getResources().getColor(R.color.DarkPrimaryColor), getResources().getColor(R.color.DarkPrimaryColor));
        sectionTop2.setSectionColor(getResources().getColor(R.color.DarkPrimaryColor), getResources().getColor(R.color.DarkPrimaryColor));
        //sectionTop1.setNotifications(4);

        /*MaterialSection sectionTop2 = newSection("Section 2", R.drawable.ic_user, new Intent(this, RegisterActivity.class));*/
        //sectionTop2.setNotifications(10);

        addSection(sectionTop1);
        addSection(sectionTop2);
        /*addSection(sectionTop2);*/

        // add section to the bottom list
        MaterialSection sectionBottom1 = newSection("Settings", R.drawable.ic_settings, new Intent(this, SettingsActivity.class));
        MaterialSection sectionBottom2 = newSection("Help & Feedback", R.drawable.ic_help, new Intent(this, SettingsActivity.class));
        MaterialSection sectionBottom3 = newSection("About", R.drawable.ic_about, new Intent(this, SettingsActivity.class));
        MaterialSection sectionBottom4 = newSection("Log Out", R.drawable.ic_logout, new MaterialSectionListener() {
            @Override
            public void onClick(MaterialSection materialSection) {
                // Xóa token trong shared preferences
                SharedPreferences preferences = getSharedPreferences(LoginActivity.LOGIN_STATE, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(LoginActivity.TOKEN, "");

                editor.commit();

                Intent i = new Intent(materialSection.getView().getContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        addBottomSection(sectionBottom1);
        addBottomSection(sectionBottom2);
        addBottomSection(sectionBottom3);
        addBottomSection(sectionBottom4);


        android.support.v7.widget.Toolbar toolbar = getToolbar();
        setSupportActionBar(toolbar);


        toolbar.setNavigationIcon(R.drawable.navigation_icon);
        toolbar.setTitleTextColor(getResources().getColor(R.color.TextIcons));


        disableLearningPattern();
    }
}
