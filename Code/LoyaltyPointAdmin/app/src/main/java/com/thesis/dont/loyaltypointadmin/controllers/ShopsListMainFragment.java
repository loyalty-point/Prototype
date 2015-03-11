package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thesis.dont.loyaltypointadmin.R;


/**
 * Created by 11120_000 on 10/03/15.
 */
public class ShopsListMainFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.shops_list_main_fragment, container, false);
    }
}
