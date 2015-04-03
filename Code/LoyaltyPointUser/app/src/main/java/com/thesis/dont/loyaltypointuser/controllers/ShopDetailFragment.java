package com.thesis.dont.loyaltypointuser.controllers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;

import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointuser.R;
import com.thesis.dont.loyaltypointuser.models.Global;
import com.thesis.dont.loyaltypointuser.models.Shop;
import com.thesis.dont.loyaltypointuser.models.ShopModel;


import butterknife.ButterKnife;

public class ShopDetailFragment extends Fragment {
    private static final String ARG_POSITION = "position";
    private static final String ARG_SHOPID = "shopId";

    private int position;
    private String shopId;
    View rootView;

    public ShopDetailFragment(int position, String shopId) {
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        b.putString(ARG_SHOPID, shopId);
        this.setArguments(b);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
        shopId = getArguments().getString(ARG_SHOPID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.fragment_shop_detail, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        ButterKnife.inject(this, rootView);
        ViewCompat.setElevation(rootView, 50);
        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final TextView shopName = (TextView)getActivity().findViewById(R.id.shopNameTv);
        final ImageView shopImg = (ImageView)getActivity().findViewById(R.id.shopImg);
        ShopModel.getShopInfo(Global.userToken, this.shopId, new ShopModel.OnGetShopInfoResult() {
            @Override
            public void onSuccess(final Shop shop) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        shopName.setText(shop.getName());
                        Picasso.with(getActivity()).load(shop.getImage()).placeholder(R.drawable.ic_award).into(shopImg);
                    }
                });

            }

            @Override
            public void onError(String error) {
//                shopName.setText(error);
            }
        });
    }

}
