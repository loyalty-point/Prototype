package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Global;
import com.thesis.dont.loyaltypointadmin.models.UserModel;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialAccount;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;
import it.neokree.materialnavigationdrawer.elements.listeners.MaterialSectionListener;

/**
 * Created by 11120_000 on 09/03/15.
 */
public class BaseNavigationActivity extends MaterialNavigationDrawer {

    @Override
    public void init(Bundle savedInstanceState) {
        allowArrowAnimation();

        MaterialAccount account = new MaterialAccount(this.getResources(),"Nguyễn Tấn Đô","nguyentando106@gmail.com", R.drawable.user_avatar, R.color.MaterialGreen);
        //Bitmap bm = BitmapFactory.decodeResource(getResources(), R.color.MaterialBlueGrey);
        int color[] = {R.color.MaterialDeepOrange};
        Bitmap bm = Bitmap.createBitmap(color, 1, 1, Bitmap.Config.ARGB_8888);
        //bm.eraseColor(R.color.MaterialDeepOrange);
        account.setBackground(bm);

        //account.setBackground(R.color.MaterialBlueGrey);
        this.addAccount(account);

        // add section to the top list
        MaterialSection sectionTop1 = newSection("Shops List", R.drawable.ic_store, new ShopsListMainFragment());
        //sectionTop1.setNotifications(4);

        MaterialSection sectionTop2 = newSection("Section 2", R.drawable.ic_user, new Intent(this, RegisterActivity.class));
        //sectionTop2.setNotifications(10);

        addSection(sectionTop1);
        addSection(sectionTop2);

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

        // add section to the account list
        MaterialSection sectionAccount1 = newSection("Nguyễn Tấn Đô", R.drawable.ic_user, new Intent(this, ShopsListActivity.class));
        MaterialSection sectionAccount2 = newSection("Nguyễn Trương Trung Tín", R.drawable.ic_user, new Intent(this, ShopsListActivity.class));
        addAccountSection(sectionAccount1);
        addAccountSection(sectionAccount2);

        getToolbar().setTitleTextColor(getResources().getColor(R.color.TextIcons));
        getToolbar().setNavigationIcon(R.drawable.navigation_icon);
        //getToolbar().navigation

        disableLearningPattern();
    }
}
