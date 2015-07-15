package com.thesis.dont.loyaltypointadmin.controllers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Customer;
import com.thesis.dont.loyaltypointadmin.models.CustomerModel;
import com.thesis.dont.loyaltypointadmin.models.Global;
import com.thesis.dont.loyaltypointadmin.models.History;

import java.util.ArrayList;

public class UserDetailFragment extends Fragment {

    private Customer mCustomer;
    ImageView mCustomerImageView;
    TextView mCustomerNameTextView, mCustomerPhoneTextView, mCustomerAddressTextView, mCustomerPointTextView;
    public static Picasso mPicaso;
    Activity mParentActivity;
    ListView mHistoryList;
    ListHistoriesAdapter mAdapter;
    String shopId;

    ArrayList<History> mHistory;

    public UserDetailFragment() {

    }

    @SuppressLint("ValidFragment")
    public UserDetailFragment(Customer customer, String shopId) {
        mCustomer = customer;
        this.shopId = shopId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_detail, container, false);
        mCustomerImageView = (ImageView) v.findViewById(R.id.user_image);
        mCustomerNameTextView = (TextView) v.findViewById(R.id.customer_name);
        mCustomerAddressTextView = (TextView) v.findViewById(R.id.customer_address);
        mCustomerPhoneTextView = (TextView) v.findViewById(R.id.customer_phone);
        mCustomerPointTextView = (TextView) v.findViewById(R.id.customer_point);

        mCustomerNameTextView.setText(mCustomer.getFullname());
        mCustomerPhoneTextView.setText("Phone number: " + mCustomer.getPhone());
        mCustomerAddressTextView.setText("Address: " + mCustomer.getAddress());
        mCustomerPointTextView.setText("Point: " + String.valueOf(mCustomer.getPoint()));
        mParentActivity = getActivity();
        mPicaso = Picasso.with(mParentActivity);
        if (mCustomer.getAvatar() != null && mCustomer.getAvatar().equals(""))
            mCustomer.setAvatar(null);
        mPicaso.load(mCustomer.getAvatar()).placeholder(R.drawable.ic_user_avatar).into(mCustomerImageView);

        mHistoryList = (ListView) v.findViewById(R.id.history_list);
        mAdapter = new ListHistoriesAdapter(mParentActivity, new ArrayList<History>());
        mHistoryList.setAdapter(mAdapter);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        getListHistory();
    }

    private void getListHistory() {
        CustomerModel.getListHistory(Global.userToken, shopId, mCustomer.getUsername(), new CustomerModel.OnGetListHistoryResult() {
            @Override
            public void onSuccess(ArrayList<History> listHistories) {
                mAdapter.setListHistories(listHistories);
                mParentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onError(String error) {

            }
        });
    }

}
