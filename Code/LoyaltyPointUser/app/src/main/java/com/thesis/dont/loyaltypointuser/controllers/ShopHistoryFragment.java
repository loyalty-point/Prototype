package com.thesis.dont.loyaltypointuser.controllers;

import android.app.Activity;
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
import com.thesis.dont.loyaltypointuser.models.History;
import com.thesis.dont.loyaltypointuser.models.ShopModel;
import com.thesis.dont.loyaltypointuser.models.UserModel;

import butterknife.ButterKnife;


public class ShopHistoryFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    private static final String ARG_SHOPID = "shop_id";
    private static final String ARG_SHOPNAME = "shop_name";
    private static final String ARG_SHOPADDRESS = "shop_address";
    public static final String AWARD_OBJECT = "award_object";

    private int position;
    private String shopId;
    private String shopName;
    private String shopAddress;

    ListView mListView;
    Activity mParentActivity;
    ListHistoriesAdapter mAdapter;

    public ShopHistoryFragment() {
        // Required empty public constructor
    }

    public ShopHistoryFragment(int position, String shopId, String shopName, String shopAddress){
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        b.putString(ARG_SHOPID, shopId);
        b.putString(ARG_SHOPNAME, shopName);
        b.putString(ARG_SHOPADDRESS, shopAddress);
        this.setArguments(b);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
        shopId = getArguments().getString(ARG_SHOPID);
        shopName = getArguments().getString(ARG_SHOPNAME);
        shopAddress = getArguments().getString(ARG_SHOPADDRESS);
    }

    @Override
    public void onResume() {
        super.onResume();
        getListHistory();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shop_history, container, false);
        ButterKnife.inject(this, rootView);
        ViewCompat.setElevation(rootView, 50);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mParentActivity = getActivity();

        // init list histories
        mListView = (ListView) mParentActivity.findViewById(R.id.listHistories);
        mAdapter = new ListHistoriesAdapter(mParentActivity, new ArrayList<History>(), shopName, shopAddress);
        mListView.setAdapter(mAdapter);
    }

    public void getListHistory() {
        UserModel.getHistory(Global.userToken, shopId, new UserModel.OnGetHistoryResult() {
            @Override
            public void onSuccess(ArrayList<History> listHistories) {
                mAdapter.setListHistories(listHistories);
                mParentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onError(final String error) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Get listAwards không thành công
                        Toast.makeText(mParentActivity, error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
