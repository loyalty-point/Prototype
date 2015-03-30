package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFloat;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.thesis.dont.loyaltypointadmin.R;

import butterknife.ButterKnife;

public class ShopUserFragment extends Fragment implements SearchView.OnQueryTextListener, SearchView.OnSuggestionListener {
    private static final String ARG_POSITION = "position";

    ImageView actionBtn;

    Activity mParentActivity;

    private int position;

    private SimpleCursorAdapter mAdapter;

    public ShopUserFragment(int position, String shopId){
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        this.setArguments(b);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shop_user,container,false);

        ButterKnife.inject(this, rootView);

        ViewCompat.setElevation(rootView, 50);
        return rootView;
    }

    private void addExpandableButton() {
        /*actionBtn = (ButtonFloat) mParentActivity.findViewById(R.id.actionBtn);
        actionBtn.setBackgroundColor(getResources().getColor(R.color.AccentColor));*/

        actionBtn = new ImageView(mParentActivity);
        actionBtn.setBackgroundColor(getResources().getColor(R.color.AccentColor));
        actionBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_new));

        FloatingActionButton actionButton = new FloatingActionButton.Builder(mParentActivity)
                .setContentView(actionBtn)
                .build();

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(mParentActivity);

        // create barcode Item
        ImageView barcodeItem = new ImageView(mParentActivity);
        barcodeItem.setImageDrawable(getResources().getDrawable(R.drawable.barcode_ic));
        SubActionButton barcodeAction = itemBuilder.setContentView(barcodeItem).build();
        barcodeAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mParentActivity, "You have clicked barcode button", Toast.LENGTH_LONG).show();
            }
        });

        // create NFC Item
        ImageView NFCItem = new ImageView(mParentActivity);
        NFCItem.setImageDrawable(getResources().getDrawable(R.drawable.nfc_ic));
        SubActionButton NFCAction = itemBuilder.setContentView(NFCItem).build();
        NFCAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mParentActivity, "You have clicked NFC button", Toast.LENGTH_LONG).show();
            }
        });

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(mParentActivity)
                .addSubActionView(barcodeItem)
                .addSubActionView(NFCItem)
                .attachTo(actionButton)
                .build();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mParentActivity = getActivity();

        // add expandable button
        addExpandableButton();

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search_shop, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setOnSuggestionListener(this);
        searchView.setSuggestionsAdapter(mAdapter);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    @Override
    public boolean onSuggestionSelect(int i) {
        return false;
    }

    @Override
    public boolean onSuggestionClick(int i) {
        return false;
    }
}
