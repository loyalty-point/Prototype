package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFloat;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Award;
import com.thesis.dont.loyaltypointadmin.models.AwardModel;
import com.thesis.dont.loyaltypointadmin.models.Global;
import com.thesis.dont.loyaltypointadmin.models.History;
import com.thesis.dont.loyaltypointadmin.models.ShopModel;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class HistoryFragment extends Fragment {

    public static final String SHOP_ID = "shop_ID";

    String shopID;

    Activity mParentActivity;

    ListView mListView;
    ListHistoriesAdapter mAdapter;

    public HistoryFragment() {
        // Required empty public constructor
    }

    public HistoryFragment(int position, String shopId){
        Bundle b = new Bundle();
        b.putString(SHOP_ID, shopId);
        this.setArguments(b);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopID = getArguments().getString(SHOP_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history,container,false);
        ButterKnife.inject(this, rootView);
        ViewCompat.setElevation(rootView, 50);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        getListHistory();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mParentActivity = getActivity();

        shopID = ((ShopDetailActivity)getActivity()).getCurrentShop().getId();

        // init list histories
        mListView = (ListView) mParentActivity.findViewById(R.id.listHistories);
        mAdapter = new ListHistoriesAdapter(mParentActivity, new ArrayList<History>());
        mListView.setAdapter(mAdapter);
    }

    public void getListHistory() {
        ShopModel.getHistory(Global.userToken, shopID, new ShopModel.OnGetHistoryResult() {
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
