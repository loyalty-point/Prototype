package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFloat;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Global;
import com.thesis.dont.loyaltypointadmin.models.Shop;
import com.thesis.dont.loyaltypointadmin.models.ShopModel;
import com.tjerkw.slideexpandable.library.ActionSlideExpandableListView;
import com.tjerkw.slideexpandable.library.SlideExpandableListAdapter;

import java.util.ArrayList;

public class ShopsListActivity extends BaseNavigationActivity {

    private String[] navMenuTitles;
    private TypedArray navMenuIcons;


    ActionSlideExpandableListView list;
    CustomShopListAdapter adapter;
    public ShopsListActivity CustomListView = null;
    public ArrayList<Shop> CustomListViewValuesArr = new ArrayList<Shop>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_shops_list);

        /*navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load titles from strings.xml

        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);//load icons from strings.xml

        set(navMenuTitles, navMenuIcons);*/

        /*ButtonFloat createShopBtn = (ButtonFloat) findViewById(R.id.createShopBtn);
        createShopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ShopsListActivity.this, CreateShopActivity.class);
                
                startActivity(i);
            }
        });

        CustomListView = this;
<<<<<<< HEAD
        setListData(); //set data to list
=======

        *//******** Take some data in Arraylist ( CustomListViewValuesArr ) ***********//*
        setListData();
>>>>>>> 2a6427fa3673b25f0cb22770c2a97ef18dc4e0d6

        Resources res =getResources();
        list= (ListView)findViewById(R.id.shop_list);  // List defined in XML ( See Below )

        *//**************** Create Custom Adapter *********//*
        adapter=new CustomShopListAdapter( CustomListView, CustomListViewValuesArr,res );
        list.setAdapter(adapter);*/
    }

    public void setListData()
    {
        ShopModel.setOnSelectListShopResult(new ShopModel.OnSelectListShopResult() {
            @Override
            public void onSuccess(String data) {
                String[]datas = data.split("&"); //slit data to json struture
                Shop shop = null;
                for(int i=0 ;i<datas.length; i++){
                    shop = (Shop)Helper.jsonToObject(datas[i], Shop.class);
                    CustomListViewValuesArr.add(shop); //add shop object to array
                }
                Resources res =getResources();
                list= (ActionSlideExpandableListView)findViewById(R.id.shop_list);  // List defined in XML ( See Below )

                /**************** Create Custom Adapter *********/

                adapter=new CustomShopListAdapter( CustomListView, CustomListViewValuesArr,res );


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        list.setAdapter(new SlideExpandableListAdapter( //set adapter to list. it'will show to interface
                                adapter,
                                R.id.expandable_toggle_button,
                                R.id.expandable
                        ));

                        list.setItemActionListener(new ActionSlideExpandableListView.OnActionClickListener() {

                            @Override
                            public void onClick(View listView, View buttonview, int position) { //implement some operation when click the button of the card.

                                String actionName = "";
                                if(buttonview.getId()==R.id.details_button) {
                                    Intent i = new Intent(ShopsListActivity.this, ShopDetailActivity.class);
                                    i.putExtra("SHOP_ID", CustomListViewValuesArr.get(position).getId());
                                    startActivity(i);
                                } else {
                                    actionName = "edit button";
                                }
                                /**
                                 * For testing sake we just show a toast
                                 */
                                Toast.makeText(
                                        ShopsListActivity.this,
                                        "Clicked Action: "+actionName+" in list item "+position,
                                        Toast.LENGTH_SHORT
                                ).show();
                            }

                            // note that we also add 1 or more ids to the setItemActionListener
                            // this is needed in order for the listview to discover the buttons
                        }, R.id.details_button, R.id.edit_button);

                    }
                });

            }

            @Override
            public void onError(String error) {
                Log.e("error", error);
            }
        });
        ShopModel.getListShop(Global.userToken);
    }

    /*****************  This function used by adapter ****************/
//    public void onItemClick(int mPosition)
//    {
//        ShopCard tempValues = (ShopCard) CustomListViewValuesArr.get(mPosition);
//
//
//        // SHOW ALERT
//
//        Toast.makeText(CustomListView,
//                "" + tempValues.getShopname() + "Image:"+tempValues.getImg()+"Url:"+tempValues.getAddress(),Toast.LENGTH_LONG).show();
//    }

}
