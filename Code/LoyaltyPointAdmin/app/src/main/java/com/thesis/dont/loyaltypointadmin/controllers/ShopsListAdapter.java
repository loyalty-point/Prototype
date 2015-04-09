package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Global;
import com.thesis.dont.loyaltypointadmin.models.Shop;
import com.thesis.dont.loyaltypointadmin.views.MyRoundedImageView;

import java.util.ArrayList;

/**
 * Created by tinntt on 3/6/2015.
 */
public class ShopsListAdapter extends BaseAdapter {

    public ArrayList<Shop> mShops = new ArrayList<Shop>();

    private Context mParentActivity;

    public static final int SELECT_PHOTO = 100;

    private LayoutInflater mInflater = null;

    String[] menuItems = {"Edit", "Delete", "Update Background"};

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
        public MyRoundedImageView cardImg;
        public RoundedImageView shopLogo;
        public TextView shopName;
        public TextView shopAddress;
        //public at.markushi.ui.CircleButton contextMenuBtn;
        public ImageButton contextMenuBtn;
    }

    //Show view of shop card
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        ViewHolder holder;

        if (convertView == null) {
            view = mInflater.inflate(R.layout.shops_list_row, parent, false);

            // Create holder
            holder = new ViewHolder();
            holder.cardImg = (MyRoundedImageView) view.findViewById(R.id.cardImg);
            holder.shopLogo = (RoundedImageView) view.findViewById(R.id.shopLogo);
            holder.shopName = (TextView) view.findViewById(R.id.shopName);
            holder.shopAddress = (TextView) view.findViewById(R.id.shopAddress);
            //holder.contextMenuBtn = (at.markushi.ui.CircleButton) view.findViewById(R.id.contextMenuBtn);
            holder.contextMenuBtn = (ImageButton) view.findViewById(R.id.contextMenuBtn);

            // save holder
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) convertView.getTag();
        }

        Shop shop = (Shop) getItem(position);

        // Load card image
        Picasso.with(mParentActivity).load(shop.getCardImg()).placeholder(R.drawable.card_img2).into(holder.cardImg);

        // Load shop logo
        Picasso.with(mParentActivity).load(shop.getImage()).placeholder(R.drawable.ic_store).into(holder.shopLogo);

        // Load shop name and shop address
        holder.shopName.setText(shop.getName());
        holder.shopAddress.setText(shop.getAddress());

        // set listener for context menu button
        // init shop menu context
        final ListPopupWindow mMenuContext;
        mMenuContext = new ListPopupWindow(mParentActivity);
        mMenuContext.setAdapter(new ArrayAdapter(mParentActivity,
                R.layout.card_context_menu_item, menuItems));
        mMenuContext.setWidth(150);
        mMenuContext.setHeight(200);
        mMenuContext.setModal(true);
        mMenuContext.setAnchorView(holder.contextMenuBtn);

        final int shopIndex = position;
        mMenuContext.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mMenuContext.dismiss();
                switch (position) {
                    case 0: { // EDIT
                        Intent i = new Intent(mParentActivity, EditShopActivity.class);
                        Shop shop = (Shop) getItem(shopIndex);
                        i.putExtra(Global.SHOP_OBJECT, shop);
                        mParentActivity.startActivity(i);
                        break;
                    }
                    case 1: // DELETE
                        break;
                    case 2: { // CHANGE BACKGROUND
                        Intent i = new Intent(mParentActivity, CropImageActivity.class);
                        i.putExtra(CropImageActivity.ASPECT_RATIO, 1);
                        ((Activity)mParentActivity).startActivityForResult(i, SELECT_PHOTO);
                        break;
                    }
                }
            }
        });

        holder.contextMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMenuContext.show();
            }
        });

        // set listener for shop card
        holder.cardImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Shop shop = (Shop) getItem(shopIndex);
                Intent i = new Intent(mParentActivity, ShopDetailActivity.class);
                i.putExtra(Global.SHOP_OBJECT, shop);
                mParentActivity.startActivity(i);
            }
        });

        return view;
    }

}
