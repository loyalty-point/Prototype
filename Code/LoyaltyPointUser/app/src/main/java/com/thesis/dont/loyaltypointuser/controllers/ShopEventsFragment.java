package com.thesis.dont.loyaltypointuser.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFloat;
import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointuser.R;
import com.thesis.dont.loyaltypointuser.models.Award;
import com.thesis.dont.loyaltypointuser.models.AwardModel;
import com.thesis.dont.loyaltypointuser.models.Event;
import com.thesis.dont.loyaltypointuser.models.EventModel;
import com.thesis.dont.loyaltypointuser.models.Global;
import com.thesis.dont.loyaltypointuser.models.Shop;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.view.CardGridView;


public class ShopEventsFragment extends Fragment {
    private static final String ARG_POSITION = "position";
    private static final String ARG_SHOPID = "shop_id";
    public static final String EVENT_OBJECT = "event_obj";
    public static final String SHOP_ID = "shop_id";

    //    @InjectView(R.id.textView)
    ButtonFloat createEventBtn;

    private CardGridView mListView;
    CardGridArrayAdapter mAdapter;

    private int position;
    private String shopId;
    private String cardId;

    Activity mParentActivity;

    public ShopEventsFragment() {
        Bundle b = getArguments();

    }

    public ShopEventsFragment(int position) {
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
        View rootView = inflater.inflate(R.layout.fragment_shop_events, container, false);
        ButterKnife.inject(this, rootView);
        ViewCompat.setElevation(rootView, 50);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mParentActivity = getActivity();
        shopId = ((ShopDetailActivity)getActivity()).getCurrentShop().getId();
        cardId = ((ShopDetailActivity)getActivity()).getCurrentCardId();
        mAdapter = new CardGridArrayAdapter(getActivity(), new ArrayList<Card>());
        mListView = (CardGridView) getActivity().findViewById(R.id.listEvents);
        mListView.setAdapter(mAdapter);
    }

    public void getListEvents() {
        EventModel.getListEvents(Global.userToken, shopId, cardId, new EventModel.OnGetListResult() {

            @Override
            public void onSuccess(final ArrayList<Event> listEvents, final ArrayList<ArrayList<Shop>> listShops) {
                mParentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.clear();
                        for (int i = 0; i < listEvents.size(); i++) {

                            EventCard card = new EventCard(getActivity());

                            //Only for test, use different titles and ratings
                            card.eventName = listEvents.get(i).getName();
                            card.eventDate = listEvents.get(i).getTime_start() + " - " + listEvents.get(i).getTime_end();
                            card.eventPoint = String.valueOf(listEvents.get(i).getPoint()) + " points";
                            card.eventImg = listEvents.get(i).getImage();
                            card.event = listEvents.get(i);
                            card.listShops = listShops.get(i);
                            card.setOnClickListener(new Card.OnCardClickListener() {
                                @Override
                                public void onClick(Card card, View view) {
                                    Event event = ((EventCard)mAdapter.getItem(position)).event;
                                    Intent i = new Intent(getActivity(), EventDetailActivity.class);
                                    i.putExtra(EVENT_OBJECT, ((EventCard) card).event);
                                    i.putParcelableArrayListExtra(Global.SHOP_ARRAY_OBJECT, ((EventCard) card).listShops);
                                    startActivity(i);
                                }
                            });

                            mAdapter.add(card);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onError(final String error) {
                mParentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Get listAwards không thành công
                        Toast.makeText(mParentActivity, error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    public class EventCard extends Card {

        protected TextView eventNameTv, eventDateTv, eventPointTv;
        protected ImageView eventImgIv;
        public Event event;
        public ArrayList<Shop> listShops;

        protected String eventName, eventDate, eventPoint, eventImg;

        public EventCard(Context context) {
            super(context, R.layout.events_list_row);
        }

        public EventCard(Context context, int innerLayout) {
            super(context, innerLayout);
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {

            //Populate the inner elements

            eventNameTv = (TextView) view.findViewById(R.id.eventName);
            eventNameTv.setText(eventName);

            eventDateTv = (TextView) view.findViewById(R.id.eventDate);
            eventDateTv.setText(eventDate);

            eventPointTv = (TextView) view.findViewById(R.id.eventPoint);
            eventPointTv.setText(eventPoint);

            eventImgIv = (ImageView) view.findViewById(R.id.eventImg);
            if (eventImg.equals(""))
                eventImg = "null";
            Picasso.with(mParentActivity).load(eventImg).placeholder(R.drawable.ic_about).into(eventImgIv);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        getListEvents();
    }
}
