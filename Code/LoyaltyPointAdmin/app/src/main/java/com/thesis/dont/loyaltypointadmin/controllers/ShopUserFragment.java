package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFloat;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Global;
import com.thesis.dont.loyaltypointadmin.models.User;

import butterknife.ButterKnife;

public class ShopUserFragment extends Fragment implements SearchView.OnQueryTextListener, SearchView.OnSuggestionListener {
    private static final String ARG_POSITION = "position";

    ButtonFloat barcodeBtn;

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mParentActivity = getActivity();

        // add expandable button
        //addExpandableButton();

        barcodeBtn = (ButtonFloat) mParentActivity.findViewById(R.id.barcodeBtn);
        barcodeBtn.setBackgroundColor(getResources().getColor(R.color.AccentColor));
        barcodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start Scan Barcode Activity
                /*IntentIntegrator scanIntegrator = new IntentIntegrator(getActivity());
                scanIntegrator.initiateScan();*/

                Intent i = new Intent(mParentActivity, CalculatePointActivity.class);
                User user = new User("username", "password", "fullname", "phone", "email", "address", "avatar", "token");
                i.putExtra(Global.USER_OBJECT, user);
                startActivity(i);

                /*Intent i = new Intent(mParentActivity, ScannerActivity.class);
                startActivity(i);*/
            }
        });

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

    //call back when scan the bar code successfully
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            // Load user from barcode
            String barcode = scanningResult.getContents();
            // Start CalculatePointActivity
            Intent i = new Intent(mParentActivity, CalculatePointActivity.class);
            User user = new User("username", "password", "fullname", "phone", "email", "address", "avatar", "token");
            i.putExtra(Global.USER_OBJECT, user);
            startActivity(i);
        } else {

        }
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

    private void addExpandableButton() {
        /*actionBtn = (ButtonFloat) mParentActivity.findViewById(R.id.actionBtn);
        actionBtn.setBackgroundColor(getResources().getColor(R.color.AccentColor));*/

        ImageView actionBtn;
        actionBtn = new ImageView(mParentActivity);
        //actionBtn.setBackgroundColor(getResources().getColor(R.color.AccentColor));
        actionBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_new));

        FloatingActionButton actionButton = new FloatingActionButton.Builder(mParentActivity)
                .setContentView(actionBtn)
                .setBackgroundDrawable(R.drawable.expandable_button_background_accent)
                .build();

        //actionButton.setBackgroundColor(getResources().getColor(R.color.AccentColor));
        //actionButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.account_background));


        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(mParentActivity);
        itemBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.expandable_button_background_accent));

        // create barcode Item
        ImageView barcodeItem = new ImageView(mParentActivity);
        //barcodeItem.setBackgroundColor(getResources().getColor(R.color.AccentColor));
        barcodeItem.setImageDrawable(getResources().getDrawable(R.drawable.barcode_ic));
        //SubActionButton barcodeAction = itemBuilder.setContentView(barcodeItem).build();
        /*barcodeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mParentActivity, "You have clicked barcode button", Toast.LENGTH_LONG).show();
            }
        });*/

        SubActionButton barcodeAction = itemBuilder.setContentView(barcodeItem).build();
        barcodeAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mParentActivity, "You have clicked barcode button", Toast.LENGTH_LONG).show();
            }
        });

        // create NFC Item
        ImageView NFCItem = new ImageView(mParentActivity);
        //NFCItem.setBackgroundColor(getResources().getColor(R.color.AccentColor));
        NFCItem.setImageDrawable(getResources().getDrawable(R.drawable.nfc_ic));

        /*NFCItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mParentActivity, "You have clicked NFC button", Toast.LENGTH_LONG).show();
            }
        });*/

        SubActionButton NFCAction = itemBuilder.setContentView(NFCItem).build();
        NFCAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mParentActivity, "You have clicked NFC button", Toast.LENGTH_LONG).show();
            }
        });

        /*FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(mParentActivity)
                .addSubActionView(itemBuilder.setContentView(barcodeItem).build())
                .addSubActionView(itemBuilder.setContentView(NFCItem).build())
                .attachTo(actionButton)
                .build();*/

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(mParentActivity)
                .addSubActionView(barcodeAction)
                .addSubActionView(NFCAction)
                .attachTo(actionButton)
                .build();
    }
}
