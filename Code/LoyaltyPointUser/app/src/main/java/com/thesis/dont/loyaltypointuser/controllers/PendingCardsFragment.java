package com.thesis.dont.loyaltypointuser.controllers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.ZoomOutSlideTransformer;
import com.gc.materialdesign.views.ButtonRectangle;
import com.thesis.dont.loyaltypointuser.R;
import com.thesis.dont.loyaltypointuser.models.Global;
import com.thesis.dont.loyaltypointuser.models.Shop;
import com.thesis.dont.loyaltypointuser.models.ShopModel;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PendingCardsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PendingCardsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PendingCardsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public Activity mParentActivity = null;

    private ViewPager mPager;
    private ListCardsPagerAdapter mPagerAdapter;
    private CircleIndicator mIndicator;

    ArrayList<Shop> mListCards;

    ProgressDialog mDialog;


    public static PendingCardsFragment newInstance() {
        PendingCardsFragment fragment = new PendingCardsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public PendingCardsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setListData() {
        mDialog.show();
        ShopModel.getFollowedShop(Global.userToken, new ShopModel.OnSelectAllShopResult() {
            @Override
            public void onSuccess(final ArrayList<Shop> listShops) {

                mListCards = new ArrayList<Shop>();
                for (Shop shop : listShops) {
                    if (shop.isAccepted() == 0)
                        mListCards.add(shop);
                }
                populateList(mListCards);
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mParentActivity = getActivity();

        // init dialog
        mDialog = new ProgressDialog(mParentActivity);
        mDialog.setTitle("Loading data");
        mDialog.setMessage("Please wait...");
        mDialog.setCancelable(false);

        mListCards = new ArrayList<Shop>();

        mPager = (ViewPager) mParentActivity.findViewById(R.id.listCardsPager);
        mPager.setPageTransformer(true, new ZoomOutSlideTransformer());

        mPagerAdapter = new ListCardsPagerAdapter(getChildFragmentManager(), mParentActivity, new ArrayList<Shop>());
        mPager.setAdapter(mPagerAdapter);

        mIndicator = (CircleIndicator) mParentActivity.findViewById(R.id.custom_indicator);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pending_cards, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public void populateList(final ArrayList<Shop> listShops) {

        mParentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mPagerAdapter.setListShops(listShops);
                mPagerAdapter.notifyDataSetChanged();
                if(listShops.size() > 0) {
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

    @Override
    public void onResume() {
        super.onResume();

        setListData();
    }

}
