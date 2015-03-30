package com.thesis.dont.loyaltypointuser.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFloat;
import com.thesis.dont.loyaltypointuser.R;
import com.thesis.dont.loyaltypointuser.models.Award;
import com.thesis.dont.loyaltypointuser.models.AwardModel;
import com.thesis.dont.loyaltypointuser.models.Global;

import java.util.ArrayList;

import butterknife.ButterKnife;

import com.thesis.dont.loyaltypointuser.R;

import butterknife.ButterKnife;


public class ShopHistoryFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    private static final String ARG_SHOPID = "shop_id";
    public static final String AWARD_OBJECT = "award_object";

    private int position;
    private String shopId;

    public ShopHistoryFragment() {
        // Required empty public constructor
    }

    public ShopHistoryFragment(int position, String shopId){
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        this.setArguments(b);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
        shopId = getArguments().getString(ARG_SHOPID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shop_history, container, false);
        ButterKnife.inject(this, rootView);
        ViewCompat.setElevation(rootView, 50);
        return rootView;
    }
}
