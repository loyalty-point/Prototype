package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

public class UserInfoActivity extends ActionBarActivity {

    private Customer mCustomer;
    String cardId;
    ImageView mCustomerImageView;
    TextView mCustomerNameTextView, mCustomerPhoneTextView, mCustomerAddressTextView, mCustomerPointTextView;
    public static Picasso mPicaso;
    ListView mHistoryList;
    ListHistoriesAdapter mAdapter;
    String shopId;

    ArrayList<History> mHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        Intent i = getIntent();
        mCustomer = i.getParcelableExtra(Global.USER_OBJECT);
        cardId = i.getStringExtra(Global.CARD_ID);

        mCustomerImageView = (ImageView) findViewById(R.id.user_image);
        mCustomerNameTextView = (TextView) findViewById(R.id.customer_name);
        mCustomerAddressTextView = (TextView) findViewById(R.id.customer_address);
        mCustomerPhoneTextView = (TextView) findViewById(R.id.customer_phone);
        mCustomerPointTextView = (TextView) findViewById(R.id.customer_point);

        mCustomerNameTextView.setText(mCustomer.getFullname());
        mCustomerPhoneTextView.setText("Phone number: " + mCustomer.getPhone());
        mCustomerAddressTextView.setText("Address: " + mCustomer.getAddress());
        mCustomerPointTextView.setText("Point: " + mCustomer.getPoint());

        mPicaso = Picasso.with(this);
        if (mCustomer.getAvatar().equals(""))
            mCustomer.setAvatar(null);
        mPicaso.load(mCustomer.getAvatar()).placeholder(R.drawable.ic_user_avatar).into(mCustomerImageView);

        mHistoryList = (ListView) findViewById(R.id.history_list);
        mAdapter = new ListHistoriesAdapter(this, new ArrayList<History>());
        mHistoryList.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getListHistory();
    }

    private void getListHistory(){
        CustomerModel.getListHistoryCard(Global.userToken, cardId, mCustomer.getUsername(), new CustomerModel.OnGetListHistoryResult() {
            @Override
            public void onSuccess(ArrayList<History> listHistories) {
                mAdapter.setListHistories(listHistories);
                runOnUiThread(new Runnable() {
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
