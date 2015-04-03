package com.thesis.dont.loyaltypointuser.controllers;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.provider.BaseColumns;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointuser.R;
import com.thesis.dont.loyaltypointuser.models.Global;
import com.thesis.dont.loyaltypointuser.models.Shop;
import com.thesis.dont.loyaltypointuser.models.ShopModel;

import java.util.ArrayList;

public class SearchShopActivity extends ActionBarActivity implements SearchView.OnQueryTextListener {
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
        //create adapter and add it to list
        mAdapter = new CustomSimpleCursorAdapter(this,
                R.layout.suggestion_list,
                cursor,
                from,
                to, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        listView = (ListView) findViewById(R.id.shopsList);
        listView.setAdapter(mAdapter);
        getListShops();
    }

    public void getListShops() {
        ShopModel.getUnfollowedShop(Global.userToken, new ShopModel.OnSelectAllShopResult() {
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
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(this);
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

    // search logic
    private void populateAdapter(String query) {
        cursor = new MatrixCursor(new String[]{BaseColumns._ID, SHOP_IMAGE, SHOP_NAME, SHOP_ADDRESS, SHOP_ID});
        for (int i = 0; i < listShop.size(); i++) {
            if (listShop.get(i).getName().toLowerCase().startsWith(query.toLowerCase()))
                cursor.addRow(new Object[]{i, listShop.get(i).getImage(), listShop.get(i).getName(), listShop.get(i).getAddress(), listShop.get(i).getId()});
        }
        mAdapter.changeCursor(cursor);

    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        populateAdapter(s);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        populateAdapter(s);
        return false;
    }

    public static class ViewHolder {
        public ImageView shopImg;
        public TextView shopName;
        public TextView shopAddress;
    }

    //custom adapter for suggestion list
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

            ViewHolder holder = new ViewHolder();
            holder.shopName = (TextView) view.findViewById(R.id.shopName);
            holder.shopAddress = (TextView) view.findViewById(R.id.shopAddress);
            holder.shopImg = (ImageView) view.findViewById(R.id.shopImg);

            view.setTag(holder);
            return view;
        }

        //bind view to get data from list
        @Override
        public void bindView(View view, final Context context, final Cursor cursor) {
            ViewHolder holder = (ViewHolder) view.getTag();

            String name = cursor.getString(cursor.getColumnIndex(SearchShopActivity.SHOP_NAME));
            String address = cursor.getString(cursor.getColumnIndex(SearchShopActivity.SHOP_ADDRESS));
            String image = cursor.getString(cursor.getColumnIndex(SearchShopActivity.SHOP_IMAGE));
            final String id = cursor.getString(cursor.getColumnIndex(SearchShopActivity.SHOP_ID));

            holder.shopName.setText(name);
            holder.shopAddress.setText(address);
            SearchShopActivity.mPicaso.load(image).placeholder(R.drawable.ic_store).into(holder.shopImg);

            Button followBtn = (Button) view.findViewById(R.id.addFollowShop);
            followBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE: {
                                    ShopModel.followShop(Global.userToken, id, 0, new ShopModel.OnFollowShopResult() {
                                        @Override
                                        public void onSuccess() {
                                            Log.e("result", "success");
                                            for (int i = 0; i < listShop.size(); i++) {
                                                if (listShop.get(i).getId().equals(id)) {
                                                    listShop.remove(i);
                                                    break;
                                                }
                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    populateAdapter("");
                                                }
                                            });

                                        }

                                        @Override
                                        public void onError(String error) {
                                            Log.e("error", error);
                                        }
                                    });
                                    break;
                                }

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Do you want to follow this shop?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }
            });
        }
    }
}