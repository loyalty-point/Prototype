package com.thesis.dont.loyaltypointadmin.controllers;

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
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Award;
import com.thesis.dont.loyaltypointadmin.models.AwardModel;
import com.thesis.dont.loyaltypointadmin.models.Event;
import com.thesis.dont.loyaltypointadmin.models.EventModel;
import com.thesis.dont.loyaltypointadmin.models.Global;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


public class ShopEventsFragment extends Fragment {
    private static final String ARG_POSITION = "position";
    private static final String ARG_SHOPID = "shop_id";
    private static final String EVENT_OBJECT = "event_obj";

    //    @InjectView(R.id.textView)
    ButtonFloat createEventBtn;

    ListView mListView;
    EventsListAdapter mAdapter;

    private int position;
    private String shopId;

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

        mAdapter = new EventsListAdapter(getActivity(), new ArrayList<Event>());
        mListView = (ListView) getActivity().findViewById(R.id.listEvents);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event event = (Event) mAdapter.getItem(position);
                Intent i = new Intent(getActivity(), EditEventActivity.class);
                i.putExtra(EVENT_OBJECT, event);
                startActivity(i);
            }
        });

        // Load dữ liệu lên list
        getListEvents();
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
