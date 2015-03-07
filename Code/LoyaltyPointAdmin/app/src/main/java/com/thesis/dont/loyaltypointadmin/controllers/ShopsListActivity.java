package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.views.CircleButton;
import com.thesis.dont.loyaltypointadmin.views.ShopCard;

import java.util.ArrayList;

public class ShopsListActivity extends BaseActivity {

    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    ListView list;
    CustomShopListAdapter adapter;
    public ShopsListActivity CustomListView = null;
    public ArrayList<ShopCard> CustomListViewValuesArr = new ArrayList<ShopCard>();

    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shops_list);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            token = extras.getString("TOKEN");
        }

        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load titles from strings.xml

        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);//load icons from strings.xml

        set(navMenuTitles, navMenuIcons);

        CircleButton createShopBtn = (CircleButton) findViewById(R.id.createShopBtn);
        createShopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ShopsListActivity.this, CreateShopActivity.class);
                i.putExtra("TOKEN", token);
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

        for (int i = 0; i < 11; i++) {

            final ShopCard sched = new ShopCard();

            /******* Firstly take data in model object ******/
            sched.setShopname("Company "+i);
            sched.setImg("image"+i);
            sched.setAddress("http:\\www."+i+".com");

            /******** Take Model Object in ArrayList **********/
            CustomListViewValuesArr.add(sched);
        }

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
