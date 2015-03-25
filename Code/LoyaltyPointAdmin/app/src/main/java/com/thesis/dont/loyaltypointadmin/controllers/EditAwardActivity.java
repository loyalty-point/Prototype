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
import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Award;
import com.thesis.dont.loyaltypointadmin.models.AwardModel;
import com.thesis.dont.loyaltypointadmin.models.Global;
import com.thesis.dont.loyaltypointadmin.models.Shop;
import com.thesis.dont.loyaltypointadmin.models.ShopModel;

import java.io.FileNotFoundException;

public class EditAwardActivity extends ActionBarActivity {

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

                // Kiểm tra khác null
                if(Helper.checkNotNull(awardName, point, quantity)) {
                    Toast.makeText(EditAwardActivity.this, "please enter all the information", Toast.LENGTH_LONG).show();
                    return;
                }

                // Kiểm tra award name hợp lệ
                if(Helper.checkAwardName(awardName)) {
                    Toast.makeText(EditAwardActivity.this, "shop name is not valid", Toast.LENGTH_LONG).show();
                    return;
                }

                // Đến đây thì thông tin người dùng nhập vào đã hoàn toàn hợp lệ
                // Gọi api để edit award

                // Show progress dialog
                mDialog.show();

                // Edit award
                Award award = new Award(mOldAward.getID(), awardName, Integer.valueOf(point), Integer.valueOf(quantity), description, null, mOldAward.getShopID());
                AwardModel.editAward(Global.userToken, award, new AwardModel.OnEditAwardResult() {
                    @Override
                    public void onSuccess(final AwardModel.EditAwardResult result) {
                        // Sửa award thành công

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

                        // Upload ảnh của award lên server
                        GCSHelper.uploadImage(EditAwardActivity.this, result.bucketName, result.fileName, awardLogo, new GCSHelper.OnUploadImageResult() {
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
                                        Toast.makeText(EditAwardActivity.this, error, Toast.LENGTH_LONG).show();
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
                                // sửa award không thành công
                                mDialog.dismiss();
                                Toast.makeText(EditAwardActivity.this, error, Toast.LENGTH_LONG).show();
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
                        isChangeAwardImage = true;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    /*// không nén ảnh
                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    shopLogo = BitmapFactory.decodeStream(imageStream);
                    shopLogoImgView.setImageBitmap(shopLogo);*/
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
