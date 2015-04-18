package com.thesis.dont.loyaltypointuser.controllers;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.AccordionTransformer;
import com.ToxicBakery.viewpager.transforms.BackgroundToForegroundTransformer;
import com.ToxicBakery.viewpager.transforms.CubeInTransformer;
import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.ToxicBakery.viewpager.transforms.DepthPageTransformer;
import com.ToxicBakery.viewpager.transforms.FlipHorizontalTransformer;
import com.ToxicBakery.viewpager.transforms.ForegroundToBackgroundTransformer;
import com.ToxicBakery.viewpager.transforms.RotateDownTransformer;
import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.ToxicBakery.viewpager.transforms.ScaleInOutTransformer;
import com.ToxicBakery.viewpager.transforms.StackTransformer;
import com.ToxicBakery.viewpager.transforms.TabletTransformer;
import com.ToxicBakery.viewpager.transforms.ZoomInTransformer;
import com.gc.materialdesign.views.ButtonFloat;
import com.thesis.dont.loyaltypointuser.R;
import com.thesis.dont.loyaltypointuser.models.Global;
import com.thesis.dont.loyaltypointuser.models.Shop;
import com.thesis.dont.loyaltypointuser.models.ShopModel;

import java.util.ArrayList;

import github.chenupt.multiplemodel.viewpager.ModelPagerAdapter;
import github.chenupt.multiplemodel.viewpager.PagerModelManager;
import github.chenupt.springindicator.SpringIndicator;

public class ShopsCardMainFragment extends Fragment {
    public static String SHOP_OBJECT = "shop_object";

    ListView mListView;
    ShopsListAdapter mAdapter;
    public Activity mParentActivity = null;

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private SpringIndicator mIndicator;

    ArrayList<Shop> mListCards, mListRegisters;

    TextView yourCardsTxt, yourRegistersTxt;
    int cardType = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_shops_card_main, container, false);

        return view;
    }

    public void populateList(ArrayList<Shop> listShops) {
        mPagerAdapter = new ListCardsPagerAdapter(((CardsListActivity)mParentActivity).getSupportFragmentManager(), mParentActivity, listShops);
        mParentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mPager.setAdapter(mPagerAdapter);
                mPagerAdapter.notifyDataSetChanged();
                if (mListCards.size() > 0) {
                    mIndicator.removeAllViews();
                    mIndicator.setViewPager(mPager);
                    mIndicator.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mParentActivity = getActivity();

        yourCardsTxt = (TextView) mParentActivity.findViewById(R.id.listCardsText);
        yourRegistersTxt = (TextView) mParentActivity.findViewById(R.id.listRegistersText);

        yourCardsTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardType = 0;

                yourCardsTxt.setTextColor(mParentActivity.getResources().getColor(R.color.AccentColor));
                yourRegistersTxt.setTextColor(mParentActivity.getResources().getColor(R.color.MaterialGrey));

                populateList(mListCards);
            }
        });

        yourRegistersTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardType = 1;

                yourRegistersTxt.setTextColor(mParentActivity.getResources().getColor(R.color.AccentColor));
                yourCardsTxt.setTextColor(mParentActivity.getResources().getColor(R.color.MaterialGrey));

                populateList(mListRegisters);
            }
        });

        ButtonFloat createShopBtn = (ButtonFloat) mParentActivity.findViewById(R.id.createShopBtn);
        createShopBtn.setBackgroundColor(getResources().getColor(R.color.AccentColor));
        createShopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mParentActivity, SearchShopActivity.class);
                startActivity(i);
            }
        });

        /*mListView = (ListView) mParentActivity.findViewById(R.id.shop_list);

        ButtonFloat createShopBtn = (ButtonFloat) mParentActivity.findViewById(R.id.createShopBtn);
        createShopBtn.setBackgroundColor(getResources().getColor(R.color.AccentColor));
        createShopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mParentActivity, SearchShopActivity.class);
                startActivity(i);
            }
        });

        setListData();*/

        mPager = (ViewPager) mParentActivity.findViewById(R.id.listCardsPager);
        mPager.setPageTransformer(true, new CubeOutTransformer());
        mPager.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        /*ArrayList<Shop> mListShops = new ArrayList<Shop>();
        mPagerAdapter = new ListCardsPagerAdapter(((CardsListActivity)mParentActivity).getSupportFragmentManager(), mParentActivity, mListShops);

        mPager.setAdapter(mPagerAdapter);*/

        mIndicator = (SpringIndicator) mParentActivity.findViewById(R.id.listCardsIndicator);
        mIndicator.setVisibility(View.GONE);


        setListData();

        /*PagerModelManager manager = new PagerModelManager();
        manager.addCommonFragment(GuideFragment.class, getBgRes(), getTitles());
        manager.add
        ModelPagerAdapter adapter = new ModelPagerAdapter(((CardsListActivity)mParentActivity).getSupportFragmentManager(), manager);*/

        //indicator.setViewPager(mPager);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setListData()
    {
        ShopModel.getFollowedShop(Global.userToken, new ShopModel.OnSelectAllShopResult() {
            @Override
            public void onSuccess(final ArrayList<Shop> listShops) {

                /*mAdapter = new ShopsListAdapter(mParentActivity, listShops);

                mParentActivity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        mListView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    }
                });*/

                mListCards = new ArrayList<Shop>();
                mListRegisters = new ArrayList<Shop>();
                for(Shop shop : listShops) {
                    if(shop.isAccepted() == 1)
                        mListCards.add(shop);
                    else
                        mListRegisters.add(shop);
                }

                if(cardType == 0)
                    populateList(mListCards);
                else
                    populateList(mListRegisters);
            }

            @Override
            public void onError(final String error) {
                mParentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mParentActivity, error, Toast.LENGTH_LONG);
                    }
                });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        //setListData();
    }
}
