package com.thesis.dont.loyaltypointadmin.controllers;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Global;
import com.thesis.dont.loyaltypointadmin.models.Product;
import com.thesis.dont.loyaltypointadmin.models.User;

import java.util.ArrayList;

import me.dm7.barcodescanner.zbar.Result;

public class CalculatePointActivity extends ActionBarActivity {

    User mUser;

    ImageView mUserAvatar;
    TextView mFullName, mPhone;

    static Picasso mPicasso;

    ListView mListView;
    ListProductsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_point);

        mPicasso = Picasso.with(this);

        Intent i = getIntent();
        mUser = (User) i.getParcelableExtra(Global.USER_OBJECT);

        // Set user info to layout
        /*mUserAvatar = (ImageView) findViewById(R.id.userAvatarImgView);
        mFullName = (TextView) findViewById(R.id.fullname);
        mPhone = (TextView) findViewById(R.id.phone);*/

        //mPicasso.load(mUser.getAvatar()).placeholder(R.drawable.user_avatar).into(mUserAvatar);
        //mPicasso.load("").placeholder(R.drawable.user_avatar).into(mUserAvatar);
        /*mFullName.setText(mUser.getFullname());
        mPhone.setText(mUser.getPhone());*/

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.barcodeScannerLayout, new ScannerFragment());
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();

        // init List Products
        mListView = (ListView) findViewById(R.id.listProducts);
        mAdapter = new ListProductsAdapter(this, new ArrayList<Product>());
        mListView.setAdapter(mAdapter);
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
