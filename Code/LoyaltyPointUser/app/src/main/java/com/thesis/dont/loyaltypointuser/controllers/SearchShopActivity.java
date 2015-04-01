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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.List;

public class SearchShopActivity extends ActionBarActivity implements SearchView.OnQueryTextListener, SearchView.OnSuggestionListener {
    private static final String SHOP_NAME = "shopName";
    private static final String SHOP_ADDRESS = "shopAddress";
    private static final String SHOP_IMAGE = "shopImg";

    private ArrayList<Shop> listShop;
    private ListView listView;
    private SimpleCursorAdapter mAdapter;
    MatrixCursor cursor;

    static Picasso mPicaso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_shop);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPicaso = Picasso.with(this);
        mAdapter = createAndBindingData();

        listView = (ListView) findViewById(R.id.shopsList);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SearchShopActivity.this,"click" + String.valueOf(position),Toast.LENGTH_LONG).show();
                if(parent.getSelectedItemId() == R.id.addFollowShop){
                    Toast.makeText(SearchShopActivity.this,"click button" + String.valueOf(position),Toast.LENGTH_LONG).show();
                }
            }
        });
        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SearchShopActivity.this,"select" + String.valueOf(position),Toast.LENGTH_LONG).show();
                if(parent.getSelectedItemId() == R.id.addFollowShop){
                    Toast.makeText(SearchShopActivity.this,"select button" +String.valueOf(position),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

    public SimpleCursorAdapter createAndBindingData(){
        final String[] from = new String[]{SHOP_IMAGE, SHOP_NAME, SHOP_ADDRESS};
        final int[] to = new int[]{R.id.shopImg, R.id.shopName, R.id.shopAddress};
        //create cursor and add it to Adapter.
        cursor = new MatrixCursor(new String[]{BaseColumns._ID, SHOP_IMAGE, SHOP_NAME, SHOP_ADDRESS});
        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this,
                R.layout.suggestion_list,
                cursor,
                from,
                to, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        //override method setViewValue to set image
        SimpleCursorAdapter.ViewBinder viewBinder = new SimpleCursorAdapter.ViewBinder() {

            public boolean setViewValue(View view, Cursor cursor,
                                        int columnIndex) {
                if(cursor.getCount() > 0) {
                    if(view.getId() == R.id.shopImg) {
                        ImageView image = (ImageView) view;
                        String link = cursor.getString(columnIndex);
                        mPicaso.load(link).placeholder(R.drawable.ic_store).into(image);
                    }else if(view.getId() == R.id.shopName || view.getId() == R.id.shopAddress){
                        TextView tv = (TextView) view;
                        ((TextView) view).setText(cursor.getString(columnIndex));
                    }
                }
                return true;
            }
        };

        //binding data to viewid
        ImageView image = (ImageView) findViewById(R.id.shopImg);
        viewBinder.setViewValue(image, cursor, cursor.getColumnIndex(SHOP_IMAGE));
        TextView name = (TextView) findViewById(R.id.shopName);
        viewBinder.setViewValue(name, cursor, cursor.getColumnIndex(SHOP_NAME));
        TextView address = (TextView) findViewById(R.id.shopAddress);
        viewBinder.setViewValue(address, cursor, cursor.getColumnIndex(SHOP_ADDRESS));
        //set viewbinder for adapter
        simpleCursorAdapter.setViewBinder(viewBinder);
        return simpleCursorAdapter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search_shop, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setOnSuggestionListener(this);

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
        cursor = new MatrixCursor(new String[]{BaseColumns._ID, SHOP_IMAGE, SHOP_NAME, SHOP_ADDRESS});
        for (int i = 0; i < listShop.size(); i++) {
            if (listShop.get(i).getName().toLowerCase().startsWith(query.toLowerCase()))
                cursor.addRow(new Object[]{i, listShop.get(i).getImage(), listShop.get(i).getName(), listShop.get(i).getAddress()});
        }
        mAdapter.changeCursor(cursor);
    }

    @Override
    public boolean onSuggestionSelect(int i) {
        Toast.makeText(this, String.valueOf(i) + " " + mAdapter.getCursor().getString(i), Toast.LENGTH_LONG).show();
        return true;
    }

    @Override
    public boolean onSuggestionClick(int i) {
        Toast.makeText(this, String.valueOf(i) + " " + mAdapter.getCursor().getString(i), Toast.LENGTH_LONG).show();
        return true;
    }

    public boolean onQueryTextChange(String newText) {
        populateAdapter(newText);
        return true;
    }

    public boolean onQueryTextSubmit(String query) {
        return true;
    }
}
