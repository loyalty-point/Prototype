package com.thesis.dont.loyaltypointadmin.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFloat;
import com.thesis.dont.loyaltypointadmin.R;

import butterknife.ButterKnife;


public class ShopEventsFragment extends Fragment {
    private static final String ARG_POSITION = "position";
    private static final String ARG_SHOPID = "shop_id";

    //    @InjectView(R.id.textView)
    ButtonFloat createEventBtn;

    private int position;
    private String shopId;

    public ShopEventsFragment(int position, String shopId){
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
        View rootView = inflater.inflate(R.layout.fragment_shop_events,container,false);
        ButterKnife.inject(this, rootView);
        ViewCompat.setElevation(rootView, 50);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        createEventBtn = (ButtonFloat)getActivity().findViewById(R.id.createEventBtn);
        createEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CreateEventActivity.class);
                Bundle b = new Bundle();
                i.putExtra(ARG_SHOPID, shopId);
                startActivity(i);
            }
        });
    }

}
