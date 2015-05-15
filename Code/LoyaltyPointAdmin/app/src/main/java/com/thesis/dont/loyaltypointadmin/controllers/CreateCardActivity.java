package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
import com.thesis.dont.loyaltypointadmin.models.Card;
import com.thesis.dont.loyaltypointadmin.models.CardModel;
import com.thesis.dont.loyaltypointadmin.models.Global;
import com.thesis.dont.loyaltypointadmin.models.Shop;
import com.thesis.dont.loyaltypointadmin.models.ShopModel;
import com.thesis.dont.loyaltypointadmin.views.MyRoundedImageView;

public class CreateCardActivity extends ActionBarActivity {

    MyRoundedImageView cardImage;
    Bitmap cardBackgroud = null;
    EditText cardName;
    ProgressDialog mDialog;
    ButtonRectangle cancelBtn, createBtn;
    static Picasso mPicasso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_card);

        mPicasso = Picasso.with(this);
        mDialog = new ProgressDialog(this);
        mDialog.setTitle("Updating Card Background");
        mDialog.setMessage("Please wait...");
        mDialog.setCancelable(false);

        cardImage = (MyRoundedImageView) findViewById(R.id.cardBackground);
        cardName = (EditText) findViewById(R.id.cardName);
        cancelBtn = (ButtonRectangle) findViewById(R.id.cancelBtn);
        createBtn = (ButtonRectangle) findViewById(R.id.confirmBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardName.getText().toString().equals("")) {
                    Toast.makeText(CreateCardActivity.this, "input card name pls!", Toast.LENGTH_LONG).show();
                } else {
                    if(cardImage.getDrawable()!= null)
                        cardBackgroud = ((BitmapDrawable)cardImage.getDrawable()).getBitmap();
                    Card card = new Card(cardName.getText().toString(), "");
                    CardModel.createCard(Global.userToken, card, new CardModel.OnCreateCardResult() {
                        @Override
                        public void onSuccess(final CardModel.CreateCardResult result) {
                            //update anh background card
                            if(!cardBackgroud.equals(null)) {
                                GCSHelper.uploadImage(CreateCardActivity.this, result.bucketName, result.fileName, cardBackgroud, new GCSHelper.OnUploadImageResult() {
                                    @Override
                                    public void onComplete() {
                                        String imageLink = "http://storage.googleapis.com/" + result.bucketName + "/" + result.fileName;
                                        mPicasso.invalidate(imageLink);
                                        finish();
                                    }

                                    @Override
                                    public void onError(final String error) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(CreateCardActivity.this, error, Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                });
                            }
                        }

                        @Override
                        public void onError(final String error) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(CreateCardActivity.this, error, Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                }
            }
        });

        cardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreateCardActivity.this, CropImageActivity.class);
                i.putExtra(CropImageActivity.ASPECT_RATIO_Y, 610);
                i.putExtra(CropImageActivity.ASPECT_RATIO_X, 948);
                ((Activity) CreateCardActivity.this).startActivityForResult(i, Global.SELECT_PHOTO);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Global.SELECT_PHOTO: {
                if (resultCode == RESULT_OK) {
                    mDialog.show();
                    Bundle bundle = data.getExtras();
                    byte[] imageByteArray = data.getByteArrayExtra(CropImageActivity.CROPPED_IMAGE);
                    final Bitmap cardBackground = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
                    BitmapDrawable ob = new BitmapDrawable(getResources(), cardBackground);
                    cardImage.setImageDrawable(ob);
                    mDialog.dismiss();
                }
                break;
            }
        }
    }

}
