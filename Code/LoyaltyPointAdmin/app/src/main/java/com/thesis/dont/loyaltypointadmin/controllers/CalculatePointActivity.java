package com.thesis.dont.loyaltypointadmin.controllers;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonRectangle;
import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Global;
import com.thesis.dont.loyaltypointadmin.models.Product;
import com.thesis.dont.loyaltypointadmin.models.Shop;
import com.thesis.dont.loyaltypointadmin.models.User;

import java.util.ArrayList;

import me.dm7.barcodescanner.zbar.Result;

public class CalculatePointActivity extends ActionBarActivity {

    User mUser;
    Shop mShop;

    ListView mListView;
    ListProductsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_point);

        Intent i = getIntent();
        mUser = (User) i.getParcelableExtra(Global.USER_OBJECT);
        mShop = (Shop) i.getParcelableExtra(Global.SHOP_OBJECT);

        // setup fragment
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.barcodeScannerLayout, new ScannerFragment());
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();

        // init List Products
        mListView = (ListView) findViewById(R.id.listProducts);
        mAdapter = new ListProductsAdapter(this, new ArrayList<Product>());
        mListView.setAdapter(mAdapter);

        // set listeners for buttons
        ButtonRectangle cancelBtn = (ButtonRectangle) findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ButtonRectangle nextBtn = (ButtonRectangle) findViewById(R.id.registerBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CalculatePointActivity.this, ConfirmUpdatePointActivity.class);

                // put user into intent
                i.putExtra(Global.USER_OBJECT, mUser);

                // put shop into intent
                i.putExtra(Global.SHOP_OBJECT, mShop);

                // put total money into intent
                EditText totalMoneyEditText = (EditText) findViewById(R.id.totalMoney);
                int totalMoney = 0;
                if(!totalMoneyEditText.getText().equals("")) {
                    totalMoney = Integer.valueOf(totalMoneyEditText.getText().toString());
                }
                i.putExtra(Global.TOTAL_MONEY, totalMoney);

                // put list products into intent
                //i.putExtra(Global.PRODUCT_LIST, mAdapter.getListProducts());
                i.putParcelableArrayListExtra(Global.PRODUCT_LIST, mAdapter.getListProducts());

                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calculate_point, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void handleScanResult(Result result, int quantity) {
        Log.e("Scan Result", result.getContents());
        Product product = new Product(quantity, result.getContents(), "Product Name");
        mAdapter.add(product);
        mAdapter.notifyDataSetChanged();
    }
}
