package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.AwardHistory;
import com.thesis.dont.loyaltypointadmin.models.Event;
import com.thesis.dont.loyaltypointadmin.models.Global;
import com.thesis.dont.loyaltypointadmin.models.TicketModel;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class UserTicketsFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    private static final String ARG_USERID = "user_id";
    private static final String ARG_SHOPID = "shop_id";

    int position;
    String userId;
    String shopId;

    ListView mListView;
    TicketsListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_user_tickets, container, false);
        ButterKnife.inject(this, rootView);
        ViewCompat.setElevation(rootView, 50);
        return rootView;
    }

    public UserTicketsFragment(int position, String userId, String shopId){
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        b.putString(ARG_USERID, userId);
        b.putString(ARG_SHOPID, shopId);
        this.setArguments(b);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
        userId = getArguments().getString(ARG_USERID);
        shopId = getArguments().getString(ARG_SHOPID);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new TicketsListAdapter(getActivity(), new ArrayList<AwardHistory>());
        mListView = (ListView) getActivity().findViewById(R.id.listTickets);
        mListView.setAdapter(mAdapter);
        // Load dữ liệu lên list
        getListTickets();
    }

    public void getListTickets() {
        TicketModel.getUserTicket(Global.userToken, userId, shopId, new TicketModel.OnGetUserTicket() {
            @Override
            public void onSuccess(ArrayList<AwardHistory> listTickets) {
                mAdapter.setListTickets(listTickets);
                UserTicketsFragment.this.getActivity().runOnUiThread(new Runnable() {
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
                        Toast.makeText(UserTicketsFragment.this.getActivity(), error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
