package com.thesis.dont.loyaltypointadmin.controllers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFloat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Global;
import com.thesis.dont.loyaltypointadmin.models.Product;
import com.thesis.dont.loyaltypointadmin.models.Shop;
import com.thesis.dont.loyaltypointadmin.models.User;

import java.util.ArrayList;

public class ConfirmUpdatePointActivity extends ActionBarActivity {

    User mUser;
    Shop mShop;
    int totalMoney;
    ArrayList<Product> mProducts;

    // User info
    ImageView mUserAvatar;
    TextView mFullName, mPhone;

    // Bill info
    EditText mBillCode;
    ImageView mBillImage;

    static Picasso mPicasso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_update_point);

        mPicasso = Picasso.with(this);

        // get data
        Intent i = getIntent();
        mUser = (User) i.getParcelableExtra(Global.USER_OBJECT);
        mShop = (Shop) i.getParcelableExtra(Global.SHOP_OBJECT);
        totalMoney = i.getIntExtra(Global.TOTAL_MONEY, 0);
        mProducts = i.getParcelableArrayListExtra(Global.PRODUCT_LIST);

        // set user info to layout
        mUserAvatar = (ImageView) findViewById(R.id.userAvatarImgView);
        mFullName = (TextView) findViewById(R.id.fullname);
        mPhone = (TextView) findViewById(R.id.phone);

        mPicasso.load(mUser.getAvatar()).placeholder(R.drawable.user_avatar).into(mUserAvatar);
        mFullName.setText(mUser.getFullname());
        mPhone.setText(mUser.getPhone());

        // get references to another view components
        mBillImage = (ImageView) findViewById(R.id.billImage);
        mBillCode = (EditText) findViewById(R.id.billCode);

        // set listener for buttons
        ButtonRectangle backBtn = (ButtonRectangle) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ButtonRectangle confirmBtn = (ButtonRectangle) findViewById(R.id.confirmBtn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call API to update point
                //ConfirmUpdatePointActivity.this.getbackst
                Intent i = new Intent(ConfirmUpdatePointActivity.this, ShopDetailActivity.class);
                i.putExtra(Global.SHOP_OBJECT, mShop);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });

        ButtonFloat captureBtn = (ButtonFloat) findViewById(R.id.captureBtn);
        captureBtn.setBackgroundColor(getResources().getColor(R.color.PrimaryColor));
        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, Global.CAMERA_REQUEST);
            }
        });

        // call calculate point API

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {

        switch(requestCode) {
            case Global.CAMERA_REQUEST:
                if(resultCode == RESULT_OK){
                    Bitmap photo = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    mBillImage.setImageBitmap(photo);
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_confirm_update_point, menu);
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
