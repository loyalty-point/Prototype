package com.thesis.dont.loyaltypointuser.controllers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointuser.R;
import com.thesis.dont.loyaltypointuser.models.Award;
import com.thesis.dont.loyaltypointuser.models.Event;
import com.thesis.dont.loyaltypointuser.models.Global;
import com.thesis.dont.loyaltypointuser.models.Shop;
import com.thesis.dont.loyaltypointuser.models.ShopModel;
import com.thesis.dont.loyaltypointuser.models.User;


import java.util.ArrayList;

import butterknife.ButterKnife;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.view.CardGridView;

public class ShopDetailFragment extends Fragment {
    private static final String ARG_POSITION = "position";
    private static final String ARG_SHOPID = "shopId";

    private int position;
    private String shopId, cardId;
    View rootView;

    private CardGridView mListViewNewestUser;
    CardGridArrayAdapter mAdapterNewestUser;
    private CardGridView mListViewNewestEvent;
    CardGridArrayAdapter mAdapterNewestEvent;
    private CardGridView mListViewNewestAward;
    CardGridArrayAdapter mAdapterNewestAward;

    Activity mParentActivity;

    public ShopDetailFragment() {
    }

    public ShopDetailFragment(int position) {
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        this.setArguments(b);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
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

        mParentActivity = getActivity();
        cardId = ((ShopDetailActivity)mParentActivity).getCurrentCardId();
        shopId = ((ShopDetailActivity)mParentActivity).getCurrentShop().getId();
        final TextView shopName = (TextView) mParentActivity.findViewById(R.id.shopName);
        final TextView shopAddress = (TextView) mParentActivity.findViewById(R.id.shopAddress);
//        final ImageView shopBackground = (ImageView) mParentActivity.findViewById(R.id.shopBackground);
        final ImageView shopImage = (ImageView) mParentActivity.findViewById(R.id.shopImg);
        final TextView userNumber = (TextView) mParentActivity.findViewById(R.id.userNumber);
        final TextView eventNumber = (TextView) mParentActivity.findViewById(R.id.eventNumber);
        final TextView awardNumber = (TextView) mParentActivity.findViewById(R.id.awardNumber);
        //list newest user
        mAdapterNewestUser = new CardGridArrayAdapter(mParentActivity, new ArrayList<Card>());
        mListViewNewestUser = (CardGridView) mParentActivity.findViewById(R.id.listNewUsers);
        mListViewNewestUser.setAdapter(mAdapterNewestUser);
        //list newest event
        mAdapterNewestEvent = new CardGridArrayAdapter(mParentActivity, new ArrayList<Card>());
        mListViewNewestEvent = (CardGridView) mParentActivity.findViewById(R.id.listNewEvents);
        mListViewNewestEvent.setAdapter(mAdapterNewestEvent);
        //list newest award
        mAdapterNewestAward = new CardGridArrayAdapter(mParentActivity, new ArrayList<Card>());
        mListViewNewestAward = (CardGridView) mParentActivity.findViewById(R.id.listNewAwards);
        mListViewNewestAward.setAdapter(mAdapterNewestAward);

        ShopModel.getShopInfo(Global.userToken, this.shopId, cardId, new ShopModel.OnGetShopInfoResult() {
            @Override
            public void onSuccess(final Shop shop) {
                ShopModel.getNumUserEventAward(Global.userToken, shop.getId(), cardId, new ShopModel.OnGetNumUserAwardEventResult() {
                    @Override
                    public void onSuccess(final ShopModel.GetNumUserAwardEventResult mGetNumUserAwardEventResult) {
                        ShopModel.getNewestUserEventAward(Global.userToken, shop.getId(), cardId, new ShopModel.OnGetNewestUserAwardEventResult() {
                            @Override
                            public void onSuccess(final ArrayList<User> listUsers, final ArrayList<Event> listEvents, final ArrayList<Award> listAwards) {
                                mParentActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        shopName.setText(shop.getName());
                                        shopAddress.setText(shop.getAddress());
                                        if (shop.getCardImg().equals(""))
                                            shop.setCardImg(null);
                                        if (shop.getImage().equals(""))
                                            shop.setImage(null);
                                        Picasso.with(mParentActivity).load(shop.getImage()).placeholder(R.drawable.ic_store).into(shopImage);
                                        userNumber.setText(mGetNumUserAwardEventResult.user);
                                        eventNumber.setText(mGetNumUserAwardEventResult.event);
                                        awardNumber.setText(mGetNumUserAwardEventResult.award);

                                        //load layout for newest user list
                                        mAdapterNewestUser.clear();
                                        for (int i = 0; i < listUsers.size(); i++) {
                                            EventCard card = new EventCard(mParentActivity);

                                            card.name = listUsers.get(i).getFullname();
                                            card.detail1 = listUsers.get(i).getUsername();
                                            card.detail2 = listUsers.get(i).getPhone();
                                            card.image = listUsers.get(i).getAvatar();

                                            mAdapterNewestUser.add(card);
                                        }
                                        //load layout for newest event list
                                        mAdapterNewestEvent.clear();
                                        for (int i = 0; i < listEvents.size(); i++) {
                                            EventCard card = new EventCard(mParentActivity);

                                            card.name = listEvents.get(i).getName();
                                            card.detail1 = listEvents.get(i).getTime_start() + " - " + listEvents.get(i).getTime_end();
                                            card.detail2 = String.valueOf(listEvents.get(i).getPoint()) + " points";
                                            card.image = listEvents.get(i).getImage();

                                            mAdapterNewestEvent.add(card);
                                        }
                                        //load layout for newest award list
                                        mAdapterNewestAward.clear();
                                        for (int i = 0; i < listAwards.size(); i++) {
                                            EventCard card = new EventCard(mParentActivity);

                                            card.name = listAwards.get(i).getName();
                                            card.detail1 = "Quantity: " + String.valueOf(listAwards.get(i).getQuantity());
                                            card.detail2 = String.valueOf(listAwards.get(i).getPoint()) + " points";
                                            card.image = listAwards.get(i).getImage();

                                            mAdapterNewestAward.add(card);
                                        }
                                        mAdapterNewestAward.notifyDataSetChanged();
                                        mAdapterNewestUser.notifyDataSetChanged();
                                        mAdapterNewestUser.notifyDataSetChanged();
                                    }
                                });
                            }

                            @Override
                            public void onError(String error) {

                            }
                        });
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            }

            @Override
            public void onError(final String error) {
                mParentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mParentActivity, error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    //card for list grid newest user, event, award
    public class EventCard extends Card {

        protected TextView nameTv, detail1Tv, detail2Tv;
        protected ImageView imageIv;
        protected Object data;

        protected String name, detail1, detail2, image;

        public EventCard(Context context) {
            super(context, R.layout.shop_detail_list_row);
        }

        public EventCard(Context context, int innerLayout) {
            super(context, innerLayout);
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {

            //Populate the inner elements

            nameTv = (TextView) view.findViewById(R.id.nameTv);
            nameTv.setText(name);

            detail1Tv = (TextView) view.findViewById(R.id.detail1Tv);
            detail1Tv.setText(detail1);

            detail2Tv = (TextView) view.findViewById(R.id.detail2Tv);
            detail2Tv.setText(detail2);
            //eventPointTv.setTextColor(Color.rgb(0, 100, 0));

            imageIv = (ImageView) view.findViewById(R.id.imageIv);
            if (image == null || image.equals(""))
                image = null;
            Picasso.with(mParentActivity).load(image).placeholder(R.drawable.ic_about).into(imageIv);
        }
    }

    public void getNewestUserEventAward() {

    }

    @Override
    public void onResume() {
        super.onResume();
        getNewestUserEventAward();
    }

}
