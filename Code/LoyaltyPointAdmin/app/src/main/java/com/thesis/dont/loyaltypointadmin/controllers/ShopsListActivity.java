package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidviewhover.BlurLayout;
import com.gc.materialdesign.views.ButtonFloat;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Shop;
import com.thesis.dont.loyaltypointadmin.models.ShopModel;
import com.thesis.dont.loyaltypointadmin.views.CircleButton;
import com.thesis.dont.loyaltypointadmin.views.ShopCard;

import java.util.ArrayList;
import java.util.List;

public class ShopsListActivity extends BaseActivity {

    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

  
    ListView list;
    CustomShopListAdapter adapter;
    public ShopsListActivity CustomListView = null;
    public ArrayList<ShopCard> CustomListViewValuesArr = new ArrayList<ShopCard>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shops_list);

        /*Bundle extras = getIntent().getExtras();
        if (extras != null) {
            token = extras.getString("TOKEN");
        }*/

        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load titles from strings.xml

        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);//load icons from strings.xml

        set(navMenuTitles, navMenuIcons);

        ButtonFloat createShopBtn = (ButtonFloat) findViewById(R.id.createShopBtn);
        createShopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ShopsListActivity.this, CreateShopActivity.class);
                /*i.putExtra("TOKEN", token);*/
                startActivity(i);
            }
        });

        CustomListView = this;

        /******** Take some data in Arraylist ( CustomListViewValuesArr ) ***********/
        setListData();

        Resources res =getResources();
        list= (ListView)findViewById(R.id.shop_list);  // List defined in XML ( See Below )

        /**************** Create Custom Adapter *********/
        adapter=new CustomShopListAdapter( CustomListView, CustomListViewValuesArr,res );
        list.setAdapter(adapter);
    }

    public void setListData()
    {
        ShopModel.setOnSelectListShopResult(new ShopModel.OnSelectListShopResult() {
            @Override
            public void onSuccess(String data) {
                String[]datas = data.split("&");
                ArrayList<Shop> shop_list = new ArrayList<Shop>();
                ShopCard shop_card = new ShopCard();
                for(int i=0 ;i<datas.length; i++){
                    shop_list.add((Shop)Helper.jsonToObject(datas[i], Shop.class));
                    shop_card.setShopname(shop_list.get(i).getName());
                    shop_card.setImg(shop_list.get(i).getImage());
                    shop_card.setAddress(shop_list.get(i).getAddress());
                    CustomListViewValuesArr.add(shop_card);
                }
            }

            @Override
            public void onError(String error) {
                Log.e("error", error);
            }
        });
        ShopModel.getListShop("94472030f34f2432fe34b0c322126828fc3cde5a630816bef70392664b6a6372");
    }


    /*****************  This function used by adapter ****************/
    public void onItemClick(int mPosition)
    {
        ShopCard tempValues = (ShopCard) CustomListViewValuesArr.get(mPosition);


        // SHOW ALERT

        Toast.makeText(CustomListView,
                "" + tempValues.getShopname() + "Image:"+tempValues.getImg()+"Url:"+tempValues.getAddress(),Toast.LENGTH_LONG).show();
    }

}
