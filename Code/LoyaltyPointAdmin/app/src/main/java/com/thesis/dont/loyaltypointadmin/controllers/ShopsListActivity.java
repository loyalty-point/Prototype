package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;

import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFloat;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Global;
import com.thesis.dont.loyaltypointadmin.models.Shop;
import com.thesis.dont.loyaltypointadmin.models.ShopModel;
import com.tjerkw.slideexpandable.library.ActionSlideExpandableListView;
import com.tjerkw.slideexpandable.library.SlideExpandableListAdapter;

import java.util.ArrayList;

public class ShopsListActivity extends BaseNavigationActivity {

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(new ShopsListMainFragment(), "ShopsListMainFragment");
    }



    /*****************  This function used by adapter ****************/
//    public void onItemClick(int mPosition)
//    {
//        ShopCard tempValues = (ShopCard) CustomListViewValuesArr.get(mPosition);
//
//
//        // SHOW ALERT
//
//        Toast.makeText(CustomListView,
//                "" + tempValues.getShopname() + "Image:"+tempValues.getImg()+"Url:"+tempValues.getAddress(),Toast.LENGTH_LONG).show();
//    }

}
