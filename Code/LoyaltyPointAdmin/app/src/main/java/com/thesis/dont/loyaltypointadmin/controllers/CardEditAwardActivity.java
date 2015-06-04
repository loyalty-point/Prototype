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
import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Award;
import com.thesis.dont.loyaltypointadmin.models.AwardModel;
import com.thesis.dont.loyaltypointadmin.models.Global;

public class CardEditAwardActivity extends ActionBarActivity {

    ImageView awardLogoImgView;
    EditText mAwardName, mPoint, mQuantity, mDescription;
    ProgressDialog mDialog;

    private static final int SELECT_PHOTO = 100;

    Bitmap awardLogo = null;

    Award mOldAward;

    static Picasso mPicasso;

    boolean isChangeAwardImage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_award);

        mPicasso = Picasso.with(this);

        Intent i = getIntent();
        mOldAward = (Award) i.getParcelableExtra(ShopAwardsFragment.AWARD_OBJECT);

        // init dialog
        mDialog = new ProgressDialog(this);
        mDialog.setTitle("Updating award info");
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
                Intent i = new Intent(CardEditAwardActivity.this, CropImageActivity.class);
                i.putExtra(CropImageActivity.ASPECT_RATIO_X, 1);
                i.putExtra(CropImageActivity.ASPECT_RATIO_Y, 1);
                startActivityForResult(i, SELECT_PHOTO);
            }
        });

        // load data lên UI components
        mAwardName.setText(mOldAward.getName());
        mPoint.setText(String.valueOf(mOldAward.getPoint()));
        mQuantity.setText(String.valueOf(mOldAward.getQuantity()));
        mDescription.setText(mOldAward.getDescription());
        mPicasso.load(mOldAward.getImage()).placeholder(R.drawable.ic_award).into(awardLogoImgView);

        // set click listener for create button
        ButtonRectangle editAwardBtn = (ButtonRectangle) findViewById(R.id.confirmBtn);
        editAwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String awardName = mAwardName.getText().toString();
                final String point = mPoint.getText().toString();
                final String quantity = mQuantity.getText().toString();
                final String description = mDescription.getText().toString();

                // Ki?m tra khác null
                if(Helper.checkNotNull(awardName, point, quantity)) {
                    Toast.makeText(CardEditAwardActivity.this, "please enter all the information", Toast.LENGTH_LONG).show();
                    return;
                }

                // Ki?m tra award name h?p l?
                if(Helper.checkAwardName(awardName)) {
                    Toast.makeText(CardEditAwardActivity.this, "shop name is not valid", Toast.LENGTH_LONG).show();
                    return;
                }

                // ??n ?ây thì thông tin ng??i dùng nh?p vào ?ã hoàn toàn h?p l?
                // G?i api ?? edit award

                // Show progress dialog
                mDialog.show();

                // Edit award
                Award award = new Award(mOldAward.getID(), awardName, Integer.valueOf(point), Integer.valueOf(quantity), description, null, mOldAward.getShopID());
                AwardModel.editAward(Global.userToken, award, new AwardModel.OnEditAwardResult() {
                    @Override
                    public void onSuccess(final AwardModel.EditAwardResult result) {
                        // S?a award thành công

                        // N?u ng??i dùng không thay ??i ?nh thì không upload lên server
                        if (!isChangeAwardImage) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mDialog.dismiss();
                                }
                            });
                            finish();
                            return;
                        }

                        // Upload ?nh c?a award lên server
                        GCSHelper.uploadImage(CardEditAwardActivity.this, result.bucketName, result.fileName, awardLogo, new GCSHelper.OnUploadImageResult() {
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

                                /*Intent i = new Intent(EditAwardActivity.this, ShopDetailActivity.class);
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
                                        Toast.makeText(CardEditAwardActivity.this, error, Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void onError(final String error) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // s?a award không thành công
                                mDialog.dismiss();
                                Toast.makeText(CardEditAwardActivity.this, error, Toast.LENGTH_LONG).show();
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
        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    byte[] imageByteArray = imageReturnedIntent.getByteArrayExtra(CropImageActivity.CROPPED_IMAGE);

                    // không nén ?nh
                    awardLogo = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
                    awardLogoImgView.setImageBitmap(awardLogo);

                    isChangeAwardImage = true;
                }
        }
    }
}
