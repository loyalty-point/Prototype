package com.thesis.dont.loyaltypointuser.controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointuser.R;
import com.thesis.dont.loyaltypointuser.models.Award;
import com.thesis.dont.loyaltypointuser.models.AwardHistory;
import com.thesis.dont.loyaltypointuser.models.AwardModel;
import com.thesis.dont.loyaltypointuser.models.Global;
import com.thesis.dont.loyaltypointuser.models.User;
import com.thesis.dont.loyaltypointuser.models.UserModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.view.CardGridView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AccountFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyAwardsFragment extends Fragment {

    private CardGridView mListView;
    CardGridArrayAdapter mAdapter;

    Activity mParentActivity;

    ProgressDialog mDialog;

    public MyAwardsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onResume() {
        super.onResume();

        getListAwards();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_awards, container, false);
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

        // init list histories
        mAdapter = new CardGridArrayAdapter(mParentActivity, new ArrayList<Card>());
        mListView = (CardGridView) mParentActivity.findViewById(R.id.listAwards);
        mListView.setAdapter(mAdapter);
    }

    public void refresh() {
        getListAwards();
    }

    public void getListAwards() {
        mDialog.show();
        UserModel.getMyAwards(Global.userToken, new UserModel.OnGetMyAwardsResult() {
            @Override
            public void onSuccess(final ArrayList<AwardHistory> awards) {
                mParentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.clear();
                        for (int i = 0; i < awards.size(); i++) {

                            AwardCard card = new AwardCard(mParentActivity);

                            //Only for test, use different titles and ratings
                            card.awardName = awards.get(i).getAwardName();
                            card.awardQuantity = "Quantity: " + String.valueOf(awards.get(i).getQuantity());
                            card.awardShopName = String.valueOf(awards.get(i).getShopName());
                            card.awardImg = awards.get(i).getAwardImage();
                            card.award = awards.get(i);

                            mAdapter.add(card);
                        }
                        mAdapter.notifyDataSetChanged();
                        mDialog.dismiss();
                    }
                });
            }

            @Override
            public void onError(final String error) {
                mParentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Get listAwards không thành công
                        mDialog.dismiss();
                        Toast.makeText(mParentActivity, error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    public class AwardCard extends Card {

        protected TextView awardNameTv, awardQuantityTv, awardShopNameTv;
        protected ImageView awardImgIv;
        protected AwardHistory award;

        protected String awardName, awardQuantity, awardShopName, awardImg;

        public AwardCard(Context context) {
            super(context, R.layout.my_awards_list_row);
        }

        public AwardCard(Context context, int innerLayout) {
            super(context, innerLayout);
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {

            //Populate the inner elements

            awardNameTv = (TextView) view.findViewById(R.id.awardName);
            awardNameTv.setText(awardName);

            awardQuantityTv = (TextView) view.findViewById(R.id.awardQuantity);
            awardQuantityTv.setText(awardQuantity);


            awardShopNameTv = (TextView) view.findViewById(R.id.awardShopName);
            awardShopNameTv.setText(awardShopName);


            awardImgIv = (ImageView) view.findViewById(R.id.awardImg);
            if (awardImg.equals(""))
                awardImg = "null";
            Picasso.with(mParentActivity).load(awardImg).placeholder(R.drawable.ic_about).into(awardImgIv);

        }
    }
}
