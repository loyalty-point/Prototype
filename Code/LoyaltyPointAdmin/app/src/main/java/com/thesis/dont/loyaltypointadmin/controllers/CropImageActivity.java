package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.edmodo.cropper.CropImageView;
import com.gc.materialdesign.views.ButtonFloat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.thesis.dont.loyaltypointadmin.R;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class CropImageActivity extends Activity {

    public static final String ASPECT_RATIO_X = "ASPECT_RATIO_X";
    public static final String ASPECT_RATIO_Y = "ASPECT_RATIO_Y";
    public static String CROPPED_IMAGE = "cropped_image";
    public static String SHOP_ID = "shop_id";

    // Static final constants
    private static final int ROTATE_NINETY_DEGREES = 90;
    private static final int ON_TOUCH = 1;

    // Instance variables
    private int mAspectRatioX;
    private int mAspectRatioY;

    Bitmap croppedImage;
    CropImageView cropImageView;
    String shopId;
    ProgressDialog mDialog;

    // Saves the state upon rotating the screen/restarting the activity
    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(ASPECT_RATIO_X, mAspectRatioX);
        bundle.putInt(ASPECT_RATIO_Y, mAspectRatioY);
    }

    // Restores the state upon rotating the screen/restarting the activity
    @Override
    protected void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        mAspectRatioX = bundle.getInt(ASPECT_RATIO_X);
        mAspectRatioY = bundle.getInt(ASPECT_RATIO_Y);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);

        // init dialog
        mDialog = new ProgressDialog(this);
        mDialog.setTitle("Cropping Your Image");
        mDialog.setMessage("Please wait...");
        mDialog.setCancelable(false);

        // get aspect ratio from Intent
        Intent i = getIntent();
        mAspectRatioX = i.getIntExtra(ASPECT_RATIO_X, 1);
        mAspectRatioY = i.getIntExtra(ASPECT_RATIO_Y, 1);
        shopId = i.getStringExtra(SHOP_ID);

        // Initialize components of the app
        cropImageView = (CropImageView) findViewById(R.id.CropImageView);
        cropImageView.setAspectRatio(mAspectRatioX, mAspectRatioY);
        cropImageView.setFixedAspectRatio(true);

        //Sets the rotate button
        final ButtonFloat rotateButton = (ButtonFloat) findViewById(R.id.Button_rotate);
        rotateButton.setBackgroundColor(getResources().getColor(R.color.MaterialGrey));
        rotateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cropImageView.rotateImage(ROTATE_NINETY_DEGREES);
            }
        });

        final ButtonRectangle cropButton = (ButtonRectangle) findViewById(R.id.cropBtn);
        cropButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mDialog.show();

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        croppedImage = cropImageView.getCroppedImage();
                        croppedImage = Helper.resizeBitmap(CropImageActivity.this, croppedImage);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mDialog.dismiss();

                                Intent resultIntent = new Intent();
                                resultIntent.putExtra(CROPPED_IMAGE, Helper.BitmapToByteArray(croppedImage));
                                if(shopId != null){
                                    resultIntent.putExtra(SHOP_ID, shopId);
                                }
                                setResult(Activity.RESULT_OK, resultIntent);

                                finish();
                            }
                        });
                    }
                });
                t.start();
            }
        });

        ButtonRectangle cancelBtn = (ButtonRectangle) findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, resultIntent);
                finish();
            }
        });

        // Go To PICK_IMAGE
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, CreateShopActivity.SELECT_PHOTO);
    }

    /*
     * Sets the font on all TextViews in the ViewGroup. Searches recursively for
     * all inner ViewGroups as well. Just add a check for any other views you
     * want to set as well (EditText, etc.)
     */
    public void setFont(ViewGroup group, Typeface font) {
        int count = group.getChildCount();
        View v;
        for (int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if (v instanceof TextView || v instanceof EditText || v instanceof Button) {
                ((TextView) v).setTypeface(font);
            } else if (v instanceof ViewGroup)
                setFont((ViewGroup) v, font);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {

        switch(requestCode) {
            case CreateShopActivity.SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    Uri selectedImageUri = imageReturnedIntent.getData();

                    // không nén ảnh
                    try {
                        croppedImage = Helper.decodeUriNormal(this, selectedImageUri);
                        cropImageView.setImageBitmap(croppedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }else {
                    Intent resultIntent = new Intent();
                    setResult(Activity.RESULT_CANCELED, resultIntent);
                    finish();
                }
        }
    }

}
