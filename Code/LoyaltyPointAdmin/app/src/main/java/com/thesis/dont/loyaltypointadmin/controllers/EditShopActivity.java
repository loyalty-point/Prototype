package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Award;
import com.thesis.dont.loyaltypointadmin.models.Global;
import com.thesis.dont.loyaltypointadmin.models.Shop;
import com.thesis.dont.loyaltypointadmin.models.ShopModel;

import java.io.FileNotFoundException;

public class EditShopActivity extends ActionBarActivity {

    EditText mShopName, mPhone, mExchangeRatio, mAddress;
    ImageView shopLogoImgView;
    Spinner mCategory;
    ProgressDialog mDialog;

    ArrayAdapter<CharSequence> mCategoryAdapter;

    Bitmap shopLogo = null;

    private static final int SELECT_PHOTO = 100;

    Shop mOldShop;

    static Picasso mPicasso;

    boolean isChangeAwardImage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shop);

        mPicasso = Picasso.with(this);

        Intent i = getIntent();
        mOldShop = (Shop) i.getParcelableExtra(Global.SHOP_OBJECT);

        // init dialog
        mDialog = new ProgressDialog(this);
        mDialog.setTitle("Updating shop info");
        mDialog.setMessage("Please wait...");
        mDialog.setCancelable(false);

        
        // get references to layout components
        mCategory = (Spinner) findViewById(R.id.shopcategory);
        // Create an ArrayAdapter using the string array and a default spinner layout
        mCategoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.shopCategory, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        mCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mCategory.setAdapter(mCategoryAdapter);

        
        mShopName = (EditText) findViewById(R.id.awardName);
        mPhone = (EditText) findViewById(R.id.point);
        mExchangeRatio = (EditText) findViewById(R.id.quantity);
        mAddress = (EditText) findViewById(R.id.description);
        shopLogoImgView = (ImageView) findViewById(R.id.shopLogo);

        // set click listener for shopLogoImgView
        shopLogoImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditShopActivity.this, CropImageActivity.class);
                i.putExtra(CropImageActivity.ASPECT_RATIO_X, 1);
                i.putExtra(CropImageActivity.ASPECT_RATIO_Y, 1);
                startActivityForResult(i, SELECT_PHOTO);
            }
        });

        // Load thông tin hiện tại của shop
        
        mShopName.setText(mOldShop.getName());
        mPhone.setText(mOldShop.getPhone_number());
        mExchangeRatio.setText(String.valueOf(mOldShop.getExchange_ratio()));
        mAddress.setText(mOldShop.getAddress());
        mCategory.setSelection(mCategoryAdapter.getPosition(mOldShop.getCategory()));
        mPicasso.load(mOldShop.getImage()).placeholder(R.drawable.ic_store).into(shopLogoImgView);

        // cancel Button
        ButtonRectangle cancelBtn = (ButtonRectangle) findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // confirm Button
        ButtonRectangle confirmBtn = (ButtonRectangle) findViewById(R.id.confirmBtn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String shopname = mShopName.getText().toString();
                String phone = mPhone.getText().toString();
                String exchangeRatio = mExchangeRatio.getText().toString();
                String address = mAddress.getText().toString();
                String category = mCategory.getSelectedItem().toString();

                // Kiểm tra khác null
                if(Helper.checkNotNull(shopname, phone, exchangeRatio, address)) {
                    Toast.makeText(EditShopActivity.this, "please enter all the information", Toast.LENGTH_LONG).show();
                    return;
                }

                // Kiểm tra shopname hợp lệ
                if(Helper.checkShopName(shopname)) {
                    Toast.makeText(EditShopActivity.this, "shop name is not valid", Toast.LENGTH_LONG).show();
                    return;
                }

                // Đến đây thì thông tin người dùng nhập vào đã hoàn toàn hợp lệ
                // Gọi api để cập nhật thông tin shop
                // Show progress dialog
                mDialog.show();

                
                final Shop shop = new Shop(mOldShop.getId(), shopname, address, phone, category, Float.valueOf(exchangeRatio), null, null);
                ShopModel.setOnEditShopInfoResult(new ShopModel.OnEditShopInfoResult() {
                    @Override
                    public void onSuccess(final ShopModel.EditShopResult result) {

                        // Nếu người dùng không thay đổi ảnh thì không upload lên server
                        if(!isChangeAwardImage) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mDialog.dismiss();
                                }
                            });
                            finish();
                            return;
                        }

                        // edit shopLogo
                        
                        GCSHelper.uploadImage(EditShopActivity.this, result.bucketName, result.fileName, shopLogo, new GCSHelper.OnUploadImageResult() {
                            @Override
                            public void onComplete() {
                                isChangeAwardImage = false;

                                // dismiss Progress Dialog
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mDialog.dismiss();
                                    }
                                });

                                // clear cache on shopLogo
                                String imageLink = "http://storage.googleapis.com/" + result.bucketName + "/" + result.fileName;
                                mPicasso.invalidate(imageLink);

                                // Cập nhật shop thành công
                                
                                /*Intent i = new Intent(EditShopActivity.this, ShopsListActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                
                                startActivity(i);*/
                                finish();
                            }

                            @Override
                            public void onError(final String error) {
                                isChangeAwardImage = false;

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mDialog.dismiss();
                                        Toast.makeText(EditShopActivity.this, error, Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    
                    public void onError(final String e) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Cập nhật shop không thành công
                                mDialog.dismiss();
                                
                                Toast.makeText(EditShopActivity.this, e, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                
                ShopModel.editShop(Global.userToken, mOldShop.getId(), shop);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    isChangeAwardImage = true;

                    byte[] imageByteArray = imageReturnedIntent.getByteArrayExtra(CropImageActivity.CROPPED_IMAGE);

                    // không nén ảnh
                    shopLogo = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
                    shopLogoImgView.setImageBitmap(shopLogo);
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_shop, menu);
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
