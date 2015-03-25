package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFloat;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Award;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


public class ShopAwardsFragment extends Fragment {
    private static final String ARG_POSITION = "position";
    public static final String SHOP_ID = "shop_ID";

    //    @InjectView(R.id.textView)
    TextView textView;

    ListView mListView;
    AwardsListAdapter mAdapter;

    private int position;

    public ShopAwardsFragment(int position, String shopId){
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        this.setArguments(b);
    }

//    public static ShopDetailFragment newInstance(int position) {
//        ShopDetailFragment f = new ShopDetailFragment();
//        Bundle b = new Bundle();
//        b.putInt(ARG_POSITION, position);
//        f.setArguments(b);
//        return f;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shop_awards,container,false);
        ButterKnife.inject(this, rootView);
        ViewCompat.setElevation(rootView, 50);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*textView = (TextView)getActivity().findViewById(R.id.textView2);
        textView.setText("CARD " + position);*/

        ButtonFloat createAwardBtn = (ButtonFloat) getActivity().findViewById(R.id.createAwardBtn);
        createAwardBtn.setBackgroundColor(getResources().getColor(R.color.AccentColor));
        createAwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CreateAwardActivity.class);
                i.putExtra(SHOP_ID, ((ShopDetailActivity)getActivity()).shopID);
                startActivity(i);
            }
        });

        // Lấy danh sách awards của shop về
        // Tạo và set adapter cho listview
        List<Award> list = new ArrayList<Award>();
        list.add(new Award("award 1", 100, 500, null, "http://award.image", null));
        mAdapter = new AwardsListAdapter(getActivity(), list);
        mListView = (ListView) getActivity().findViewById(R.id.listAwards);
        mListView.setAdapter(mAdapter);
    }

}
