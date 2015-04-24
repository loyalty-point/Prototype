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
import com.thesis.dont.loyaltypointuser.models.Global;

import java.util.ArrayList;

import butterknife.ButterKnife;


public class ShopAwardsFragment extends Fragment {
    private static final String ARG_POSITION = "position";
    public static final String SHOP_ID = "shop_ID";
    public static final String AWARD_OBJECT = "award_object";
    public static final String USER_POINT = "user_point";

    //    @InjectView(R.id.textView)
    TextView textView;

    ListView mListView;
    AwardsListAdapter mAdapter;

    private int position;

    String shopID;
    private int userPoint;

    public ShopAwardsFragment() {}

    public ShopAwardsFragment(int position, String shopId, int userPoint){
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        b.putInt(USER_POINT, userPoint);
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
        userPoint = getArguments().getInt(USER_POINT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shop_awards,container,false);
        ButterKnife.inject(this, rootView);
        ViewCompat.setElevation(rootView, 50);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        getListAwards();
    }

    public void refresh() {
        getListAwards();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        shopID = ((ShopDetailActivity)getActivity()).shop.getId();

        // Lấy danh sách awards của shop về
        // Tạo và set adapter cho listview
        mAdapter = new AwardsListAdapter(getActivity(), this, new ArrayList<Award>(), userPoint);
        mListView = (ListView) getActivity().findViewById(R.id.listAwards);
        mListView.setAdapter(mAdapter);

        // set listener for Item Click
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // start Details Award Activity
                /*Award award = (Award) mAdapter.getItem(position);
                Intent i = new Intent(getActivity(), EditAwardActivity.class);
                //i.putExtra(SHOP_ID, shopID);
                i.putExtra(AWARD_OBJECT, award);
                startActivity(i);*/
            }
        });
    }

    public void getListAwards() {
        AwardModel.getListAwards(Global.userToken, shopID, new AwardModel.OnGetListAwardsResult() {
            @Override
            public void onSuccess(ArrayList<Award> listAwards) {
                // Get listAwards thành công
                // Cập nhật dữ liệu lên mAdapter
                mAdapter.setListAwards(listAwards);
                ShopAwardsFragment.this.getActivity().runOnUiThread(new Runnable() {
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
                        Toast.makeText(ShopAwardsFragment.this.getActivity(), error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

}
