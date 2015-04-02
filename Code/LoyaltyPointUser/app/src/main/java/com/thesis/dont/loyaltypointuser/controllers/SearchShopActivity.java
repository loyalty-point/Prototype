package com.thesis.dont.loyaltypointuser.controllers;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.provider.BaseColumns;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointuser.R;
import com.thesis.dont.loyaltypointuser.models.Global;
import com.thesis.dont.loyaltypointuser.models.Shop;
import com.thesis.dont.loyaltypointuser.models.ShopModel;

import java.util.ArrayList;

public class SearchShopActivity extends ActionBarActivity implements CustomSimpleCursorAdapter.ViewBinder {
    public static final String SHOP_NAME = "shopName";
    public static final String SHOP_ADDRESS = "shopAddress";
    public static final String SHOP_IMAGE = "shopImg";
    public static final String SHOP_ID = "shopId";

    private ArrayList<Shop> listShop;
    private ListView listView;
    private CustomSimpleCursorAdapter mAdapter;

    MatrixCursor cursor;

    public static Picasso mPicaso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_shop);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPicaso = Picasso.with(this);
        final String[] from = new String[]{SHOP_IMAGE, SHOP_NAME, SHOP_ADDRESS, SHOP_ID};
        final int[] to = new int[]{R.id.shopImg, R.id.shopName, R.id.shopAddress};
        cursor = new MatrixCursor(new String[]{BaseColumns._ID, SHOP_IMAGE, SHOP_NAME, SHOP_ADDRESS, SHOP_ID});
        mAdapter = new CustomSimpleCursorAdapter(this,
                R.layout.suggestion_list,
                cursor,
                from,
                to, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        listView = (ListView) findViewById(R.id.shopsList);
        listView.setItemsCanFocus(false);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                parent.findViewById(R.id.addFollowShop).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("click", String.valueOf(position));
                    }
                });
                view.findViewById(R.id.addFollowShop).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("click", String.valueOf(position));
                    }
                });
            }
        });

        listView.setAdapter(mAdapter);
        getListShops();
    }

    public void getListShops() {
        ShopModel.getAllShop(Global.userToken, new ShopModel.OnSelectAllShopResult() {
            @Override
            public void onSuccess(ArrayList<Shop> listShops) {
                SearchShopActivity.this.listShop = listShops;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        populateAdapter("");
                    }
                });
            }

            @Override
            public void onError(final String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SearchShopActivity.this, "error: " + error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search_shop, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // You must implements your logic to get data using OrmLite
    private void populateAdapter(String query) {
        cursor = new MatrixCursor(new String[]{BaseColumns._ID, SHOP_IMAGE, SHOP_NAME, SHOP_ADDRESS, SHOP_ID});
        for (int i = 0; i < listShop.size(); i++) {
            if (listShop.get(i).getName().toLowerCase().startsWith(query.toLowerCase()))
                cursor.addRow(new Object[]{i, listShop.get(i).getImage(), listShop.get(i).getName(), listShop.get(i).getAddress(), listShop.get(i).getId()});
        }
        mAdapter.changeCursor(cursor);
    }

    @Override
    public boolean setViewValue(View view, Cursor cursor, int i) {
        String name = cursor.getString(cursor.getColumnIndex(SearchShopActivity.SHOP_NAME));
        String address = cursor.getString(cursor.getColumnIndex(SearchShopActivity.SHOP_ADDRESS));
        String image = cursor.getString(cursor.getColumnIndex(SearchShopActivity.SHOP_IMAGE));
        final String id = cursor.getString(cursor.getColumnIndex(SearchShopActivity.SHOP_ID));

        TextView shopName = (TextView) view.findViewById(R.id.shopName);
        TextView shopAddress = (TextView) view.findViewById(R.id.shopAddress);
        ImageView shopImage = (ImageView) view.findViewById(R.id.shopImg);

        shopName.setText(name);
        shopAddress.setText(address);
        SearchShopActivity.mPicaso.load(image).placeholder(R.drawable.ic_store).into(shopImage);

        Button yourButton = (Button) view.findViewById(R.id.addFollowShop);
        yourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShopModel.followShop(Global.userToken,id,new ShopModel.OnFollowShopResult() {
                    @Override
                    public void onSuccess() {
                        Log.e("result", "success");

                    }

                    @Override
                    public void onError(String error) {
                        Log.e("error", error);
                    }
                });
            }
        });
        return true;
    }
}