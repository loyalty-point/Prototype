package com.thesis.dont.loyaltypointuser.controllers;

import android.database.MatrixCursor;
import android.provider.BaseColumns;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.thesis.dont.loyaltypointuser.R;
import com.thesis.dont.loyaltypointuser.models.Shop;
import com.thesis.dont.loyaltypointuser.models.ShopModel;

import java.util.ArrayList;
import java.util.List;

public class SearchShopActivity extends ActionBarActivity implements SearchView.OnQueryTextListener, SearchView.OnSuggestionListener {

    private ArrayList<String> shopsNameList;
    private ListView listView;
    private SimpleCursorAdapter mAdapter;
    private ShopsListAdapter shopsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_shop);
        final String[] from = new String[]{"cityName"};
        final int[] to = new int[]{R.id.text1};
        mAdapter = new SimpleCursorAdapter(this,
                R.layout.suggestion_list,
                null,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        shopsListAdapter = new ShopsListAdapter(this, new ArrayList<Shop>());
        listView = (ListView) findViewById(R.id.shopsList);
        listView.setAdapter(shopsListAdapter);
        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        shopsNameList = new ArrayList<String>();
        getListShops();
    }

    public void getListShops() {
        ShopModel.getAllShop(Global.userToken, new ShopModel.OnSelectAllShopResult() {
            @Override
            public void onSuccess(ArrayList<Shop> listShops) {
                for(int i = 0; i<listShops.size();i++){
                    shopsNameList.add(listShops.get(i).getName());
                }
                shopsListAdapter.setListShops(listShops);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        shopsListAdapter.notifyDataSetChanged();
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
        searchView.setOnSuggestionListener(this);
        searchView.setSuggestionsAdapter(mAdapter);
        return true;
    }

    public boolean onQueryTextChange(String newText) {
        populateAdapter(newText);
        return true;
    }

    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    // You must implements your logic to get data using OrmLite
    private void populateAdapter(String query) {
        final MatrixCursor c = new MatrixCursor(new String[]{BaseColumns._ID, "cityName"});
        for (int i = 0; i < shopsNameList.size(); i++) {
            if (shopsNameList.get(i).toLowerCase().startsWith(query.toLowerCase()))
                c.addRow(new Object[]{i, shopsNameList.get(i)});
        }
        mAdapter.changeCursor(c);
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
}
