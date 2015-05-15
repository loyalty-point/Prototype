package com.thesis.dont.loyaltypointadmin.controllers;

import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.ToxicBakery.viewpager.transforms.RotateDownTransformer;
import com.gc.materialdesign.views.ButtonFloat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Card;
import com.thesis.dont.loyaltypointadmin.models.CardModel;
import com.thesis.dont.loyaltypointadmin.models.Global;
import com.thesis.dont.loyaltypointadmin.models.Shop;
import com.thesis.dont.loyaltypointadmin.models.ShopModel;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;


/**
 * Created by 11120_000 on 10/03/15.
 */
public class CardsListMainFragment extends Fragment {

    public CardsListActivity mParentActivity = null;

    private ViewPager mPager;
    private ListCardsPagerAdapter mPagerAdapter;
    private CircleIndicator mIndicator;

    ProgressDialog mDialog;

    public CardsListMainFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.cards_list_main_fragment, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.e("resume", "onActivityCreated");
        super.onActivityCreated(savedInstanceState);

        mParentActivity = (CardsListActivity) getActivity();

        // init dialog
        mDialog = new ProgressDialog(mParentActivity);
        mDialog.setTitle("Loading data");
        mDialog.setMessage("Please wait...");
        mDialog.setCancelable(false);

        //mListView = (ListView) mParentActivity.findViewById(R.id.shop_list);

        ButtonRectangle createShopBtn = (ButtonRectangle) mParentActivity.findViewById(R.id.createShopBtn);
        createShopBtn.setBackgroundColor(getResources().getColor(R.color.AccentColor));
        createShopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mParentActivity, CreateShopActivity.class);
                startActivity(i);
            }
        });

        mPager = (ViewPager) mParentActivity.findViewById(R.id.listCardsPager);
        mPager.setPageTransformer(true, new RotateDownTransformer());

        mPagerAdapter = new ListCardsPagerAdapter(getChildFragmentManager(), mParentActivity, new ArrayList<Card>());
        mPager.setAdapter(mPagerAdapter);

        mIndicator = (CircleIndicator) mParentActivity.findViewById(R.id.custom_indicator);

        setListData();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void populateList(final ArrayList<Card> listCards) {
        //mPagerAdapter = new ListCardsPagerAdapter(((CardsListActivity)mParentActivity).getSupportFragmentManager(), mParentActivity, listCards);
        mParentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mPager.setCurrentItem(0);
                mPagerAdapter.setListCards(listCards);
                mPagerAdapter.notifyDataSetChanged();
                if(listCards.size() > 0) {
                    mIndicator.setViewPager(mPager);
                    mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {

                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });
                }

                mDialog.dismiss();
            }
        });
    }

    public void setListData()
    {
        mDialog.show();
        CardModel.getListCards(Global.userToken, new CardModel.OnGetListResult() {
            @Override
            public void onSuccess(ArrayList<Card> listCards) {
                populateList(listCards);
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
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
