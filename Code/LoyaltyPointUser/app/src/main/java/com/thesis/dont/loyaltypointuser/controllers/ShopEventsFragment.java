package com.thesis.dont.loyaltypointuser.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFloat;
import com.thesis.dont.loyaltypointuser.R;
import com.thesis.dont.loyaltypointuser.models.Award;
import com.thesis.dont.loyaltypointuser.models.AwardModel;
import com.thesis.dont.loyaltypointuser.models.Event;
import com.thesis.dont.loyaltypointuser.models.EventModel;
import com.thesis.dont.loyaltypointuser.models.Global;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


public class ShopEventsFragment extends Fragment {
    private static final String ARG_POSITION = "position";
    private static final String ARG_SHOPID = "shop_id";
    public static final String EVENT_OBJECT = "event_obj";
    public static final String SHOP_ID = "shop_id";

    //    @InjectView(R.id.textView)
    ButtonFloat createEventBtn;

    ListView mListView;
    EventsListAdapter mAdapter;

    private int position;
    private String shopId;

    public ShopEventsFragment() {
        Bundle b = getArguments();

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

    public void getListEvents() {
        EventModel.getListEvents(shopId, new EventModel.OnGetListResult() {

            @Override
            public void onSuccess(ArrayList<Event> listEvents) {
                mAdapter.setListEvents(listEvents);
                ShopEventsFragment.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
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
        getListEvents();
    }
}
