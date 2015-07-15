package com.thesis.dont.loyaltypointuser.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointuser.R;
import com.thesis.dont.loyaltypointuser.models.Card;
import com.thesis.dont.loyaltypointuser.models.CardModel;
import com.thesis.dont.loyaltypointuser.models.Global;
import com.thesis.dont.loyaltypointuser.models.Shop;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class CardDetailFragment extends Fragment implements SearchView.OnQueryTextListener {

    private static final String ARG_POSITION = "position";
    private static final String ARG_CARDID = "cardId";
    private static final String SHOP_NAME = "shopName";
    private static final String SHOP_IMG = "shopImage";
    private static final String SHOP_ADDRESS = "shopAddress";
    private static final String SHOP_ID = "shopId";

    Activity mParentActivity;
    SwipeRefreshLayout mSwipeLayout;

    private Card mCard;

    View rootView;

    private ArrayList<Shop> listShop;
    private ListView listView;
    private CustomSimpleCursorAdapter mAdapter;
    MatrixCursor cursor;
    public static Picasso mPicaso;

    private int position;

    public CardDetailFragment() {}

    public CardDetailFragment(int position, Card cardId) {
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        b.putParcelable(ARG_CARDID, cardId);
        this.setArguments(b);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
        mCard = getArguments().getParcelable(ARG_CARDID);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_card_detail, container, false);

        ButterKnife.inject(this, rootView);

        ViewCompat.setElevation(rootView, 50);
        return rootView;
    }

    private void getListShops() {
        CardModel.getListShop(Global.userToken, mCard.getId(), new CardModel.OnSelectListShopResult() {
            @Override
            public void onSuccess(ArrayList<Shop> listShops) {
                listShop = listShops;
                mParentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        populateAdapter("");
                        mSwipeLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onError(final String error) {
                mParentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeLayout.setRefreshing(false);
                        Toast.makeText(mParentActivity, "error: " + error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mParentActivity = getActivity();
        mPicaso = Picasso.with(mParentActivity);


        //create list shop and search adapter.
        final String[] from = new String[]{SHOP_NAME};
        final int[] to = new int[]{R.id.shopName, R.id.shopAddress, R.id.shopImg};
        cursor = new MatrixCursor(new String[]{BaseColumns._ID, SHOP_NAME, SHOP_ADDRESS, SHOP_IMG, SHOP_ID});
        //create adapter and add it to list
        mAdapter = new CustomSimpleCursorAdapter(mParentActivity,
                R.layout.shop_list_layout,
                cursor,
                from,
                to, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        listView = (ListView) rootView.findViewById(R.id.listShops);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(mParentActivity, ShopDetailActivity.class);
                Shop currentShop = null;
                for (int j = 0; j < listShop.size(); j++) {
                    if (listShop.get(j).getId().equals(cursor.getString(4)))
                        currentShop = listShop.get(j);
                }
                i.putExtra(Global.CARD_OBJECT, mCard);
                i.putExtra(Global.SHOP_OBJECT, currentShop);
                startActivity(i);

            }
        });

        mSwipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        mSwipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doRefresh();
            }
        });

        doRefresh();

        setHasOptionsMenu(true);
    }

    public void doRefresh() {
        mSwipeLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(true);
                getListShops();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search_shop, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(this);
    }


    // search logic
    private void populateAdapter(String query) {
        cursor = new MatrixCursor(new String[]{BaseColumns._ID, SHOP_NAME, SHOP_ADDRESS, SHOP_IMG, SHOP_ID});
        for (int i = 0; i < listShop.size(); i++) {
            if (listShop.get(i).getName().toLowerCase().startsWith(query.toLowerCase()))
                cursor.addRow(new Object[]{i, listShop.get(i).getName(), listShop.get(i).getAddress(), listShop.get(i).getImage(), listShop.get(i).getId()});
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

    //Custom cursor to update data for suggestion list
    public class CustomSimpleCursorAdapter extends SimpleCursorAdapter {
        private Context mContext;

        public CustomSimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
            super(context, layout, c, from, to, flags);
            this.mContext = context;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.shop_list_layout, parent, false);

            return view;
        }

        @Override
        public void bindView(View view, final Context context, final Cursor cursor) {
            String name = cursor.getString(cursor.getColumnIndex(SHOP_NAME));
            String address = cursor.getString(cursor.getColumnIndex(SHOP_ADDRESS));
            String image = cursor.getString(cursor.getColumnIndex(SHOP_IMG));
            final String shopId = cursor.getString(cursor.getColumnIndex(SHOP_ID));

            TextView shopName = (TextView) view.findViewById(R.id.shopName);
            TextView shopAddress = (TextView) view.findViewById(R.id.shopAddress);
            ImageView shopImg = (ImageView) view.findViewById(R.id.shopImg);

            shopName.setText(name);
            shopAddress.setText(address);
            if (image.equals(""))
                image = null;
            mPicaso.load(image).placeholder(R.drawable.ic_store).into(shopImg);
        }
    }

}
