package com.thesis.dont.loyaltypointadmin.controllers;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFloat;
import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Event;
import com.thesis.dont.loyaltypointadmin.models.EventModel;
import com.thesis.dont.loyaltypointadmin.models.Global;

import java.util.ArrayList;

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
    Activity mParentActivity;

    private int position;
    private String shopId;
    private String cardId;

    public ShopEventsFragment() {
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

        cardId = ((ShopDetailActivity) mParentActivity).getCurrentCardId();
        shopId = ((ShopDetailActivity)mParentActivity).getCurrentShop().getId();
        createEventBtn = (ButtonFloat) mParentActivity.findViewById(R.id.createEventBtn);
        createEventBtn.setBackgroundColor(getResources().getColor(R.color.AccentColor));
        createEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mParentActivity, CreateEventActivity.class);
                Bundle b = new Bundle();
                i.putExtra(ARG_SHOPID, shopId);
                i.putExtra(Global.CARD_ID, cardId);
                startActivity(i);
            }
        });

        mAdapter = new CardGridArrayAdapter(mParentActivity, new ArrayList<Card>());
        mListView = (CardGridView) mParentActivity.findViewById(R.id.listEvents);
        mListView.setAdapter(mAdapter);

    }

    public class EventCard extends Card {

        protected TextView eventNameTv, eventDateTv, eventPointTv;
        protected ImageView eventImgIv;
        protected Event event;

        protected String eventName, eventDate, eventPoint, eventImg;

        public EventCard(Context context) {
            super(context, R.layout.event_list_row);
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
            //eventPointTv.setTextColor(Color.rgb(0, 100, 0));

            eventImgIv = (ImageView) view.findViewById(R.id.eventImg);
            if (eventImg == null || eventImg.equals(""))
                eventImg = null;
            Picasso.with(mParentActivity).load(eventImg).placeholder(R.drawable.ic_about).into(eventImgIv);
        }

    }



    public void getListEvents() {
        EventModel.getListEvents(shopId, cardId, new EventModel.OnGetListResult() {

            @Override
            public void onSuccess(final ArrayList<Event> listEvents) {
                mParentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.clear();
                        for (int i = 0; i < listEvents.size(); i++) {

                            EventCard card = new EventCard(mParentActivity);

                            //Only for test, use different titles and ratings
                            card.eventName = listEvents.get(i).getName();
                            card.eventDate = listEvents.get(i).getTime_start() + " - " + listEvents.get(i).getTime_end();
                            card.eventPoint = String.valueOf(listEvents.get(i).getPoint()) + " points";
                            card.eventImg = listEvents.get(i).getImage();
                            card.event = listEvents.get(i);
                            card.setOnClickListener(new Card.OnCardClickListener() {
                                @Override
                                public void onClick(Card card, View view) {
                                    Intent i = new Intent(mParentActivity, EditEventActivity.class);
                                    i.putExtra(EVENT_OBJECT, ((EventCard)card).event);
                                    i.putExtra(Global.CARD_ID, cardId);
                                    i.putExtra(SHOP_ID, shopId);
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

    @Override
    public void onResume() {
        super.onResume();
        getListEvents();
    }
}
