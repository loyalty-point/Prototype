package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Award;
import com.thesis.dont.loyaltypointadmin.models.AwardModel;
import com.thesis.dont.loyaltypointadmin.models.Global;

public class CardCreateAwardActivity extends ActionBarActivity {

    ImageView awardLogoImgView;
    EditText mAwardName, mPoint, mQuantity, mDescription;;

    private static final int SELECT_PHOTO = 100;

    Bitmap awardLogo = null;

    String shopID;
    String cardID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_create_award);

        Intent i = getIntent();
        shopID = i.getStringExtra(ShopAwardsFragment.SHOP_ID);
        cardID = i.getStringExtra(Global.CARD_ID);

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
                Intent i = new Intent(CardCreateAwardActivity.this, CropImageActivity.class);
                i.putExtra(CropImageActivity.ASPECT_RATIO_X, 1);
                i.putExtra(CropImageActivity.ASPECT_RATIO_Y, 1);
                startActivityForResult(i, SELECT_PHOTO);
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

                // Ki?m tra khác null
                if(Helper.checkNotNull(awardName, point, quantity)) {
                    Toast.makeText(CardCreateAwardActivity.this, "please enter all the information", Toast.LENGTH_LONG).show();
                    return;
                }

                // Ki?m tra award name h?p l?
                if(Helper.checkAwardName(awardName)) {
                    Toast.makeText(CardCreateAwardActivity.this, "shop name is not valid", Toast.LENGTH_LONG).show();
                    return;
                }

                // ??n ?ây thì thông tin ng??i dùng nh?p vào ?ã hoàn toàn h?p l?
                // G?i api ?? t?o award

                // Create award
                Award award = new Award(null, awardName, Integer.valueOf(point), Integer.valueOf(quantity), description, null, shopID);
//                AwardModel.createAward(Global.userToken, shopID, cardID, award, new AwardModel.OnCreateAwardResult() {
//                    @Override
//                    public void onSuccess(AwardModel.CreateAwardResult result) {
//                        // T?o award thành công
//
//                        // Upload ?nh c?a award lên server
//                        if (awardLogo != null) {
//                            GCSHelper.uploadImage(CardCreateAwardActivity.this, result.bucketName, result.fileName, awardLogo, new GCSHelper.OnUploadImageResult() {
//                                @Override
//                                public void onComplete() {
//
//                                    // dismiss Progress Dialog
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            mDialog.dismiss();
//                                        }
//                                    });
//                                    finish();
//                                }
//
//                                @Override
//                                public void onError(final String error) {
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            mDialog.dismiss();
//                                            Toast.makeText(CreateAwardActivity.this, error, Toast.LENGTH_LONG).show();
//                                        }
//                                    });
//                                }
//                            });
//                        } else {
//                            finish();
//                        }
//                    }
//
//                    @Override
//                    public void onError(final String error) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                // T?o award không thành công
//                                mDialog.dismiss();
//                                Toast.makeText(CreateAwardActivity.this, error, Toast.LENGTH_LONG).show();
//                            }
//                        });
//                    }
//                });

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

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    byte[] imageByteArray = imageReturnedIntent.getByteArrayExtra(CropImageActivity.CROPPED_IMAGE);

                    // không nén ?nh
                    awardLogo = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
                    awardLogoImgView.setImageBitmap(awardLogo);
                }
        }
    }
}
