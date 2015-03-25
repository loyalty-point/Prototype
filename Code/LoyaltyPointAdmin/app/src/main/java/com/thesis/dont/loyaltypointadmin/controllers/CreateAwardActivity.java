package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Award;
import com.thesis.dont.loyaltypointadmin.models.AwardModel;
import com.thesis.dont.loyaltypointadmin.models.Global;
import com.thesis.dont.loyaltypointadmin.models.Shop;
import com.thesis.dont.loyaltypointadmin.models.ShopModel;

import java.io.FileNotFoundException;

public class CreateAwardActivity extends ActionBarActivity {

    ImageView awardLogoImgView;
    EditText mAwardName, mPoint, mQuantity, mDescription;
    ProgressDialog mDialog;

    private static final int SELECT_PHOTO = 100;

    Bitmap awardLogo = null;

    String shopID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_award);

        Intent i = getIntent();
        shopID = i.getStringExtra(ShopAwardsFragment.SHOP_ID);

        // init dialog
        mDialog = new ProgressDialog(this);
        mDialog.setTitle("Uploading shop logo");
        mDialog.setMessage("Please wait...");
        mDialog.setCancelable(false);

        // get references to layout components
        mAwardName = (EditText) findViewById(R.id.awardName);
        mPoint = (EditText) findViewById(R.id.point);
        mQuantity = (EditText) findViewById(R.id.quantity);
        mDescription = (EditText) findViewById(R.id.description);

        // set click listener for awardLogoImgView
        awardLogoImgView = (ImageView) findViewById(R.id.awardLogo);
        awardLogoImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

        // set click listener for create button
        ButtonRectangle createAwardBtn = (ButtonRectangle) findViewById(R.id.confirmBtn);
        createAwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String awardName = mAwardName.getText().toString();
                final String point = mPoint.getText().toString();
                final String quantity = mQuantity.getText().toString();
                final String description = mDescription.getText().toString();

                // Kiểm tra khác null
                if(Helper.checkNotNull(awardName, point, quantity)) {
                    Toast.makeText(CreateAwardActivity.this, "please enter all the information", Toast.LENGTH_LONG).show();
                    return;
                }

                // Kiểm tra award name hợp lệ
                if(Helper.checkAwardName(awardName)) {
                    Toast.makeText(CreateAwardActivity.this, "shop name is not valid", Toast.LENGTH_LONG).show();
                    return;
                }

                // Đến đây thì thông tin người dùng nhập vào đã hoàn toàn hợp lệ
                // Gọi api để tạo award

                // Show progress dialog
                mDialog.show();

                // Create award
                Award award = new Award(null, awardName, Integer.valueOf(point), Integer.valueOf(quantity), description, null, shopID);
                AwardModel.createAward(Global.userToken, award, new AwardModel.OnCreateAwardResult() {
                    @Override
                    public void onSuccess(AwardModel.CreateAwardResult result) {
                        // Tạo award thành công

                        // Upload ảnh của award lên server
                        if(awardLogo!=null) {
                            GCSHelper.uploadImage(CreateAwardActivity.this, result.bucketName, result.fileName, awardLogo, new GCSHelper.OnUploadImageResult() {
                                @Override
                                public void onComplete() {

                                    // dismiss Progress Dialog
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mDialog.dismiss();
                                        }
                                    });

                                    finish();
                                }

                                @Override
                                public void onError(final String error) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mDialog.dismiss();
                                            Toast.makeText(CreateAwardActivity.this, error, Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            });
                        }else{
                            finish();
                        }
                    }

                    @Override
                    public void onError(final String error) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Tạo award không thành công
                                mDialog.dismiss();
                                Toast.makeText(CreateAwardActivity.this, error, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });

            }
        });


        // set click listener for cancel button
        ButtonRectangle cancelBtn = (ButtonRectangle) findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i = new Intent(CreateAwardActivity.this, Shops.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);*/
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        //super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();

                    // nén ảnh
                    try {
                        awardLogo = Helper.decodeUri(this, selectedImage);
                        awardLogoImgView.setImageBitmap(awardLogo);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_award, menu);
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
