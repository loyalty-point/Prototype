package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointadmin.models.Global;
import com.thesis.dont.loyaltypointadmin.models.ShopModel;

public class CardsListActivity extends BaseNavigationActivity {

    public static final int SELECT_PHOTO = 100;
    ProgressDialog mDialog;
    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(new CardsListMainFragment(), "CardsListMainFragment");
        mDialog = new ProgressDialog(this);
        mDialog.setTitle("Updating Card Background");
        mDialog.setMessage("Please wait...");
        mDialog.setCancelable(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SELECT_PHOTO: {
                if(resultCode == RESULT_OK){
                    mDialog.show();
                    Bundle bundle = data.getExtras();
                    String shopId = bundle.getString(CropImageActivity.SHOP_ID);
                    byte[] imageByteArray = data.getByteArrayExtra(CropImageActivity.CROPPED_IMAGE);
                    final Bitmap shopBackground = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
                    ShopModel.updateBackgroundShop(Global.userToken, shopId, new ShopModel.OnUpdateBackgroundResult() {
                        @Override
                        public void onSuccess(final ShopModel.UpdateBackgroundResult result) {
                            GCSHelper.uploadImage(CardsListActivity.this, result.bucketName, result.fileName, shopBackground, new GCSHelper.OnUploadImageResult() {
                                @Override
                                public void onComplete() {
                                    String imageLink = "http://storage.googleapis.com/" + result.bucketName + "/" + result.fileName;
                                    Picasso.with(CardsListActivity.this).invalidate(imageLink);
                                    finish();
                                    startActivity(getIntent());
                                    mDialog.dismiss();
                                }

                                @Override
                                public void onError(final String error) {
                                    Toast.makeText(CardsListActivity.this,"Can't update shop background", Toast.LENGTH_LONG).show();
                                    mDialog.dismiss();
                                }
                            });
                        }

                        @Override
                        public void onError(String error) {
                            Toast.makeText(CardsListActivity.this,"Can't update shop background", Toast.LENGTH_LONG).show();
                            mDialog.dismiss();
                        }
                    });
                }
                break;
            }
        }
    }

}
