package com.thesis.dont.loyaltypointuser.controllers;

import android.database.MatrixCursor;
import android.provider.BaseColumns;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.thesis.dont.loyaltypointuser.R;

public class SearchShopActivity extends ActionBarActivity implements SearchView.OnQueryTextListener, SearchView.OnSuggestionListener {

    private static final String[] SUGGESTIONS = {
            "Bauru", "Sao Paulo", "Rio de Janeiro",
            "Bahia", "Mato Grosso", "Minas Gerais",
            "Tocantins", "Rio Grande do Sul"
    };
    private SimpleCursorAdapter mAdapter;
    private TextView mStatusView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_shop);
        mStatusView = (TextView) findViewById(R.id.resultTv);
        final String[] from = new String[]{"cityName"};
        final int[] to = new int[]{R.id.text1};
        mAdapter = new SimpleCursorAdapter(this,
                R.layout.suggestion_list,
                null,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
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
        mStatusView.setText("Query = " + newText);
        populateAdapter(newText);
        return true;
    }

    public boolean onQueryTextSubmit(String query) {
        mStatusView.setText("Query = " + query + " : submitted");
        return true;
    }

    // You must implements your logic to get data using OrmLite
    private void populateAdapter(String query) {
        final MatrixCursor c = new MatrixCursor(new String[]{BaseColumns._ID, "cityName"});
        for (int i = 0; i < SUGGESTIONS.length; i++) {
            if (SUGGESTIONS[i].toLowerCase().startsWith(query.toLowerCase()))
                c.addRow(new Object[]{i, SUGGESTIONS[i]});
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
