package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFloat;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Customer;
import com.thesis.dont.loyaltypointadmin.models.Global;
import com.thesis.dont.loyaltypointadmin.models.ShopModel;
import com.thesis.dont.loyaltypointadmin.models.Shop;
import com.thesis.dont.loyaltypointadmin.models.User;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class ShopUserFragment extends Fragment implements SearchView.OnQueryTextListener {
    private static final String ARG_POSITION = "position";
    private static final String ARG_SHOPID = "shopId";
    private static final String USER_ID = "userId";
    private static final String USER_NAME = "userName";
    private static final String USER_IMG = "userImg";
    private static final String USER_PHONENUMBER = "userPhoneNumber";
    private static final String USER_POINT = "user_point";
    public static final String USER_OBJECT = "userObject";

    ButtonFloat barcodeBtn;

    Activity mParentActivity;

    private String shopId;
    private String cardId;

    private ArrayList<Customer> listUser;
    private ListView listView;
    private CustomSimpleCursorAdapter mAdapter;
    MatrixCursor cursor;
    public static Picasso mPicaso;

    private int position;

    public ShopUserFragment() {}

    public ShopUserFragment(int position) {
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
        View rootView = inflater.inflate(R.layout.fragment_shop_user, container, false);

        ButterKnife.inject(this, rootView);

        ViewCompat.setElevation(rootView, 50);
        return rootView;
    }

    private void getListUsers() {
        ShopModel.getFollowingUsers(Global.userToken, shopId, cardId, new ShopModel.OnSelectFollowingUsersResult() {
            @Override
            public void onSuccess(ArrayList<Customer> listUsers) {
                listUser = listUsers;
                mParentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        populateAdapter("");
                    }
                });
            }

            @Override
            public void onError(final String error) {
                mParentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mParentActivity, "error: " + error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cardId = ((ShopDetailActivity)getActivity()).getCurrentCardId();
        shopId = ((ShopDetailActivity)getActivity()).getCurrentShop().getId();
        mParentActivity = getActivity();
        mPicaso = Picasso.with(mParentActivity);
        // add expandable button
        //addExpandableButton();

        barcodeBtn = (ButtonFloat) mParentActivity.findViewById(R.id.barcodeBtn);
        barcodeBtn.setBackgroundColor(getResources().getColor(R.color.AccentColor));
        barcodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start Scan Barcode Activity

                Intent i = new Intent(mParentActivity, ScannerActivity.class);
                i.putExtra(Global.CARD_ID, cardId);
                i.putParcelableArrayListExtra(Global.USER_LIST, listUser);

                // put shop into intent
                Shop shop = ((ShopDetailActivity) mParentActivity).getCurrentShop();
                i.putExtra(Global.SHOP_OBJECT, shop);

                startActivity(i);
            }
        });
        //create list user and search adapter.
        final String[] from = new String[]{USER_NAME};
        final int[] to = new int[]{R.id.userName, R.id.userPhone, R.id.userImg};
        cursor = new MatrixCursor(new String[]{BaseColumns._ID, USER_NAME, USER_PHONENUMBER, USER_IMG, USER_ID, USER_POINT});
        //create adapter and add it to list
        mAdapter = new CustomSimpleCursorAdapter(mParentActivity,
                R.layout.search_user_layout,
                cursor,
                from,
                to, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        listView = (ListView) mParentActivity.findViewById(R.id.usersList);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(mParentActivity, UserDetailActivity.class);
                i.putExtra(Global.USER_NAME, cursor.getString(4));
                i.putExtra(Global.USER_FULLNAME, cursor.getString(1));
                i.putExtra(Global.SHOP_ID, shopId);
                i.putExtra(Global.USER_POINT, Integer.parseInt(cursor.getString(5)));
                i.putExtra(Global.CARD_ID, cardId);
                Shop shop = ((ShopDetailActivity)mParentActivity).getCurrentShop();
                i.putExtra(Global.SHOP_OBJECT, shop);
                startActivity(i);

            }
        });
        //getListUsers();
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search_shop, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(this);
    }

    //call back when scan the bar code successfully
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        /*IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            // Load user from barcode
            String barcode = scanningResult.getContents();
            // Start CalculatePointActivity
            Intent i = new Intent(mParentActivity, CalculatePointActivity.class);
            User user = new User("username", "password", "fullname", "phone", "email", "address", "avatar", "token");
            i.putExtra(Global.USER_OBJECT, user);
            startActivity(i);
        } else {

        }*/

        /*switch(requestCode) {
            case Global.SCAN_BARCODE:
                if(resultCode == Activity.RESULT_OK){
                    String barcode = intent.getStringExtra(Global.BARCODE);

                }
        }*/
    }

    // search logic
    private void populateAdapter(String query) {
        cursor = new MatrixCursor(new String[]{BaseColumns._ID, USER_NAME, USER_PHONENUMBER, USER_IMG, USER_ID, USER_POINT});
        for (int i = 0; i < listUser.size(); i++) {
            if (listUser.get(i).getFullname().toLowerCase().startsWith(query.toLowerCase()))
                cursor.addRow(new Object[]{i, listUser.get(i).getFullname(), listUser.get(i).getPhone(), listUser.get(i).getAvatar(), listUser.get(i).getUsername(), listUser.get(i).getPoint()});
        }
        mAdapter.changeCursor(cursor);

    }

    @Override
    public void onResume() {
        super.onResume();

        getListUsers();
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
            View view = inflater.inflate(R.layout.search_user_layout, parent, false);

            return view;
        }

        @Override
        public void bindView(View view, final Context context, final Cursor cursor) {
            String fullname = cursor.getString(cursor.getColumnIndex(USER_NAME));
            String phone = cursor.getString(cursor.getColumnIndex(USER_PHONENUMBER));
            String image = cursor.getString(cursor.getColumnIndex(USER_IMG));
            final String username = cursor.getString(cursor.getColumnIndex(USER_ID));

            TextView userName = (TextView) view.findViewById(R.id.userName);
            TextView userPhone = (TextView) view.findViewById(R.id.userPhone);
            ImageView userImg = (ImageView) view.findViewById(R.id.userImg);

            userName.setText(fullname);
            userPhone.setText(phone);
            if (image.equals(""))
                image = null;
            mPicaso.load(image).placeholder(R.drawable.ic_user_avatar).into(userImg);

            Button addBtn = (Button) view.findViewById(R.id.addUserPoint);
            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(mParentActivity, CalculatePointActivity.class);
                    i.putExtra(Global.CARD_ID, cardId);
                    // put username into intent
                    i.putExtra(Global.USER_NAME, username);

                    // put shop into intent
                    Shop shop = ((ShopDetailActivity) mParentActivity).getCurrentShop();
                    i.putExtra(Global.SHOP_OBJECT, shop);

                    startActivity(i);
                }
            });
        }
    }


    private void addExpandableButton() {

        ImageView actionBtn;
        actionBtn = new ImageView(mParentActivity);
        //actionBtn.setBackgroundColor(getResources().getColor(R.color.AccentColor));
        actionBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_new));

        FloatingActionButton actionButton = new FloatingActionButton.Builder(mParentActivity)
                .setContentView(actionBtn)
                .setBackgroundDrawable(R.drawable.expandable_button_background_accent)
                .build();

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(mParentActivity);
        itemBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.expandable_button_background_accent));

        // create barcode Item
        ImageView barcodeItem = new ImageView(mParentActivity);
        //barcodeItem.setBackgroundColor(getResources().getColor(R.color.AccentColor));
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

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(mParentActivity)
                .addSubActionView(barcodeAction)
                .addSubActionView(NFCAction)
                .attachTo(actionButton)
                .build();
    }
}
