package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFloat;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Global;
import com.thesis.dont.loyaltypointadmin.models.Shop;
import com.thesis.dont.loyaltypointadmin.models.ShopModel;
import com.tjerkw.slideexpandable.library.ActionSlideExpandableListView;
import com.tjerkw.slideexpandable.library.SlideExpandableListAdapter;

import java.util.ArrayList;


/**
 * Created by 11120_000 on 10/03/15.
 */
public class ShopsListMainFragment extends Fragment {

    ActionSlideExpandableListView list;
    CustomShopListAdapter adapter;
    public ShopsListActivity CustomListView = null;
    public ArrayList<Shop> CustomListViewValuesArr = new ArrayList<Shop>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.shops_list_main_fragment, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity activity = getActivity();
        ButtonFloat createShopBtn = (ButtonFloat) activity.findViewById(R.id.createShopBtn);
        createShopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CreateShopActivity.class);

                startActivity(i);
            }
        });

        CustomListView = (ShopsListActivity) getActivity();
        setListData(); //set data to list

        Resources res =getResources();
        list= (ActionSlideExpandableListView) getActivity().findViewById(R.id.shop_list);  // List defined in XML ( See Below )


        adapter=new CustomShopListAdapter( CustomListView, CustomListViewValuesArr,res );
        list.setAdapter(adapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setListData()
    {
        ShopModel.setOnSelectListShopResult(new ShopModel.OnSelectListShopResult() {
            @Override
            public void onSuccess(String data) {
                String[] datas = data.split("&"); //slit data to json struture
                Shop shop = null;
                for (int i = 0; i < datas.length; i++) {
                    shop = (Shop) Helper.jsonToObject(datas[i], Shop.class);
                    CustomListViewValuesArr.add(shop); //add shop object to array
                }
                Resources res = getResources();
                list = (ActionSlideExpandableListView) getActivity().findViewById(R.id.shop_list);  // List defined in XML ( See Below )

                /**************** Create Custom Adapter *********/

                adapter = new CustomShopListAdapter(CustomListView, CustomListViewValuesArr, res);


                getActivity().runOnUiThread(new Runnable() {
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
                                if (buttonview.getId() == R.id.details_button) {
                                    Intent i = new Intent(getActivity(), ShopDetailActivity.class);
                                    i.putExtra("SHOP_ID", CustomListViewValuesArr.get(position).getId());
                                    startActivity(i);
                                } else {
                                    actionName = "edit button";
                                }
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
//        ShopModel.getListShop(Global.userToken);
        ShopModel.getListShop("4f9aab34a15368a50069cde837365ebc6e6ace46c169880a2ffad300d40e7edf");
    }
}
