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

    String cardID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_create_award);

        Intent i = getIntent();
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
        ButtonRectangle nextAwardBtn = (ButtonRectangle) findViewById(R.id.nextBtn);
        nextAwardBtn.setOnClickListener(new View.OnClickListener() {
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

                // Create award
                Award award = new Award(null, awardName, Integer.valueOf(point), Integer.valueOf(quantity), description, null, "");

                Intent i = new Intent(CardCreateAwardActivity.this, CardShopListApplyAwardActivity.class);
                Global.tempBitmap = awardLogo;
                i.putExtra(Global.AWARD_OBJECT, award);
                i.putExtra(Global.CARD_ID, cardID);
                i.putExtra(Global.AWARD_LIST_TYPE, Global.CARD_CREATE_AWARD_LIST);
                Global.tempActivity = CardCreateAwardActivity.this;
                startActivity(i);
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
