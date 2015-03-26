package com.thesis.dont.loyaltypointadmin.controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Award;
import com.thesis.dont.loyaltypointadmin.models.Shop;

import java.util.ArrayList;

/**
 * Created by tinntt on 3/6/2015.
 */
public class ShopsListAdapter extends BaseAdapter {

    public ArrayList<Shop> mShops = new ArrayList<Shop>();

    private Context mParentActivity;

    private LayoutInflater mInflater = null;

    Shop tempValues = null;
    int i = 0;

    public ShopsListAdapter(Context context, ArrayList<Shop> shopsList) {

        mParentActivity = context;
        mShops = shopsList;

        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return mShops.size();
    }

    public void setListShops(ArrayList<Shop> listShops) {
        mShops = listShops;
    }

    public Object getItem(int position) {
        return mShops.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    //view holder contain data of shop card
    public static class ViewHolder {
        public ImageView cardImg;
        public ImageView shopLogo;
        public TextView shopName;
        public TextView shopAddress;
    }

    //Show view of shop card
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        ViewHolder holder;

        if(convertView == null) {
            view = mInflater.inflate(R.layout.shops_list_row, parent, false);

            // Create holder
            holder = new ViewHolder();
            holder.cardImg = (ImageView) view.findViewById(R.id.cardImg);
            holder.shopLogo = (ImageView) view.findViewById(R.id.shopLogo);
            holder.shopName = (TextView) view.findViewById(R.id.shopName);
            holder.shopAddress = (TextView) view.findViewById(R.id.shopAddress);

            // save holder
            view.setTag(holder);
        }else {
            view = convertView;
            holder = (ViewHolder) convertView.getTag();
        }

        Shop shop = (Shop) getItem(position);

        // Load card image
        Picasso.with(mParentActivity).load(shop.getCardImg()).placeholder(R.drawable.card_img).into(holder.cardImg);

        // Load shop logo
        Picasso.with(mParentActivity).load(shop.getImage()).placeholder(R.drawable.ic_store).into(holder.shopLogo);

        // Load shop name and shop address
        holder.shopName.setText(shop.getName());
        holder.shopAddress.setText(shop.getAddress());

        return view;
    }

}
