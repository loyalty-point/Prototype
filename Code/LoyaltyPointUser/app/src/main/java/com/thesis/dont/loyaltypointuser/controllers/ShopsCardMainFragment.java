package com.thesis.dont.loyaltypointuser.controllers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.RotateDownTransformer;
import com.ToxicBakery.viewpager.transforms.ZoomOutSlideTransformer;
//import com.bartoszlipinski.flippablestackview.FlippableStackView;
import com.bartoszlipinski.flippablestackview.FlippableStackView;
import com.bartoszlipinski.flippablestackview.StackPageTransformer;
import com.gc.materialdesign.views.ButtonRectangle;
import com.thesis.dont.loyaltypointuser.R;
import com.thesis.dont.loyaltypointuser.models.Card;
import com.thesis.dont.loyaltypointuser.models.CardModel;
import com.thesis.dont.loyaltypointuser.models.Global;
import com.thesis.dont.loyaltypointuser.models.Shop;
import com.thesis.dont.loyaltypointuser.models.ShopModel;
import com.thesis.dont.loyaltypointuser.models.User;
import com.thesis.dont.loyaltypointuser.models.UserModel;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PendingCardsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PendingCardsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShopsCardMainFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public Activity mParentActivity = null;

    private FlippableStackView mPager;
    private ListCardsPagerAdapter mPagerAdapter;

    ArrayList<Card> mListCards;

    ProgressDialog mDialog;
    User mUser;


    public static ShopsCardMainFragment newInstance() {
        ShopsCardMainFragment fragment = new ShopsCardMainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ShopsCardMainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setListData() {
        mDialog.show();
        CardModel.getFollowedCards(Global.userToken, new CardModel.OnGetListResult() {
            @Override
            public void onSuccess(final ArrayList<Card> listCards) {
                //Get user info
                UserModel.getUserInfo(Global.userToken, new UserModel.OnGetUserInfoResult() {
                    @Override
                    public void onSuccess(User user) {

                        mListCards = new ArrayList<Card>();
                        for (Card card : listCards) {
                            if (card.getIsAccepted() == 1)
                                mListCards.add(card);
                        }
                        mUser = user;
                        populateList(mListCards, user);
                    }

                    @Override
                    public void onError(final String e) {
                        mParentActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mParentActivity, e, Toast.LENGTH_LONG).show();
                            }
                        });
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

        ButtonRectangle addCardBtn = (ButtonRectangle) mParentActivity.findViewById(R.id.addCardBtn);
        addCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mParentActivity, SearchShopActivity.class);
                i.putExtra(Global.USER_OBJECT, mUser);
                startActivity(i);
            }
        });

        mListCards = new ArrayList<Card>();

        mPager = (FlippableStackView) mParentActivity.findViewById(R.id.listCardsPager);

        setListData();
    }

    public void refresh() {
        setListData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shops_card_main, container, false);
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

    public void populateList(final ArrayList<Card> listCards, final User user) {

        mParentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mPagerAdapter = new ListCardsPagerAdapter(getChildFragmentManager(), mParentActivity, listCards, false);
                mPagerAdapter.setUser(user);

                mPager.initStack(2, StackPageTransformer.Orientation.VERTICAL, 0.9f, 0.75f, 1f, StackPageTransformer.Gravity.CENTER);
                mPager.setAdapter(mPagerAdapter);

                mDialog.dismiss();
            }
        });
    }



}