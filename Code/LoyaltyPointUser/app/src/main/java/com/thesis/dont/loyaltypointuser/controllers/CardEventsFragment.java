package com.thesis.dont.loyaltypointuser.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFloat;
import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointuser.R;
import com.thesis.dont.loyaltypointuser.models.CardModel;
import com.thesis.dont.loyaltypointuser.models.Event;

import java.util.ArrayList;

import butterknife.ButterKnife;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.view.CardGridView;

public class CardEventsFragment extends Fragment {
    private static final String ARG_POSITION = "position";
    private static final String ARG_CARDID = "card_id";
    public static final String EVENT_OBJECT = "event_obj";
    public static final String SHOP_ID = "shop_id";

    //    @InjectView(R.id.textView)

    private CardGridView mListView;
    CardGridArrayAdapter mAdapter;
    Activity mParentActivity;

    private int position;
    private String cardId;

    public CardEventsFragment() {
    }

    public CardEventsFragment(int position, String cardId) {
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        b.putString(ARG_CARDID, cardId);
        this.setArguments(b);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
        cardId = getArguments().getString(ARG_CARDID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_card_events, container, false);
        ButterKnife.inject(this, rootView);
        ViewCompat.setElevation(rootView, 50);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mParentActivity = getActivity();
        mAdapter = new CardGridArrayAdapter(getActivity(), new ArrayList<Card>());
        mListView = (CardGridView) getActivity().findViewById(R.id.listEvents);
        mListView.setAdapter(mAdapter);

    }

    public class EventCard extends Card {

        protected TextView eventNameTv, eventDateTv, eventPointTv, eventShopNameTv;
        protected ImageView eventImgIv;
        protected Event event;

        protected String eventName, eventDate, eventPoint, eventImg, eventShopName;

        public EventCard(Context context) {
            super(context, R.layout.card_event_list_row);
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

            eventShopNameTv = (TextView) view.findViewById(R.id.eventShopName);
            eventShopNameTv.setText(eventShopName);
            //eventPointTv.setTextColor(Color.rgb(0, 100, 0));

            eventImgIv = (ImageView) view.findViewById(R.id.eventImg);
            if (eventImg == null || eventImg.equals(""))
                eventImg = null;
            Picasso.with(mParentActivity).load(eventImg).placeholder(R.drawable.ic_about).into(eventImgIv);
        }

    }



    public void getListEvents() {
        CardModel.getListEvents(cardId, new CardModel.OnGetListEventResult() {
            @Override
            public void onSuccess(final ArrayList<Event> listEvents) {
                CardEventsFragment.this.getActivity().runOnUiThread(new Runnable() {
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
                            card.eventShopName = listEvents.get(i).getShopName();
//                            card.setOnClickListener(new Card.OnCardClickListener() {
//                                @Override
//                                public void onClick(Card card, View view) {
//                                    Intent i = new Intent(getActivity(), EditEventActivity.class);
//                                    i.putExtra(EVENT_OBJECT, ((EventCard) card).event);
//                                    i.putExtra(SHOP_ID, cardId);
//                                    startActivity(i);
//                                }
//                            });

                            mAdapter.add(card);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onError(final String error) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Get listEvents không thành công
                        Toast.makeText(CardEventsFragment.this.getActivity(), error, Toast.LENGTH_LONG).show();
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
