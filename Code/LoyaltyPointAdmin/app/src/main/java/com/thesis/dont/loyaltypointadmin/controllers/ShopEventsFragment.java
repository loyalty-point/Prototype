package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFloat;
import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Award;
import com.thesis.dont.loyaltypointadmin.models.AwardModel;
import com.thesis.dont.loyaltypointadmin.models.Event;
import com.thesis.dont.loyaltypointadmin.models.EventModel;
import com.thesis.dont.loyaltypointadmin.models.Global;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
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

    public ShopEventsFragment() {
    }

    public ShopEventsFragment(int position, String shopId) {
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        b.putString(ARG_SHOPID, shopId);
        this.setArguments(b);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
        shopId = getArguments().getString(ARG_SHOPID);
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
        createEventBtn = (ButtonFloat) getActivity().findViewById(R.id.createEventBtn);
        createEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CreateEventActivity.class);
                Bundle b = new Bundle();
                i.putExtra(ARG_SHOPID, shopId);
                startActivity(i);
            }
        });
        mParentActivity = getActivity();
        mAdapter = new CardGridArrayAdapter(getActivity(), new ArrayList<Card>());
        mListView = (CardGridView) getActivity().findViewById(R.id.listEvents);
        mListView.setAdapter(mAdapter);
        getListEvents();

//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Event event = (Event) mAdapter.getItem(position);
//                Intent i = new Intent(getActivity(), EditEventActivity.class);
//                i.putExtra(EVENT_OBJECT, event);
//                i.putExtra(SHOP_ID, shopId);
//                startActivity(i);
//            }
//        });
    }

    /**
     * A card as Google Play
     */
    public class GplayGridCard extends Card {

        protected TextView eventNameTv, eventDateTv, eventPointTv;
        protected ImageView eventImgIv;
        protected Button buyBtn;
        protected int resourceIdThumbnail = -1;

        protected String eventName, eventDate, eventPoint, eventImg;

        public GplayGridCard(Context context) {
            super(context, R.layout.card_list_grid_inner_content);
        }

        public GplayGridCard(Context context, int innerLayout) {
            super(context, innerLayout);
        }

        private void init() {

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

            eventImgIv = (ImageView) view.findViewById(R.id.eventImage);
            if (eventImg.equals(""))
                eventImg = "null";
            Picasso.with(mParentActivity).load(eventImg).placeholder(R.drawable.ic_about).into(eventImgIv);
        }

        /**
         * CardThumbnail
         */
        class GplayGridThumb extends CardThumbnail {

            public GplayGridThumb(Context context) {
                super(context);
            }

            @Override
            public void setupInnerViewElements(ViewGroup parent, View viewImage) {
                /*
                viewImage.getLayoutParams().width = 196;
                viewImage.getLayoutParams().height = 196;
                */

            }
        }

    }

    public void getListEvents() {
        EventModel.getListEvents(shopId, new EventModel.OnGetListResult() {

            @Override
            public void onSuccess(final ArrayList<Event> listEvents) {
                ShopEventsFragment.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < listEvents.size(); i++) {

                            GplayGridCard card = new GplayGridCard(getActivity());

                            //Only for test, use different titles and ratings
                            card.eventName = listEvents.get(i).getName();
                            card.eventDate = listEvents.get(i).getTime_start() + " - " + listEvents.get(i).getTime_end();
                            card.eventPoint = "Points: " + String.valueOf(listEvents.get(i).getPoint());
                            card.eventImg = listEvents.get(i).getImage();

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
                        // Get listAwards không thành công
                        Toast.makeText(ShopEventsFragment.this.getActivity(), error, Toast.LENGTH_LONG).show();
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
