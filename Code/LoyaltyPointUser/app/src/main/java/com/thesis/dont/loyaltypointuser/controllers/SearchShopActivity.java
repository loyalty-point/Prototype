package com.thesis.dont.loyaltypointuser.controllers;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.app.ProgressDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.avast.android.dialogs.fragment.SimpleDialogFragment;
import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointuser.R;
import com.thesis.dont.loyaltypointuser.models.CardModel;
import com.thesis.dont.loyaltypointuser.models.Global;
import com.thesis.dont.loyaltypointuser.models.RequiredCustomerInfo;
import com.thesis.dont.loyaltypointuser.models.Shop;
import com.thesis.dont.loyaltypointuser.models.ShopModel;

import java.util.ArrayList;

public class SearchShopActivity extends ActionBarActivity implements SearchView.OnQueryTextListener {
    public static final String SHOP_NAME = "shopName";
    public static final String SHOP_ADDRESS = "shopAddress";
    public static final String SHOP_IMAGE = "shopImg";
    public static final String SHOP_ID = "shopId";
    public static final String CARD_NAME = "cardName";
    public static final String CARD_ID = "cardId";

    private ArrayList<Shop> listShop = new ArrayList<Shop>();
    private ListView listView;
    private CustomSimpleCursorAdapter mAdapter;

    MatrixCursor cursor;
    String[] requiredInfoArray = new String[]{"Phone", "Email", "Full name", "Address", "Identity Number"};
    RequiredCustomerInfo requiredCustomerInfo = new RequiredCustomerInfo(0, 0, 0, 0, 0);

    ProgressDialog mDialog;

    public static Picasso mPicaso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_search_shop);

        // init dialog
        mDialog = new ProgressDialog(this);
        mDialog.setTitle("Loading list shops");
        mDialog.setMessage("Please wait...");
        mDialog.setCancelable(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPicaso = Picasso.with(this);
        final String[] from = new String[]{SHOP_IMAGE, SHOP_NAME, SHOP_ADDRESS, CARD_NAME, SHOP_ID, CARD_ID};
        final int[] to = new int[]{R.id.shopImg, R.id.shopName, R.id.shopAddress};
        cursor = new MatrixCursor(new String[]{BaseColumns._ID, SHOP_IMAGE, SHOP_NAME, SHOP_ADDRESS, CARD_NAME, SHOP_ID, CARD_ID});
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
        mDialog.show();
        ShopModel.getUnfollowedShop(Global.userToken, new ShopModel.OnSelectAllShopResult() {
            @Override
            public void onSuccess(ArrayList<Shop> listShops) {
                SearchShopActivity.this.listShop = listShops;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.dismiss();
                        populateAdapter("");
                    }
                });
            }

            @Override
            public void onError(final String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.dismiss();
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
        cursor = new MatrixCursor(new String[]{BaseColumns._ID, SHOP_IMAGE, SHOP_NAME, SHOP_ADDRESS, CARD_NAME, SHOP_ID, CARD_ID});
        for (int i = 0; i < listShop.size(); i++) {
            if (listShop.get(i).getName().toLowerCase().startsWith(query.toLowerCase()))
                cursor.addRow(new Object[]{i, listShop.get(i).getImage(), listShop.get(i).getName(), listShop.get(i).getAddress(), listShop.get(i).getCardName(), listShop.get(i).getId(), listShop.get(i).getCardId()});
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
        public TextView cardName;
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
            holder.cardName = (TextView) view.findViewById(R.id.cardName);
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
            String cardName = cursor.getString(cursor.getColumnIndex(SearchShopActivity.CARD_NAME));
            String image = cursor.getString(cursor.getColumnIndex(SearchShopActivity.SHOP_IMAGE));
            final String id = cursor.getString(cursor.getColumnIndex(SearchShopActivity.SHOP_ID));
            final String cardId = cursor.getString(cursor.getColumnIndex(SearchShopActivity.CARD_ID));

            holder.shopName.setText(name);
            holder.shopAddress.setText(address);
            holder.cardName.setText(cardName);
            SearchShopActivity.mPicaso.load(image).placeholder(R.drawable.ic_store).into(holder.shopImg);

            Button followBtn = (Button) view.findViewById(R.id.addFollowShop);
            followBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CardModel.getRequiredCustomerInfo(Global.userToken, cardId, new CardModel.OnGetRequiredCustomerInfoResult() {
                        @Override
                        public void onSuccess(RequiredCustomerInfo requiredCustomerInfo) {
                            SearchShopActivity.this.requiredCustomerInfo = requiredCustomerInfo;
                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE: {
                                            ShopModel.followShop(Global.userToken, cardId, 0, new ShopModel.OnFollowShopResult() {
                                                @Override
                                                public void onSuccess() {
                                                    Log.e("result", "success");
                                                    for (int i = 0; i < listShop.size(); i++) {
                                                        if (listShop.get(i).getCardId().equals(cardId)) {
                                                            listShop.remove(i);
                                                            i--;
                                                        }
                                                    }
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                                            builder.setTitle("Your request has been sent successfully")
                                                                    .setMessage("Please wait for the acceptance!")
                                                                    .setNegativeButton("My Pending Cards", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                            Intent i = new Intent(SearchShopActivity.this, CardsListActivity.class);
                                                                            i.putExtra(Global.FRAGMENT_ID, 1);
                                                                            startActivity(i);
                                                                            SearchShopActivity.this.finish();
                                                                        }
                                                                    })
                                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {

                                                                        }
                                                                    }).show();
                                                            populateAdapter("");
                                                        }
                                                    });
                                                    //SimpleDialogFragment.createBuilder(SearchShopActivity.this, getSupportFragmentManager()).setMessage("You've just register as a member of this shop\nPlease waiting for the acceptance").show();
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

//                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                    builder.setMessage("Do you want to be a member of this shop?").setPositiveButton("Yes", dialogClickListener)
//                            .setNegativeButton("No", dialogClickListener).show();

                            final AlertDialog.Builder builderSingle = new AlertDialog.Builder(
                                    context);
                            builderSingle.setTitle("Do you want to be a member of this shop?");

                            //setup list
                            ArrayList<String> requiredInfo = new ArrayList<String>();
                            for(int i = 0; i < requiredInfoArray.length; i++){
                                if(requiredCustomerInfo.getStateAtPosition(i) != 0) {
                                    requiredInfo.add(requiredInfoArray[i]);
                                }
                            }

                            final RequiredUserInfoAdapter adapter = new RequiredUserInfoAdapter(SearchShopActivity.this , requiredInfo);
                            builderSingle.setAdapter(adapter,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    });
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    builderSingle.show();
                                }
                            });

                        }

                        @Override
                        public void onError(String error) {
                            Log.e("error", error);
                        }
                    });

                }
            });
        }
    }

    private class RequiredUserInfoAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final ArrayList<String> userRequiredInfo;

        public RequiredUserInfoAdapter(Context context, ArrayList<String> values) {
            super(context, -1, values);
            this.context = context;
            this.userRequiredInfo = values;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.user_required_info_row, parent, false);

            TextView userInfo = (TextView) rowView.findViewById(R.id.userInfo);
            userInfo.setText(userRequiredInfo.get(position));

            return rowView;
        }
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
}