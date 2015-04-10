package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFloat;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Global;
import com.thesis.dont.loyaltypointadmin.models.Shop;
import com.thesis.dont.loyaltypointadmin.models.ShopModel;

import java.util.ArrayList;


/**
 * Created by 11120_000 on 10/03/15.
 */
public class ShopsListMainFragment extends Fragment {

    ListView mListView;
    ShopsListAdapter mAdapter;
    public ShopsListActivity mParentActivity = null;

    ProgressDialog mDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.shops_list_main_fragment, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.e("resume", "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        mParentActivity = (ShopsListActivity) getActivity();

        // init dialog
        mDialog = new ProgressDialog(mParentActivity);
        mDialog.setTitle("Loading data");
        mDialog.setMessage("Please wait...");
        mDialog.setCancelable(false);

        mListView = (ListView) mParentActivity.findViewById(R.id.shop_list);

        ButtonFloat createShopBtn = (ButtonFloat) mParentActivity.findViewById(R.id.createShopBtn);
        createShopBtn.setBackgroundColor(getResources().getColor(R.color.AccentColor));
        createShopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mParentActivity, CreateShopActivity.class);
                startActivity(i);
            }
        });

        //setListData();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setListData()
    {
        mDialog.show();
        ShopModel.setOnSelectListShopResult(new ShopModel.OnSelectListShopResult() {
            @Override
            public void onSuccess(ArrayList<Shop> listShops) {

                mAdapter = new ShopsListAdapter(mParentActivity, listShops);

                mParentActivity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        mDialog.dismiss();
                        mListView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onError(final String error) {
                mParentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.dismiss();
                        Toast.makeText(mParentActivity, error, Toast.LENGTH_LONG);
                    }
                });
            }
        });
        ShopModel.getListShop(Global.userToken);
    }

    @Override
    public void onResume() {
        super.onResume();
        setListData();
    }
}
