package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Global;
import com.thesis.dont.loyaltypointadmin.models.Shop;

public class CreateShopActivity extends ActionBarActivity {

    ImageView shopLogoImgView;
    EditText mShopName, mPhone, mExchangeRatio, mAddress;
    Spinner mCategory;
    CheckBox mAgreeTerm;
    ProgressDialog mDialog;

    public static final int SELECT_PHOTO = 100;

    Bitmap shopLogo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shop);

        // init dialog
        mDialog = new ProgressDialog(this);
        mDialog.setTitle("Creating shop");
        mDialog.setMessage("Please wait...");
        mDialog.setCancelable(false);

        // init category combobox
        mCategory = (Spinner) findViewById(R.id.shopcategory);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.shopCategory, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mCategory.setAdapter(adapter);

        // get references to layout components
        mShopName = (EditText) findViewById(R.id.awardName);
        mPhone = (EditText) findViewById(R.id.point);
        mExchangeRatio = (EditText) findViewById(R.id.quantity);
        mAddress = (EditText) findViewById(R.id.description);
        mAgreeTerm = (CheckBox) findViewById(R.id.agreeTerm);

        final ButtonRectangle createShopBtn = (ButtonRectangle) findViewById(R.id.confirmBtn);
        ButtonRectangle cancelBtn = (ButtonRectangle) findViewById(R.id.cancelBtn);

        if(mAgreeTerm.isChecked()) {
            createShopBtn.setEnabled(true);
            createShopBtn.setBackgroundColor(getResources().getColor(R.color.AccentColor));
        }else {
            createShopBtn.setEnabled(false);
            createShopBtn.setBackgroundColor(getResources().getColor(R.color.MaterialDisable));
        }
        mAgreeTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAgreeTerm.isChecked()) {
                    createShopBtn.setEnabled(true);
                    createShopBtn.setBackgroundColor(getResources().getColor(R.color.AccentColor));
                }
                else {
                    createShopBtn.setEnabled(false);
                    createShopBtn.setBackgroundColor(getResources().getColor(R.color.MaterialDisable));
                }
            }
        });

        // set click listener for shopLogoImgView
        shopLogoImgView = (ImageView) findViewById(R.id.shopLogo);
        shopLogoImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(CreateShopActivity.this, CropImageActivity.class);
                i.putExtra(CropImageActivity.ASPECT_RATIO_X, 1);
                i.putExtra(CropImageActivity.ASPECT_RATIO_Y, 1);
                startActivityForResult(i, SELECT_PHOTO);
            }
        });

        createShopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String shopname = mShopName.getText().toString();
                final String phone = mPhone.getText().toString();
                final String exchangeRatio = mExchangeRatio.getText().toString();
                final String address = mAddress.getText().toString();
                final String category = mCategory.getSelectedItem().toString();
                Bitmap image = null;
                if(shopLogoImgView.getDrawable() != null)
                    image = ((BitmapDrawable)shopLogoImgView.getDrawable()).getBitmap();

                // Kiểm tra khác null
                if(Helper.checkNotNull(shopname, phone, exchangeRatio, address)) {
                    Toast.makeText(CreateShopActivity.this, "please enter all the information", Toast.LENGTH_LONG).show();
                    return;
                }

                // Kiểm tra shopname hợp lệ
                if(Helper.checkShopName(shopname)) {
                    Toast.makeText(CreateShopActivity.this, "shop name is not valid", Toast.LENGTH_LONG).show();
                    return;
                }
                Shop shop = new Shop(null, shopname, address, phone, category, Float.valueOf(exchangeRatio), null, null);
                Intent i = new Intent(CreateShopActivity.this,SearchCard.class);
                i.putExtra(Global.SHOP_OBJECT, shop);
                i.putExtra(Global.OBJECT, image);
                Global.tempActivity = CreateShopActivity.this;
                startActivity(i);

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreateShopActivity.this, CardsListActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    byte[] imageByteArray = imageReturnedIntent.getByteArrayExtra(CropImageActivity.CROPPED_IMAGE);

                    // không nén ảnh
                    shopLogo = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
                    shopLogoImgView.setImageBitmap(shopLogo);
                }
        }
    }
}
