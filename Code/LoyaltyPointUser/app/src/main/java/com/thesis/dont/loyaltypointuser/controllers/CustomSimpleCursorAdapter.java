package com.thesis.dont.loyaltypointuser.controllers;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.thesis.dont.loyaltypointuser.R;
import com.thesis.dont.loyaltypointuser.models.Global;
import com.thesis.dont.loyaltypointuser.models.ShopModel;

/**
 * Created by tinntt on 3/31/2015.
 */
public class CustomSimpleCursorAdapter extends SimpleCursorAdapter {
    private Context context;

    public CustomSimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.context = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.suggestion_list, parent, false);


        return view;
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
//        String name = cursor.getString(cursor.getColumnIndex(SearchShopActivity.SHOP_NAME));
//        String address = cursor.getString(cursor.getColumnIndex(SearchShopActivity.SHOP_ADDRESS));
//        String image = cursor.getString(cursor.getColumnIndex(SearchShopActivity.SHOP_IMAGE));
//        final String id = cursor.getString(cursor.getColumnIndex(SearchShopActivity.SHOP_ID));
//
//        TextView shopName = (TextView) view.findViewById(R.id.shopName);
//        TextView shopAddress = (TextView) view.findViewById(R.id.shopAddress);
//        ImageView shopImage = (ImageView) view.findViewById(R.id.shopImg);
//
//        shopName.setText(name);
//        shopAddress.setText(address);
//        SearchShopActivity.mPicaso.load(image).placeholder(R.drawable.ic_store).into(shopImage);
//
//        Button yourButton = (Button) view.findViewById(R.id.addFollowShop);
//        yourButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ShopModel.followShop(Global.userToken,id,new ShopModel.OnFollowShopResult() {
//                    @Override
//                    public void onSuccess() {
//                        Log.e("result", "success");
//
//                    }
//
//                    @Override
//                    public void onError(String error) {
//                        Log.e("error", error);
//                    }
//                });
//            }
//        });


    }
}
