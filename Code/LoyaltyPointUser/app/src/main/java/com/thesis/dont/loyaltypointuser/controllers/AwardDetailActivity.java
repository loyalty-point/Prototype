package com.thesis.dont.loyaltypointuser.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointuser.R;
import com.thesis.dont.loyaltypointuser.models.Award;
import com.thesis.dont.loyaltypointuser.models.Global;
import com.thesis.dont.loyaltypointuser.models.Shop;

import java.util.ArrayList;

public class AwardDetailActivity extends ActionBarActivity {

    TextView awardName, awardNumber, awardDescription;
    ImageView awardImage;
    ListView listShop;

    public static Picasso mPicaso;
    Award award;
    ArrayList<Shop> listShops;
    ListShopsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_award_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        award = (Award) i.getParcelableExtra(ShopAwardsFragment.AWARD_OBJECT);
        listShops = i.getParcelableArrayListExtra(Global.SHOP_ARRAY_OBJECT);

        mPicaso = Picasso.with(this);
        awardName = (TextView) findViewById(R.id.awardName);
        awardNumber = (TextView) findViewById(R.id.awardNumber);
        awardDescription = (TextView) findViewById(R.id.awardDescription);
        awardImage = (ImageView) findViewById(R.id.awardImage);
        listShop = (ListView) findViewById(R.id.listShop);

        awardName.setText(award.getName());
        awardNumber.setText("Quantity: " + award.getQuantity());
        awardDescription.setText("Description: " + award.getDescription());

        mAdapter = new ListShopsAdapter(this, listShops);
        listShop.setAdapter(mAdapter);
        mPicaso.load(award.getImage()).placeholder(R.drawable.ic_award).into(awardImage);

    }

    public class ListShopsAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private ArrayList<Shop> mShops = new ArrayList<Shop>();

        private Activity mParentActivity;

        public ListShopsAdapter(Context context, ArrayList<Shop> Shops) {
            mInflater = LayoutInflater.from(context);
            this.mShops = Shops;
            mParentActivity = (Activity) context;
        }

        public ArrayList<Shop> getListShops() {
            return mShops;
        }

        public void setListShops(ArrayList<Shop> listShops) {
            mShops = listShops;
        }

        @Override
        public int getCount() {
            return mShops.size();
        }

        @Override
        public Object getItem(int position) {
            return mShops.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder holder;

            if (convertView == null) {
                view = mInflater.inflate(R.layout.list_shop_award_detail, parent, false);

                // Create holder
                holder = new ViewHolder();
                holder.shopNameTv = (TextView) view.findViewById(R.id.shopName);
                holder.shopAddressTv = (TextView) view.findViewById(R.id.shopAddress);
                holder.shopImage = (ImageView) view.findViewById(R.id.shopImg);

                // save holder
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder) convertView.getTag();
            }

            Shop shop = (Shop) getItem(position);
            holder.shopNameTv.setText(shop.getName());
            holder.shopAddressTv.setText(shop.getAddress());
            String image = shop.getImage();
            if (image.equals(""))
                image = null;
            mPicaso.load(image).placeholder(R.drawable.ic_store).into(holder.shopImage);
            return view;
        }

        public void add(Shop shop) {
            mShops.add(shop);
        }

        public class ViewHolder {
            public TextView shopNameTv;
            public TextView shopAddressTv;
            public ImageView shopImage;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
