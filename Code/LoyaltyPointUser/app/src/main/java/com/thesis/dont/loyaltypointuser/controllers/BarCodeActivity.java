package com.thesis.dont.loyaltypointuser.controllers;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.thesis.dont.loyaltypointuser.R;
import com.thesis.dont.loyaltypointuser.models.Global;
import com.thesis.dont.loyaltypointuser.models.User;
import com.thesis.dont.loyaltypointuser.models.UserModel;

public class BarCodeActivity extends BaseNavigationActivity {

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(new AccountFragment(), "AccountFragment");

        setDefaultSectionLoaded(1);
    }

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code);

        barcodeImgView = (ImageView) findViewById(R.id.barcodeImgView);

        // Get user info
        UserModel.getUserInfo(Global.userToken, new UserModel.OnGetUserInfoResult() {
            @Override
            public void onSuccess(User user) {
                // Generate ra ảnh barcode
                final Bitmap barcodeImg = Helper.generateBarCodeImage(user.getBarcode());

                // Show lên màn hình
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        barcodeImgView.setImageBitmap(barcodeImg);
                    }
                });
            }

            @Override
            public void onError(final String e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BarCodeActivity.this, e, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bar_code, menu);
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
}
