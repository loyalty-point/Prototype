package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.provider.BaseColumns;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.Button;
import com.gc.materialdesign.views.ButtonRectangle;
import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.CardModel;
import com.thesis.dont.loyaltypointadmin.models.Event;
import com.thesis.dont.loyaltypointadmin.models.Global;
import com.thesis.dont.loyaltypointadmin.models.Shop;

import java.util.ArrayList;

public class CardShopListApplyEventActivity extends ActionBarActivity {
    private static final String SHOP_NAME = "shopName";
    private static final String SHOP_IMG = "shopImage";
    private static final String SHOP_ADDRESS = "shopAddress";
    private static final String SHOP_ID = "shopId";
    ArrayList<ShopCheckList> shopList = new ArrayList<ShopCheckList>();

    int type;
    Event mEvent;
    String cardId;
    private ListView listView;
    ArrayList<Shop> listShops = new ArrayList<Shop>();

    private CustomSimpleCursorAdapter mAdapter;
    MatrixCursor cursor;
    public static Picasso mPicaso;
    Bitmap eventLogo = null;
    ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_shop_list_apply_event);

        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Please wait...");
        mDialog.setCancelable(false);

        Intent i = getIntent();
        mEvent = i.getParcelableExtra(Global.EVENT_OBJECT);
        cardId = i.getStringExtra(Global.CARD_ID);
        listShops = i.getParcelableArrayListExtra(Global.SHOP_LIST_OBJECT);
        type = i.getIntExtra(Global.EVENT_LIST_TYPE, 0);
        eventLogo = Global.tempBitmap;
        mPicaso = Picasso.with(this);

        final String[] from = new String[]{SHOP_NAME};
        final int[] to = new int[]{R.id.shopName, R.id.shopAddress, R.id.shopImg};
        //get data to array list of shop
        cursor = new MatrixCursor(new String[]{BaseColumns._ID, SHOP_NAME, SHOP_ADDRESS, SHOP_IMG, SHOP_ID});
        //create adapter and add it to list
        mAdapter = new CustomSimpleCursorAdapter(this,
                R.layout.shop_list_layout,
                cursor,
                from,
                to, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        // Assign adapter to ListView
        listView = (ListView) findViewById(R.id.shopCheckList);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int firstPosition = listView.getFirstVisiblePosition() - listView.getHeaderViewsCount();
                View v = listView.getChildAt(position - firstPosition);

                CheckBox checkBox = (CheckBox) ((ViewGroup)v).getChildAt(0);//get checkbox view
                checkBox.setChecked(!checkBox.isChecked());
                shopList.get(position).setSelected(checkBox.isChecked());
            }
        });

        checkButtonClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    public void getData() {
        CardModel.getListShop(Global.userToken, cardId, new CardModel.OnSelectListShopResult() {
            @Override
            public void onSuccess(ArrayList<Shop> listShops) {
                shopList.clear();
                for (int i = 0; i < listShops.size(); i++) {
                    ShopCheckList shop = new ShopCheckList(listShops.get(i), false);
                    shopList.add(shop);
                }
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
                        Toast.makeText(CardShopListApplyEventActivity.this, "error: " + error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    // search logic
    private void populateAdapter(String query) {
        cursor = new MatrixCursor(new String[]{BaseColumns._ID, SHOP_NAME, SHOP_ADDRESS, SHOP_IMG, SHOP_ID});
        for (int i = 0; i < shopList.size(); i++) {
            if (shopList.get(i).getShop().getName().toLowerCase().startsWith(query.toLowerCase()))
                cursor.addRow(new Object[]{i, shopList.get(i).getShop().getName(), shopList.get(i).getShop().getAddress(), shopList.get(i).getShop().getImage(), shopList.get(i).getShop().getId()});
        }
        mAdapter.changeCursor(cursor);

    }

    private void checkButtonClick() {


        ButtonRectangle cancelBtn = (ButtonRectangle) findViewById(R.id.backBtn);
        ButtonRectangle createBtn = (ButtonRectangle) findViewById(R.id.createEventBtn);
        if (type == Global.CARD_EDIT_EVENT_LIST)
            createBtn.setText("EDIT");
        createBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ArrayList<String> listShopsId = new ArrayList<String>();
                for (int i = 0; i < shopList.size(); i++) {
                    if (shopList.get(i).isSelected()) {
                        listShopsId.add(shopList.get(i).getShop().getId());
                    }
                }
                if (type == Global.CARD_EDIT_EVENT_LIST) {
                    mDialog.setTitle("Updating event");
                    mDialog.show();
                    CardModel.editEvent(mEvent, listShopsId, cardId, new CardModel.OnAddEventResult() {
                        @Override
                        public void onSuccess(final CardModel.CreateEventResult createEventResult) {
                            if(eventLogo != null) {
                                GCSHelper.uploadImage(CardShopListApplyEventActivity.this, createEventResult.bucketName, createEventResult.fileName, eventLogo, new GCSHelper.OnUploadImageResult() {
                                    @Override
                                    public void onComplete() {

                                        // dismiss Progress Dialog
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                mDialog.dismiss();
                                            }
                                        });
                                        String imageLink = "http://storage.googleapis.com/" + createEventResult.bucketName + "/" + createEventResult.fileName;
                                        mPicaso.invalidate(imageLink);
                                        Global.tempActivity.finish();
                                        finish();
                                    }

                                    @Override
                                    public void onError(final String error) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                mDialog.dismiss();
                                                Toast.makeText(CardShopListApplyEventActivity.this, error, Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                });
                            }else{
                                Global.tempActivity.finish();
                                finish();
                            }
                        }

                        @Override
                        public void onError(final String error) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(CardShopListApplyEventActivity.this, error, Toast.LENGTH_LONG);
                                }
                            });
                        }
                    });
                }else if(type == Global.CARD_CREATE_EVENT_LIST){
                    mDialog.setTitle("Creating event");
                    mDialog.show();
                    CardModel.createEvent(mEvent, listShopsId, cardId, new CardModel.OnAddEventResult() {
                        @Override
                        public void onSuccess(CardModel.CreateEventResult createEventResult) {
                            if(eventLogo != null) {
                                GCSHelper.uploadImage(CardShopListApplyEventActivity.this, createEventResult.bucketName, createEventResult.fileName, eventLogo, new GCSHelper.OnUploadImageResult() {
                                    @Override
                                    public void onComplete() {

                                        // dismiss Progress Dialog
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                mDialog.dismiss();
                                            }
                                        });

                                        Global.tempActivity.finish();
                                        finish();
                                    }

                                    @Override
                                    public void onError(final String error) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                mDialog.dismiss();
                                                Toast.makeText(CardShopListApplyEventActivity.this, error, Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                });
                            }else{
                                Global.tempActivity.finish();
                                finish();
                            }
                        }

                        @Override
                        public void onError(final String error) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(CardShopListApplyEventActivity.this, error, Toast.LENGTH_LONG);
                                }
                            });
                        }
                    });
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
            View view = inflater.inflate(R.layout.shop_check_list_row, parent, false);

            return view;
        }

        @Override
        public void bindView(View view, final Context context, final Cursor cursor) {

            String name = cursor.getString(cursor.getColumnIndex(SHOP_NAME));
            String address = cursor.getString(cursor.getColumnIndex(SHOP_ADDRESS));
            String image = cursor.getString(cursor.getColumnIndex(SHOP_IMG));
            final int position = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));

            TextView shopName = (TextView) view.findViewById(R.id.shopName);
            TextView shopAddress = (TextView) view.findViewById(R.id.shopAddress);
            ImageView shopImg = (ImageView) view.findViewById(R.id.shopImg);
            final CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
            if(type == Global.CARD_EDIT_EVENT_LIST) {
                for (int i = 0; i < listShops.size(); i++) {
                    if (shopList.get(position).shop.getId().equals(listShops.get(i).getId())) {
                        shopList.get(position).setSelected(true);
                        break;
                    }
                }
            }
            checkBox.setChecked(shopList.get(position).isSelected());
            checkBox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    shopList.get(position).setSelected(checkBox.isChecked());
                }
            });
            shopName.setText(name);
            shopAddress.setText(address);
            if (image.equals(""))
                image = null;
            mPicaso.load(image).placeholder(R.drawable.ic_user_avatar).into(shopImg);
        }
    }

    public class ShopCheckList {

        private Shop shop;
        private boolean selected = false;

        public ShopCheckList(Shop shop, boolean isSelected) {
            this.setSelected(isSelected);
            this.setShop(shop);
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public Shop getShop() {
            return shop;
        }

        public void setShop(Shop shop) {
            this.shop = shop;
        }
    }

}
